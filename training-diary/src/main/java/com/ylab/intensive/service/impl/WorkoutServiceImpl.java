package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.*;
import com.ylab.intensive.util.converter.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of the WorkoutService interface providing methods for managing workout-related operations.
 */
@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {
    /**
     * Workout DAO.
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
    private final Converter converter;

    @Override
    @Loggable
    @Timed
    @Transactional
    public WorkoutDto addWorkout(String email, WorkoutDto workoutDto) {
        int userId = getAuthorizedUserId(email);
        String type = workoutDto.getType();
        List<WorkoutType> types = workoutTypeService.findByUserId(userId);
        Optional<WorkoutType> typeOptional = types.stream()
                .filter(t -> t.getType().equals(type))
                .findFirst();

        if (typeOptional.isPresent()) {
            LocalDate date = convertToDate(workoutDto.getDate());
            Optional<Workout> byDate = workoutDao.findByDate(date, userId);
            if (byDate.isPresent()) {
                throw new WorkoutException("Тренировка типа " + typeOptional.get().getType()
                                           + " в " + date + " уже была добавлена! Ее теперь можно только редактировать.");
            }
            Workout workout = new Workout();
            workout.setUuid(UUID.randomUUID());
            workout.setUserId(userId);
            workout.setType(typeOptional.get().getType());
            workout.setDate(date);
            workout.setDuration(workoutDto.getDuration());
            workout.setCalorie(workoutDto.getCalorie());

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
    @Transactional
    public Workout addWorkoutInfo(String email, String uuidStr, WorkoutInfoDto workoutInfoDto) {
        int userId = getAuthorizedUserId(email);
        UUID uuid = convertToUUID(uuidStr);
        Workout workout = workoutDao.findByUUID(uuid)
                .orElseThrow(() -> new NotFoundException("Тренировка с uuid = " + uuid +
                                                         " нет в базе данных! Сначала добавьте ее."));
        if(workout.getUserId() != userId){
            throw new AccessDeniedException("Дополнительную информацию можно добавлять только в свои тренировочные данные!");
        }
        WorkoutType workoutType = workoutTypeService.findByName(workout.getType());
        workout.setType(workoutType.getType());
        Optional<WorkoutInfo> infoByWorkoutId = workoutInfoService.getInfoByWorkoutId(workout.getId());
        infoByWorkoutId.ifPresent(workoutInfo -> workout.setWorkoutInfo(workoutInfo.getWorkoutInfo()));

        if (workoutInfoDto.getWorkoutInfo() != null) {
            for (Map.Entry<String, String> infoMap : workoutInfoDto.getWorkoutInfo().entrySet()) {
                if(!workout.getWorkoutInfo().containsKey(infoMap.getKey())){
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
    public List<Workout> getAllUserWorkouts(String login) {
        int userId = getAuthorizedUserId(login);
        List<Workout> workoutList = workoutDao.findByUserId(userId);
        for (Workout workout : workoutList) {
            WorkoutInfoDto workoutInfoDto = new WorkoutInfoDto();
            Optional<WorkoutInfo> workoutInfo = workoutInfoService.getInfoByWorkoutId(workout.getId());
            if(workoutInfo.isPresent()){
                workoutInfoDto.setWorkoutInfo(workoutInfo.get().getWorkoutInfo());
            }else {
                workoutInfoDto.setWorkoutInfo(Collections.emptyMap());
            }
            workout.setWorkoutInfo(workoutInfoDto.getWorkoutInfo());
        }
        return workoutList;
    }

    @Override
    @Transactional
    public Workout updateWorkout(String email, String uuidStr, EditWorkout editWorkout) {
        UUID uuid = convertToUUID(uuidStr);
        Workout workout = workoutDao.findByUUID(uuid)
                .orElseThrow(() -> new NotFoundException("Тренировка с uuid = " + uuid +
                                                         " нет в базе данных! Сначала добавьте ее."));
        int userId = getAuthorizedUserId(email);
        if(workout.getUserId() != userId){
            throw new AccessDeniedException("Только свои тренировочные данные можно редактировать!");
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
            updateDuration(workout.getId(), editWorkout.getDuration());
            workout.setDuration(editWorkout.getDuration());
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

        workoutInfo.forEach((key, value) -> {
            if (workoutInfoMap.containsKey(key)) {
                workoutInfoService.updateWorkoutInfo(workoutId, key, value);
            }
            workoutInfoMap.put(key, value);
        });
        return workoutInfoMap;
    }

    @Override
    @Loggable
    @Timed
    @Transactional
    public void deleteWorkout(String email, String uuidStr) {
        UUID uuid = convertToUUID(uuidStr);
        int userId = getAuthorizedUserId(email);
        Workout workout = workoutDao.findByUUID(uuid)
                .orElseThrow(() -> new NotFoundException("Тренировка с uuid = " + uuid +
                                                         " нет в базе данных! Сначала добавьте ее."));
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
        for (User user : userList) {
            List<Workout> workoutList = workoutDao.findByUserId(user.getId());
            for (Workout workout : workoutList) {
                workout.setType(workout.getType());
                Optional<WorkoutInfo> workoutInfo = workoutInfoService.getInfoByWorkoutId(workout.getId());
                if (workoutInfo.isPresent()){
                    workout.setWorkoutInfo(workoutInfo.get().getWorkoutInfo());
                }else {
                    workout.setWorkoutInfo(Collections.emptyMap());
                }
            }
            user.setWorkouts(workoutList);
        }
        return userList;
    }

    @Override
    @Loggable
    @Timed
    public List<WorkoutType> getAllType(String login) {
        int userId = getAuthorizedUserId(login);
        return workoutTypeService.findByUserId(userId);
    }

    @Override
    @Loggable
    @Timed
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
                .orElseThrow(() -> new NotFoundException("User with email = "
                                                         + email + " does not exist!"));
        return user.getId();
    }
    private UUID convertToUUID(String uuidStr) {
        return converter.convert(uuidStr, UUID::fromString, "Invalid UUID");
    }
    private LocalDate convertToDate(String dateStr) {
        return converter.convert(dateStr, LocalDate::parse, "Incorrect date format. Should be yyyy-MM-dd");
    }

}