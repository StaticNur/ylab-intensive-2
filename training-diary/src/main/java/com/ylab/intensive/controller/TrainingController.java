package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.service.WorkoutService;

import java.util.List;
import java.util.Optional;

public class TrainingController {
    @Inject
    private InputData inputData;
    @Inject
    private OutputData outputData;
    @Inject
    private WorkoutService workoutService;

    public void addTrainingType() {
        String date = getUserInput("Введите дату тренировки (dd-MM-yyyy):");
        String type = getUserInput("Введите тип тренировки:");
        try {
            workoutService.addTrainingType(date, type);
            outputData.output("Новый тип тренировок успешно добавлен!");
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    public void addWorkout() {
        String date = getUserInput("Введите дату тренировки (dd-MM-yyyy):");
        String type = getUserInput("Введите тип тренировки:");
        String duration = getUserInput("Введите длительность тренировки в формате hh:mm:ss (пример для '5 часов 7 минут 25 секунды': 5:7:25)");
        String calorie = getUserInput("Введите количество потраченных калорий во время тренировки:");
        try {
            workoutService.addWorkout(date, type, duration, calorie);
            outputData.output("Новая тренировка успешно добавлена!");
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    public void addWorkoutInfo() {
        String date = getUserInput("Введите дату тренировки (dd-MM-yyyy):");
        String title = getUserInput("Введите заголовок для добавления дополнительной информации (например: пройденное расстояние):");
        String info = getUserInput("Введите дополнительную информацию (например: 3,67 км):");
        try {
            workoutService.addWorkoutInfo(date, title, info);
            outputData.output("Дополнительная информация о тренировке успешно добавлена!");
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    public void showWorkoutHistory() {
        List<WorkoutDto> workouts = workoutService.getAllWorkouts();
        if (workouts.isEmpty()) {
            outputData.errOutput("Вы еще не добавили тренировки!");
        } else {
            outputData.output(workouts);
        }
    }

    public void editWorkout() {
        String date = getUserInput("Введите дату тренировки, которую хотите редактировать (dd-MM-yyyy):");
        try {
            Optional<WorkoutDto> workoutDto = workoutService.getWorkoutByDate(date);
            if (workoutDto.isEmpty()) {
                outputData.errOutput("В этот день не было тренировки");
                return;
            }
            outputData.output(workoutDto.get());
            outputData.output("Редактировать: " +
                              "\n 1 - тип тренировки " +
                              "\n 2 - длительность тренировки " +
                              "\n 3 - количество потраченных калорий " +
                              "\n 4 - дополнительная информация");
            String choice = getUserInput("");
            switch (choice) {
                case "1" -> editType(workoutDto.get());
                case "2" -> editDuration(workoutDto.get());
                case "3" -> editCalories(workoutDto.get());
                case "4" -> editAdditionalInfo(workoutDto.get());
                default -> outputData.output("Неизвестная команда!");
            }
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    private void editType(WorkoutDto workoutDto) {
        String oldType = getUserInput("Введите текущий тип тренировки:");
        String newType = getUserInput("Введите новый тип тренировки:");
        workoutService.updateType(workoutDto, oldType, newType);
        outputData.output("Тип тренировки успешно изменен!");
    }

    private void editDuration(WorkoutDto workoutDto) {
        String newDuration = getUserInput("Введите новую длительность тренировки:");
        workoutService.updateDuration(workoutDto, newDuration);
        outputData.output("Длительность тренировки успешно изменена!");
    }

    private void editCalories(WorkoutDto workoutDto) {
        String newCalories = getUserInput("Введите новое количество потраченных калорий:");
        workoutService.updateCalories(workoutDto, newCalories);
        outputData.output("Количество потраченных калорий успешно изменено!");
    }

    private void editAdditionalInfo(WorkoutDto workoutDto) {
        String title = getUserInput("Введите заголовок дополнительной информации:");
        String info = getUserInput("Введите новые данные для этого заголовка (например: 11,6 км):");
        workoutService.updateAdditionalInfo(workoutDto, title, info);
        outputData.output("Дополнительная информация успешно изменена!");
    }

    public void deleteWorkout() {
        String date = getUserInput("Введите дату тренировки, которую хотите удалить (dd-MM-yyyy):");
        try {
            workoutService.deleteWorkout(date);
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    public void showWorkoutStatistics() {
        outputData.output("Показать статистики по тренировкам в определенном диапазоне:");
        String begin = getUserInput("Введите дату начала (dd-MM-yyyy):");
        String end = getUserInput("Введите дату конца (dd-MM-yyyy):");
        try {
            int workoutStatistics = workoutService.getWorkoutStatistics(begin, end);
            outputData.output("Количество потраченных калорий в разрезе времени: " + workoutStatistics);
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    private String getUserInput(String message) {
        outputData.output(message);
        return inputData.input().toString();
    }

    private void handleException(Exception e) {
        outputData.errOutput(e.getMessage());
    }
}

