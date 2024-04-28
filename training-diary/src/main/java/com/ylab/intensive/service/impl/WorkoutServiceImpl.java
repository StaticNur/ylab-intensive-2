package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Implementation of the WorkoutService interface providing methods for managing workout-related operations.
 */
@ApplicationScoped
@NoArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    /**
     * Workout DAO.
     * This DAO is responsible for data access operations related to workouts.
     */
    private WorkoutDao workoutDao;

    /**
     * User Management Service.
     * This service provides functionality for managing user-related operations.
     */
    private UserService userService;

    /**
     * Audit Service.
     * This service provides functionality for audit-related operations.
     */
    private AuditService auditService;

    /**
     * Workout Information Service.
     * This service provides functionality for managing workout information.
     */
    private WorkoutInfoService workoutInfoService;

    /**
     * Workout Type Service.
     * This service provides functionality for managing workout types.
     */
    private WorkoutTypeService workoutTypeService;

    @Inject
    public WorkoutServiceImpl(WorkoutDao workoutDao, UserService userService,
                              AuditService auditService, WorkoutInfoService workoutInfoService,
                              WorkoutTypeService workoutTypeService) {
        this.workoutDao = workoutDao;
        this.userService = userService;
        this.auditService = auditService;
        this.workoutInfoService = workoutInfoService;
        this.workoutTypeService = workoutTypeService;
    }

    @Override
    @Timed
    @Loggable
    public WorkoutDto addWorkout(String email, WorkoutDto workoutDto) {
        int userId = getAuthorizedUserId(email);
        String type = workoutDto.getType();
        List<WorkoutType> types = workoutTypeService.findByUserId(userId);
        Optional<WorkoutType> typeOptional = types.stream()
                .filter(t -> t.getType().equals(type))
                .findFirst();

        if (typeOptional.isPresent()) {
            LocalDate date = getDate(workoutDto.getDate());
            Optional<Workout> byDate = workoutDao.findByDate(date, userId);
            if (byDate.isPresent()) {
                throw new WorkoutException("Тренировка типа " + typeOptional.get().getType()
                                           + " в " + date + " уже была добавлена! Ее теперь можно только редактировать.");
            }
            Workout workout = new Workout();
            workout.setUuid(UUID.randomUUID());
            workout.setUserId(userId);
            workout.setType(String.valueOf(typeOptional.get().getId()));
            workout.setDate(date);
            workout.setDuration(workoutDto.getDuration());
            workout.setCalorie(workoutDto.getCalorie());
            Workout savedWorkout = workoutDao.saveWorkout(workout);

            for (Map.Entry<String, String> infoMap : workoutDto.getWorkoutInfo().entrySet()) {
                workoutInfoService.saveWorkoutInfo(savedWorkout.getId(), infoMap.getKey(), infoMap.getValue());
            }
            workoutDto.setUuid(savedWorkout.getUuid());
            //auditService.saveAction(userId, "Пользователь добавил новую тренировку. Добавлено: " + workout);
            return workoutDto;
        } else throw new WorkoutException("Тренировка типа " + type
                                          + " не существует для данного пользователя, с начала добавьте ее.");
    }

    @Override
    @Timed
    @Loggable
    public Workout addWorkoutInfo(String uuidStr, WorkoutInfoDto workoutInfoDto) {
        UUID uuid = convertToUUID(uuidStr);
        Workout workout = workoutDao.findByUUID(uuid)
                .orElseThrow(() -> new NotFoundException("Тренировка с uuid = " + uuid +
                                                         " нет в базе данных! Сначала добавьте ее."));
        WorkoutType workoutType = workoutTypeService.findById(Integer.parseInt(workout.getType()));
        workout.setType(workoutType.getType());

        if(workoutInfoDto.getWorkoutInfo() != null){
            for (Map.Entry<String, String> infoMap : workoutInfoDto.getWorkoutInfo().entrySet()) {
                workoutInfoService.saveWorkoutInfo(workout.getId(), infoMap.getKey(), infoMap.getValue());
                workout.getWorkoutInfo().put(infoMap.getKey(), infoMap.getValue());
            }
        }
        return workout;
    }

    @Override
    @Timed
    @Loggable
    public List<WorkoutDto> getAllUserWorkouts(String login) {
        int userId = getAuthorizedUserId(login);
        List<Workout> workoutList = workoutDao.findByUserId(userId);
        List<WorkoutDto> workoutDtoList = new ArrayList<>();
        for (Workout workout : workoutList) {
            WorkoutDto workoutDto = new WorkoutDto();
            workoutDto.setUuid(workout.getUuid());
            workoutDto.setDate(workout.getDate().toString());

            WorkoutTypeDto workoutTypeDto = new WorkoutTypeDto(workoutTypeService
                    .findById(Integer.parseInt(workout.getType())).getType());
            workoutDto.setType(workoutTypeDto.getType());
            workoutDto.setDuration(workout.getDuration());
            workoutDto.setCalorie(workout.getCalorie());

            WorkoutInfoDto workoutInfoDto = new WorkoutInfoDto();
            WorkoutInfo workoutInfo = workoutInfoService.getInfoByWorkoutId(workout.getId());
            workoutInfoDto.setWorkoutInfo(workoutInfo.getWorkoutInfo());
            workoutDto.setWorkoutInfo(workoutInfoDto.getWorkoutInfo());

            workoutDtoList.add(workoutDto);
        }
        return workoutDtoList;
    }

    @Override
    @Timed
    @Loggable
    public Workout updateWorkout(String email, String uuidStr, EditWorkout editWorkout) {
        UUID uuid = convertToUUID(uuidStr);
        Workout workout = workoutDao.findByUUID(uuid)
                .orElseThrow(() -> new NotFoundException("Тренировка с uuid = " + uuid +
                                                         " нет в базе данных! Сначала добавьте ее."));
        int userId = getAuthorizedUserId(email);
        if (editWorkout.getCalorie() != null) {
            updateCalories(userId, workout.getId(), editWorkout.getCalorie());
            workout.setCalorie(editWorkout.getCalorie());
        }
        if (editWorkout.getWorkoutInfo() != null) {
            updateAdditionalInfo(userId, workout.getId(), editWorkout.getWorkoutInfo());
            workout.setWorkoutInfo(editWorkout.getWorkoutInfo());
        } else {
            WorkoutInfo infoByWorkoutId = workoutInfoService.getInfoByWorkoutId(workout.getId());
            workout.setWorkoutInfo(infoByWorkoutId.getWorkoutInfo());
        }
        if (editWorkout.getType() != null) {
            updateType(userId, workout.getId(), editWorkout.getType());
            workout.setType(editWorkout.getType());
        } else {
            WorkoutTypeDto workoutTypeDto = new WorkoutTypeDto(workoutTypeService
                    .findById(Integer.parseInt(workout.getType())).getType());
            workout.setType(workoutTypeDto.getType());
        }
        if (editWorkout.getDuration() != null) {
            updateDuration(userId, workout.getId(), editWorkout.getDuration());
            workout.setDuration(editWorkout.getDuration());
        }
        return workout;
    }

    @Override
    @Timed
    public void updateType(int userId, int workoutId, String type) {
        List<WorkoutType> types = workoutTypeService.findByUserId(userId);

        Optional<WorkoutType> typeOptional = types.stream()
                .filter(t -> t.getType().equals(type))
                .findFirst();

        if (typeOptional.isPresent()) {
            workoutDao.updateType(workoutId, typeOptional.get().getId());
        } else throw new WorkoutInfoException("Такой тип тренировки " + type + " нет в базе данных!");
    }

    @Override
    @Timed
    public void updateDuration(int userId, int workoutId, Duration duration) {
        workoutDao.updateDuration(workoutId, duration);
    }

    @Override
    @Timed
    public void updateCalories(int userId, int workoutId, Float calories) {
        workoutDao.updateCalorie(workoutId, calories);
    }

    @Override
    @Timed
    public void updateAdditionalInfo(int userId, int workoutId, Map<String, String> workoutInfo) {
        for (Map.Entry<String, String> infoMap : workoutInfo.entrySet()) {
            workoutInfoService.updateWorkoutInfo(workoutId, infoMap.getKey(), infoMap.getValue());
        }
    }

    @Override
    @Timed
    @Loggable
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
    @Timed
    @Loggable
    public StatisticsDto getWorkoutStatistics(String email, String beginStr, String endStr) {
        LocalDate begin = getDate(beginStr);
        LocalDate end = getDate(endStr);
        int authorizedUserId = getAuthorizedUserId(email);
        List<Workout> workoutList = workoutDao.findByDuration(authorizedUserId, begin, end);
        int totalCalories = 0;

        for (Workout workout : workoutList) {
            totalCalories += workout.getCalorie();
        }
        return new StatisticsDto(totalCalories);
    }

    @Override
    @Timed
    @Loggable
    public List<User> getAllUsersWorkouts(List<User> userList) {
        for (User user : userList) {
            List<Workout> workoutList = workoutDao.findByUserId(user.getId());
            for (Workout workout : workoutList) {
                WorkoutType workoutType = workoutTypeService.findById(Integer.parseInt(workout.getType()));
                workout.setType(workoutType.getType());
                WorkoutInfo workoutInfo = workoutInfoService.getInfoByWorkoutId(workout.getId());
                workout.setWorkoutInfo(workoutInfo.getWorkoutInfo());
            }
            user.setWorkouts(workoutList);
        }
        return userList;
    }

    @Override
    @Timed
    @Loggable
    public List<WorkoutType> getAllType(String login) {
        int userId = getAuthorizedUserId(login);
        return workoutTypeService.findByUserId(userId);
    }

    @Override
    @Timed
    @Loggable
    public WorkoutType saveWorkoutType(String login, String typeName) {
        int userId = getAuthorizedUserId(login);
        return workoutTypeService.saveType(userId, typeName);
    }

    /**
     * Parses a date string into a LocalDate object.
     *
     * @param dateStr the date string in "dd-MM-yyyy" format
     * @return the parsed LocalDate object
     * @throws DateFormatException if the date string has an incorrect format
     */

    private LocalDate getDate(String dateStr) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new DateFormatException("Incorrect date format. Should be yyyy-MM-dd");
        }
        return date;
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
        try {
            return UUID.fromString(uuidStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(e.getMessage());
        }
    }
}
