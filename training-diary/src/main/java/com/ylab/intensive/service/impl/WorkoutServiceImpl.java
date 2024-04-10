package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.dto.WorkoutDto;
import com.ylab.intensive.exception.*;
import com.ylab.intensive.model.Workout;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.WorkoutService;
import javassist.NotFoundException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class WorkoutServiceImpl implements WorkoutService {
    @Inject
    private WorkoutDao workoutDao;
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
        } else throw new TrainingTypeException("В " + date + " тренировки не было");
    }

    @Override
    public void addWorkout(String date, String typeName, String durationStr, String calorie){
        LocalDate localDate = getDate(date);
        String[] durationHMS = durationStr.split(":");
        if(durationHMS.length != 3){
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
    }

    @Override
    public void addWorkoutInfo(String date, String title, String key) {
        LocalDate localDate = getDate(date);
        Optional<Workout> workoutMayBe = workoutDao.findByDate(localDate);
        if (workoutMayBe.isEmpty()) {
            throw new NotFoundWorkoutException("Тренировка в " + date + " не проводилось! Сначала добавьте ее.");
        }
        workoutDao.saveWorkoutInfo(workoutMayBe.get(), title, key);
    }

    @Override
    public List<WorkoutDto> getAllWorkouts() {
        return workoutDao.findAll().stream()
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
    }

    @Override
    public void updateDuration(WorkoutDto workoutDto, String durationStr) {
        String[] durationHMS = durationStr.split(":");
        Duration duration = Duration.ofHours(Integer.parseInt(durationHMS[0])).plusMinutes(Integer.parseInt(durationHMS[1])).plusSeconds(Integer.parseInt(durationHMS[2]));

        workoutDao.updateDuration(workoutDto, duration);
    }

    @Override
    public void updateCalorie(WorkoutDto workoutDto, String calorie) {

        workoutDao.updateCalorie(workoutDto, Float.parseFloat(calorie));
    }

    @Override
    public void updateWorkoutInfo(WorkoutDto workoutDto, String title, String info) {
        String s = workoutDto.getInfo().get(title);
        if (s.isEmpty()) {
            throw new WorkoutInfoException("Такой заголовок в доп. инфо. нет!");
        }
        workoutDao.updateWorkoutInfo(workoutDto, title, info);
    }

    @Override
    public void deleteWorkout(String date) {
        Optional<WorkoutDto> workoutDto = this.getWorkoutByDate(date);
        if (workoutDto.isEmpty()) {
            throw new NotFoundWorkoutException("В этот день тренировки не было!");
        } else {
            workoutDao.deleteWorkout(workoutDto.get().getDate());
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



    private Workout dtoToEntity(WorkoutDto workoutDto) {
        Workout workout = new Workout();
        workout.setDate(workoutDto.getDate());
        workout.setType(workoutDto.getType());
        workout.setDuration(workoutDto.getDuration());
        workout.setCalorie(workoutDto.getCalorie());
        workout.setInfo(workoutDto.getInfo());
        return workout;
    }

}
