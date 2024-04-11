package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.UserManagementService;
import com.ylab.intensive.service.WorkoutService;

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
    @Inject
    private WorkoutDao workoutDao;
    @Inject
    private UserManagementService userManagementService;
    @Inject
    private Session authorizedUser;

    public void setAuthorizedWorkoutDB(List<Workout> workouts) {
        workoutDao.init(workouts);
    }

    @Override
    public void addTrainingType(String date, String typeName) {
        LocalDate localDate = getDate(date);
        Optional<Workout> workout = workoutDao.findByDate(localDate);
        if (workout.isPresent()) {
            if (workout.get().getType().contains(typeName)) {
                throw new TrainingTypeException("Такой тип тренировки уже существует в " + date);
            }
            workoutDao.saveType(workout.get(), typeName);
            userManagementService.saveAction("Пользователь расширил перечень типов тренировок. Добавлено: " + typeName + " на " + date);
        } else throw new TrainingTypeException("В " + date + " тренировки не было");
    }

    @Override
    public void addWorkout(String date, String typeName, String durationStr, String calorie) {
        LocalDate localDate = getDate(date);
        String[] durationHMS = durationStr.split(":");
        if (durationHMS.length != 3) {
            throw new DateFormatException("Не правильный формат данных (пример для '1 час 5 минут 24 секунды': 1:5:24 )!");
        }
        Duration duration = Duration.ofHours(Integer.parseInt(durationHMS[0])).plusMinutes(Integer.parseInt(durationHMS[1])).plusSeconds(Integer.parseInt(durationHMS[2]));

        Optional<Workout> workoutMayBe = workoutDao.findByDate(localDate);
        if (workoutMayBe.isPresent()) {
            throw new WorkoutException("Тренировка в " + date + " уже была добавлена! Ее теперь можно только редактировать");
        }
        int size = workoutDao.getSize();
        Workout workout = new Workout(
                size,
                localDate,
                new HashSet<>(),
                duration,
                Float.parseFloat(calorie),
                new HashMap<>()
        );
        workout.getType().add(typeName);

        workoutDao.saveWorkout(workout);
        userManagementService.saveAction("Пользователь добавил новую тренировку. Добавлено: " + workout);
    }

    @Override
    public void addWorkoutInfo(String date, String title, String info) {
        LocalDate localDate = getDate(date);
        Optional<Workout> workoutMayBe = workoutDao.findByDate(localDate);
        if (workoutMayBe.isEmpty()) {
            throw new NotFoundWorkoutException("Тренировка в " + date + " не проводилось! Сначала добавьте ее.");
        }
        workoutDao.saveWorkoutInfo(workoutMayBe.get(), title, info);
        userManagementService.saveAction("Пользователь добавил дополнительную информацию о тренировке. Добавлено: " + title + " " + info);
    }

    @Override
    public List<WorkoutDto> getAllWorkouts() {
        userManagementService.saveAction("Пользователь просмотрел свои предыдущие тренировки.");
        return workoutDao.findAll().stream()
                .sorted(Comparator.comparing(Workout::getDate))
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WorkoutDto> getWorkoutByDate(String date) {
        LocalDate localDate = getDate(date);
        Optional<Workout> workout = workoutDao.findByDate(localDate);
        return Optional.of(workout.map(this::entityToDto)).orElseGet(Optional::empty);
    }

    @Override
    public void updateType(WorkoutDto workoutDto, String oldType, String newType) {
        workoutDao.updateType(workoutDto, oldType, newType);
        userManagementService.saveAction("Пользователь редактировал тип тренировки с " + oldType + " на " + newType);
    }

    @Override
    public void updateDuration(WorkoutDto workoutDto, String durationStr) {
        String[] durationHMS = durationStr.split(":");
        Duration duration = Duration.ofHours(Integer.parseInt(durationHMS[0])).plusMinutes(Integer.parseInt(durationHMS[1])).plusSeconds(Integer.parseInt(durationHMS[2]));
        workoutDao.updateDuration(workoutDto, duration);
        userManagementService.saveAction("Пользователь редактировал длительность тренировки, теперь " + durationStr);
    }

    @Override
    public void updateCalories(WorkoutDto workoutDto, String calories) {
        workoutDao.updateCalorie(workoutDto, Float.parseFloat(calories));
        userManagementService.saveAction("Пользователь редактировал количество потраченных калорий в " + workoutDto.getDate() + ", теперь " + calories);
    }

    @Override
    public void updateAdditionalInfo(WorkoutDto workoutDto, String title, String info) {
        String s = workoutDto.getInfo().get(title);
        if (s == null || s.isEmpty()) {
            throw new WorkoutInfoException("Такой заголовок в доп. инфо. нет!");
        }
        workoutDao.updateWorkoutInfo(workoutDto, title, info);
        userManagementService.saveAction("Пользователь редактировал дополнительную информацию для "
                                         + title + " c " + workoutDto.getInfo().get(title) + " на  " + info);
    }

    @Override
    public void deleteWorkout(String date) {
        Optional<WorkoutDto> workoutDto = this.getWorkoutByDate(date);
        if (workoutDto.isEmpty()) {
            throw new NotFoundWorkoutException("В этот день тренировки не было!");
        } else {
            workoutDao.deleteWorkout(workoutDto.get().getDate());
            userManagementService.saveAction("Пользователь удалил тренировку: " + date);
        }
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
        userManagementService.saveAction("Пользователь просмотрел статистику по тренировкам за период " + beginStr + " -- " + endStr);
        return totalCalories;
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

    private WorkoutDto entityToDto(Workout workout) {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(workout.getDate());
        workoutDto.setType(workout.getType());
        workoutDto.setDuration(workout.getDuration());
        workoutDto.setCalorie(workout.getCalorie());
        workoutDto.setInfo(workout.getInfo());
        return workoutDto;
    }
}
