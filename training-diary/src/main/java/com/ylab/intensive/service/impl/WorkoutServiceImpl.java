package com.ylab.intensive.service.impl;

import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.*;
import com.ylab.intensive.util.converter.Converter;
import io.ylab.loggingspringbootstarter.annotation.Loggable;
import io.ylab.loggingspringbootstarter.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the WorkoutService interface providing methods for managing workout-related operations.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    /**
     * This DAO is responsible for data access operations related to workouts.
     */
    private final WorkoutDao workoutDao;

    /**
     * User Management Service.
     * This service provides functionality for managing user-related operations.
     */
    private final UserService userService;

    /**
     * Workout Information Service.
     * This service provides functionality for managing workout information.
     */
    private final WorkoutInfoService workoutInfoService;

    /**
     * Workout Type Service.
     * This service provides functionality for managing workout types.
     */
    private final WorkoutTypeService workoutTypeService;

    /**
     * The converter used for converting one type of object to another.
     */
    private final Converter converter;

    @Override
    @Loggable
    @Timed
    @CacheEvict(value = "viewHistoryWorkouts", key = "#email")
    @Transactional
    public WorkoutDto addWorkout(String email, WorkoutDto workoutDto) {
        int userId = getAuthorizedUserId(email);
        String type = workoutDto.getType();
        List<WorkoutType> types = workoutTypeService.findByUserId(userId);
        Optional<WorkoutType> typeOptional = types.stream()
                .filter(t -> t.getType().equals(type))
                .findFirst();

        if (typeOptional.isPresent()) {
            WorkoutType typeInput = typeOptional.get();
            LocalDate date = convertToDate(workoutDto.getDate());
            Optional<Workout> byDate = workoutDao.findByDate(date, userId);
            if (byDate.isPresent() && typeInput.getType().equals(byDate.get().getType())) {
                throw new WorkoutException("Тренировка типа " + typeInput.getType()
                                           + " в " + date + " уже была добавлена! Ее теперь можно только редактировать.");
            }
            Workout workout = generateNewWorkout(workoutDto, userId, typeInput, date);

            Workout savedWorkout = workoutDao.saveWorkout(workout);

            for (Map.Entry<String, String> infoMap : workoutDto.getWorkoutInfo().entrySet()) {
                workoutInfoService.saveWorkoutInfo(savedWorkout.getId(), infoMap.getKey(), infoMap.getValue());
            }
            workoutDto.setUuid(savedWorkout.getUuid());
            return workoutDto;
        } else throw new WorkoutException("Тренировка типа " + type
                                          + " не существует для данного пользователя, с начала добавьте ее.");
    }

    @Override
    @Loggable
    @Timed
    @CacheEvict(value = "viewHistoryWorkouts", key = "#email")
    @Transactional
    public Workout addWorkoutInfo(String email, String uuidStr, WorkoutInfoDto workoutInfoDto) {
        int userId = getAuthorizedUserId(email);
        UUID uuid = convertToUUID(uuidStr);
        Workout workout = getWorkoutByUUID(uuid);
        if (workout.getUserId() != userId) {
            throw new InvalidInputException("Дополнительную информацию можно добавлять только в свои тренировочные данные!");
        }

        WorkoutType workoutType = workoutTypeService.findByName(workout.getType());
        workout.setType(workoutType.getType());
        Optional<WorkoutInfo> infoByWorkoutId = workoutInfoService.getInfoByWorkoutId(workout.getId());
        infoByWorkoutId.ifPresent(workoutInfo -> workout.setWorkoutInfo(workoutInfo.getWorkoutInfo()));

        if (workoutInfoDto.getWorkoutInfo() != null) {
            for (Map.Entry<String, String> infoMap : workoutInfoDto.getWorkoutInfo().entrySet()) {
                if (!workout.getWorkoutInfo().containsKey(infoMap.getKey())) {
                    workoutInfoService.saveWorkoutInfo(workout.getId(), infoMap.getKey(), infoMap.getValue());
                }
                workout.getWorkoutInfo().put(infoMap.getKey(), infoMap.getValue());
            }
        }
        return workout;
    }

    @Override
    @Loggable
    @Timed
    @Cacheable(value = "viewHistoryWorkouts", key = "#login")
    public List<Workout> getAllWorkoutsByUser(String login) {
        int userId = getAuthorizedUserId(login);
        List<Workout> workoutList = workoutDao.findByUserId(userId);
        workoutList.forEach(workout -> {
            Optional<WorkoutInfo> workoutInfo = workoutInfoService.getInfoByWorkoutId(workout.getId());
            workout.setWorkoutInfo(workoutInfo
                    .map(WorkoutInfo::getWorkoutInfo)
                    .orElse(Collections.emptyMap()));
        });
        return workoutList;
    }

    @Override
    @Transactional
    @CacheEvict(value = "viewHistoryWorkouts", key = "#email")
    public Workout updateWorkout(String email, String uuidStr, EditWorkout editWorkout) {
        UUID uuid = convertToUUID(uuidStr);
        Workout workout = getWorkoutByUUID(uuid);
        int userId = getAuthorizedUserId(email);
        if (workout.getUserId() != userId) {
            throw new InvalidInputException("Только свои тренировочные данные можно редактировать!");
        }
        if (editWorkout.getCalorie() != null) {
            updateCalories(workout.getId(), editWorkout.getCalorie());
            workout.setCalorie(editWorkout.getCalorie());
        }
        if (editWorkout.getWorkoutInfo() != null) {
            Map<String, String> allInfo = updateAdditionalInfo(workout.getId(), editWorkout.getWorkoutInfo());
            workout.setWorkoutInfo(allInfo);
        } else {
            Optional<WorkoutInfo> workoutInfo = workoutInfoService.getInfoByWorkoutId(workout.getId());
            workoutInfo.ifPresent(info -> workout.getWorkoutInfo().putAll(info.getWorkoutInfo()));
        }
        if (editWorkout.getType() != null) {
            updateType(userId, workout.getId(), editWorkout.getType());
            workout.setType(editWorkout.getType());
        }
        if (editWorkout.getDuration() != null) {
            Duration duration = convertToDuration(editWorkout.getDuration());
            updateDuration(workout.getId(), duration);
            workout.setDuration(duration);
        }
        return workout;
    }

    @Override
    @Loggable
    @Timed
    public void updateType(int userId, int workoutId, String type) {
        WorkoutType workoutType = workoutTypeService.findTypeByUserId(userId, type.trim());
        workoutDao.updateType(workoutId, workoutType.getType());
    }

    @Override
    @Loggable
    @Timed
    public void updateDuration(int workoutId, Duration duration) {
        workoutDao.updateDuration(workoutId, duration);
    }

    @Override
    @Loggable
    @Timed
    public void updateCalories(int workoutId, Float calories) {
        workoutDao.updateCalorie(workoutId, calories);
    }

    @Override
    @Loggable
    @Timed
    public Map<String, String> updateAdditionalInfo(int workoutId, Map<String, String> workoutInfo) {
        Optional<WorkoutInfo> infoByWorkoutId = workoutInfoService.getInfoByWorkoutId(workoutId);
        Map<String, String> workoutInfoMap = infoByWorkoutId.map(WorkoutInfo::getWorkoutInfo)
                .orElse(new HashMap<>());
        int sizeBefore = 0;
        for (Map.Entry<String, String> map: workoutInfo.entrySet()){
            if (workoutInfoMap.containsKey(map.getKey())) {
                workoutInfoService.updateWorkoutInfo(workoutId, map.getKey(), map.getValue());
                workoutInfoMap.put(map.getKey(), map.getValue());
                sizeBefore++;
            }
        }
        if (sizeBefore == 0) {
            throw new InvalidInputException("Ни один из заголовков не был изменен." +
                                            " Для внесения изменений введите существующий заголовок дополнительной " +
                                            " информации для данной тренировки.");
        }
        return workoutInfoMap;
    }

    @Override
    @Loggable
    @Timed
    @CacheEvict(value = "viewHistoryWorkouts", key = "#email")
    @Transactional
    public void deleteWorkout(String email, String uuidStr) {
        UUID uuid = convertToUUID(uuidStr);
        int userId = getAuthorizedUserId(email);
        Workout workout = getWorkoutByUUID(uuid);
        workoutInfoService.delete(workout.getId());
        workoutDao.deleteWorkout(userId, workout.getId());
    }

    @Override
    @Loggable
    @Timed
    public StatisticsDto getWorkoutStatistics(String email, String beginStr, String endStr) {
        LocalDate begin = convertToDate(beginStr);
        LocalDate end = convertToDate(endStr);
        int authorizedUserId = getAuthorizedUserId(email);

        List<Workout> workoutList = workoutDao.findByDuration(authorizedUserId, begin, end);

        float totalCalories = (float) workoutList.stream()
                .mapToDouble(Workout::getCalorie)
                .sum();
        return new StatisticsDto(totalCalories);
    }

    @Override
    @Loggable
    @Timed
    public List<User> getAllUsersWorkouts(List<User> userList) {
        return userList.stream()
                .map(user -> {
                    List<Workout> workoutList = getAllWorkoutsByUser(user.getEmail());
                    user.setWorkouts(workoutList);
                    return user;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Loggable
    @Timed
    @Cacheable(value = "findWorkoutTypesByUserId", key = "#login")
    public List<WorkoutType> getAllType(String login) {
        int userId = getAuthorizedUserId(login);
        return workoutTypeService.findByUserId(userId);
    }

    @Override
    @Loggable
    @Timed
    @CacheEvict(value = "findWorkoutTypesByUserId", key = "#login")
    @Transactional
    public WorkoutType saveWorkoutType(String login, String typeName) {
        int userId = getAuthorizedUserId(login);
        return workoutTypeService.saveType(userId, typeName);
    }

    /**
     * Retrieves the ID of the currently authorized user.
     *
     * @return the ID of the authorized user
     * @throws NotFoundException if the authorized user is not found
     */
    private int getAuthorizedUserId(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> {
                    String message = "User with email = " + email + " does not exist!";
                    log.error(message);
                    return new NotFoundException(message);
                });
        return user.getId();
    }

    /**
     * Retrieves a workout from the database based on the provided UUID.
     *
     * @param uuid the UUID of the workout to retrieve
     * @return the workout corresponding to the UUID
     * @throws NotFoundException if the workout with the specified UUID is not found in the database
     */
    private Workout getWorkoutByUUID(UUID uuid) {
        return workoutDao.findByUUID(uuid)
                .orElseThrow(() -> {
                    String message = "Тренировка с uuid = " + uuid + " нет в базе данных! Сначала добавьте ее.";
                    log.error(message);
                    return new NotFoundException(message);
                });
    }

    /**
     * Generates a Workout entity based on the provided parameters.
     *
     * @param workoutDto the DTO containing workout information
     * @param userId     the ID of the user associated with the workout
     * @param type       the type of the workout
     * @param date       the date of the workout
     * @return the newly generated Workout entity
     */
    private Workout generateNewWorkout(WorkoutDto workoutDto, int userId, WorkoutType type, LocalDate date) {
        Workout workout = new Workout();
        workout.setUuid(UUID.randomUUID());
        workout.setUserId(userId);
        workout.setType(type.getType());
        workout.setDate(date);
        workout.setDuration(convertToDuration(workoutDto.getDuration()));
        workout.setCalorie(workoutDto.getCalorie());
        return workout;
    }

    private Duration convertToDuration(String durationStr) {
        return converter.convert(durationStr, d ->{
            String[] durationHMS = d.split(":");
            return Duration.ofHours(Integer.parseInt(durationHMS[0]))
                    .plusMinutes(Integer.parseInt(durationHMS[1]))
                    .plusSeconds(Integer.parseInt(durationHMS[2]));
        }, "Invalid Duration");
    }

    /**
     * Converts a string representation of a UUID to a UUID object.
     *
     * @param uuidStr the string representation of the UUID
     * @return the UUID object
     * @throws IllegalArgumentException if the string cannot be parsed into a valid UUID
     */
    private UUID convertToUUID(String uuidStr) {
        return converter.convert(uuidStr, UUID::fromString, "Invalid UUID");
    }

    /**
     * Converts a string representation of a date to a LocalDate object.
     *
     * @param dateStr the string representation of the date
     * @return the LocalDate object
     * @throws IllegalArgumentException if the string cannot be parsed into a valid date
     */
    private LocalDate convertToDate(String dateStr) {
        return converter.convert(dateStr, LocalDate::parse, "Incorrect date format. Should be yyyy-MM-dd");
    }
}