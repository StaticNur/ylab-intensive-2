package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.mapper.WorkoutMapper;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the WorkoutService interface providing methods for managing workout-related operations.
 */
public class WorkoutServiceImpl implements WorkoutService {
    /**
     * Workout DAO.
     * This DAO is responsible for data access operations related to workouts.
     */
    @Inject
    private WorkoutDao workoutDao;

    /**
     * User Management Service.
     * This service provides functionality for managing user-related operations.
     */
    @Inject
    private UserManagementService userManagementService;

    @Inject
    private AuditService auditService;
    @Inject
    private WorkoutInfoService workoutInfoService;
    @Inject
    private WorkoutTypeService workoutTypeService;
    /**
     * Authorized User Session.
     * This session represents the currently authorized user.
     */
    @Inject
    private Session authorizedUser;
    @Inject
    private WorkoutMapper workoutMapper;

    @Override
    public void addTrainingType(String date, String typeName) {
        LocalDate localDate = getDate(date);
        Optional<Workout> workout = workoutDao.findByDate(localDate);
        if (workout.isPresent()) {
            Set<String> types = workoutTypeService.findByWorkoutId(workout.get().getId());
            if (types.contains(typeName)) {
                throw new TrainingTypeException("Такой тип тренировки уже существует в " + date);
            }
            workoutTypeService.saveType(workout.get().getId(), typeName);
            auditService.saveAction(getAuthorizedUserId(), "Пользователь расширил перечень типов тренировок. " +
                                                           "Добавлено: " + typeName + " на " + date);
        } else throw new TrainingTypeException("В " + date + " тренировки не было");
    }

    @Override
    public void addWorkout(String date, String typeName, String durationStr, String calorie) {
        LocalDate localDate = getDate(date);
        String[] durationHMS = durationStr.split(":");
        int userId = getAuthorizedUserId();
        if (durationHMS.length != 3) {
            throw new DateFormatException("Не правильный формат данных (пример для '1 час 5 минут 24 секунды': 1:5:24 )!");
        }
        Duration duration = Duration.ofHours(Integer.parseInt(durationHMS[0]))
                .plusMinutes(Integer.parseInt(durationHMS[1]))
                .plusSeconds(Integer.parseInt(durationHMS[2]));

        Optional<Workout> workoutMayBe = workoutDao.findByDate(localDate);
        if (workoutMayBe.isPresent()) {
            Set<String> types = workoutTypeService.findByWorkoutId(workoutMayBe.get().getId());
            if (types.contains(typeName)) {
                throw new WorkoutException("Тренировка типа " + typeName + " в " + date +
                                           " уже была добавлена! Ее теперь можно только редактировать");
            }
        }
        Workout workout = Workout.builder()
                .userId(userId)
                .date(localDate)
                .duration(duration)
                .calorie(Float.parseFloat(calorie))
                .build();

        Workout savedWorkout = workoutDao.saveWorkout(workout);
        workoutTypeService.saveType(savedWorkout.getId(), typeName);
        auditService.saveAction(userId, "Пользователь добавил новую тренировку. Добавлено: " + workout);
    }

    @Override
    public void addWorkoutInfo(String date, String title, String info) {
        LocalDate localDate = getDate(date);
        Workout workout = workoutDao.findByDate(localDate)
                .orElseThrow(() -> new NotFoundException("Тренировка в " + date + " не проводилось! Сначала добавьте ее."));

        workoutInfoService.saveWorkoutInfo(workout.getId(), title, info);
        auditService.saveAction(getAuthorizedUserId(), "Пользователь добавил дополнительную информацию о тренировке. " +
                                                       "Добавлено: " + title + " " + info);
    }

    @Override
    public List<WorkoutDto> getAllUserWorkouts() {
        auditService.saveAction(getAuthorizedUserId(), "Пользователь просмотрел свои предыдущие тренировки.");
        int userId = getAuthorizedUserId();
        List<Workout> workoutList = workoutDao.findByUserId(userId);
        for (Workout workout: workoutList){
            workout.setType(workoutTypeService.findByWorkoutId(workout.getId()));
            workout.setInfo(workoutInfoService.getInfoByWorkoutId(workout.getId()));
        }
        return workoutList.stream().sorted(Comparator.comparing(Workout::getDate))
                .map(w -> workoutMapper.entityToDto(w))
                .collect(Collectors.toList());
    }

    @Override
    public WorkoutDto getWorkoutByDate(String date) {
        LocalDate localDate = getDate(date);
        Workout workout = workoutDao.findByDate(localDate)
                .orElseThrow(() -> new NotFoundException("Тренировка в " + localDate +
                                                                                                   " не проводилось! Сначала добавьте ее."));;

        workout.setType(workoutTypeService.findByWorkoutId(workout.getId()));
        workout.setInfo(workoutInfoService.getInfoByWorkoutId(workout.getId()));
        return workoutMapper.entityToDto(workout);
    }

    @Override
    public void updateType(WorkoutDto workoutDto, String oldType, String newType) {
        Workout workout = workoutDao.findByDate(workoutDto.getDate())
                .orElseThrow(() -> new NotFoundException("Тренировка в " + workoutDto.getDate() +
                                                         " не проводилось! Сначала добавьте ее."));

        workoutTypeService.updateType(workout.getId(), oldType, newType);
        auditService.saveAction(getAuthorizedUserId(), "Пользователь редактировал тип тренировки с "
                                                       + oldType + " на " + newType);
    }

    @Override
    public void updateDuration(WorkoutDto workoutDto, String durationStr) {
        String[] durationHMS = durationStr.split(":");
        Duration duration = Duration.ofHours(Integer.parseInt(durationHMS[0]))
                                        .plusMinutes(Integer.parseInt(durationHMS[1]))
                                        .plusSeconds(Integer.parseInt(durationHMS[2]));
        Workout workout = workoutDao.findByDate(workoutDto.getDate())
                .orElseThrow(() -> new NotFoundException("Тренировка в " + workoutDto.getDate() +
                                                         " не проводилось! Сначала добавьте ее."));

        workoutDao.updateDuration(workout.getId(), duration);
        auditService.saveAction(getAuthorizedUserId(), "Пользователь редактировал длительность тренировки, теперь " + durationStr);
    }

    @Override
    public void updateCalories(WorkoutDto workoutDto, String calories) {
        Workout workout = workoutDao.findByDate(workoutDto.getDate())
                .orElseThrow(() -> new NotFoundException("Тренировка в " + workoutDto.getDate() +
                                                         " не проводилось! Сначала добавьте ее."));

        workoutDao.updateCalorie(workout.getId(), Float.parseFloat(calories));
        auditService.saveAction(getAuthorizedUserId(), "Пользователь редактировал количество потраченных калорий в "
                                                       + workoutDto.getDate() + ", теперь " + calories);
    }

    @Override
    public void updateAdditionalInfo(WorkoutDto workoutDto, String title, String info) {
        String s = workoutDto.getInfo().get(title);
        if (s == null || s.isEmpty()) {
            throw new WorkoutInfoException("Такой заголовок в доп. инфо. нет!");
        }
        Workout workout = workoutDao.findByDate(workoutDto.getDate())
                .orElseThrow(() -> new NotFoundException("Тренировка в " + workoutDto.getDate()
                                                         + " не проводилось! Сначала добавьте ее."));

        workoutInfoService.updateWorkoutInfo(workout.getId(), title, info);
        auditService.saveAction(getAuthorizedUserId(), "Пользователь редактировал дополнительную информацию для "
                                                       + title + " c " + workoutDto.getInfo().get(title) + " на  " + info);
    }

    @Override
    public void deleteWorkout(String dateStr) {
        LocalDate date = getDate(dateStr);
        Workout workout = workoutDao.findByDate(date).orElseThrow(() -> new NotFoundException("Тренировка в " + date
                                                                                              + " не проводилось! Сначала добавьте ее."));
        workoutTypeService.delete(workout.getId());
        workoutInfoService.delete(workout.getId());
        workoutDao.deleteWorkout(date);
        auditService.saveAction(getAuthorizedUserId(), "Пользователь удалил тренировку: " + date);
    }

    @Override
    public int getWorkoutStatistics(String beginStr, String endStr) {
        LocalDate begin = getDate(beginStr);
        LocalDate end = getDate(endStr);

        List<Workout> workoutList = workoutDao.findByDuration(begin, end);
        int totalCalories = 0;

        for (Workout workout : workoutList) {
            totalCalories += workout.getCalorie();
        }
        auditService.saveAction(getAuthorizedUserId(),
                "Пользователь просмотрел статистику по тренировкам за период " + beginStr + " -- " + endStr);
        return totalCalories;
    }

    @Override
    public List<User> getAllUsersWorkouts(List<User> userList) {
        for (User user: userList){
            List<Workout> workoutList = workoutDao.findByUserId(user.getId());
            for (Workout workout: workoutList){
                workout.setType(workoutTypeService.findByWorkoutId(workout.getId()));
                workout.setInfo(workoutInfoService.getInfoByWorkoutId(workout.getId()));
            }
            user.setWorkout(workoutList);
        }
        return userList;
    }

    private LocalDate getDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new DateFormatException("Не правильный формат даты. Должен быть dd-MM-yyyy");
        }
        return date;
    }

    private int getAuthorizedUserId() {
        UserDto userFromAttribute = (UserDto) authorizedUser.getAttribute("authorizedUser");
        User user = userManagementService.findByEmail(userFromAttribute.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь с email = "
                                                         + userFromAttribute.getEmail() + " не существует!"));
        return user.getId();
    }
}
