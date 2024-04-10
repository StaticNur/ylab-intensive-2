package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.dto.WorkoutDto;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.Workout;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.ui.ConsoleText;

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
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output("Введите дату тренировки (dd-MM-yyyy):");
            String date = inputData.input().toString();
            outputData.output("Введите тип тренировки:");
            String type = inputData.input().toString();
            try {
                workoutService.addTrainingType(date, type);
                outputData.output("Новый тип тренировок успешно добавлен!");
                processIsRun = false;
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
                outputData.output("Попытаться еще раз? 1-да, 2-нет");
                processIsRun = inputData.input().toString().equals("1");
            }
        }
    }

    public void addWorkout() {
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output("Введите дату тренировки (dd-MM-yyyy):");
            String date = inputData.input().toString();
            outputData.output("Введите тип тренировки:");
            String type = inputData.input().toString();
            outputData.output("Введите длительность тренировки (hh:mm:ss):");
            String duration = inputData.input().toString();
            outputData.output("Введите количество потраченных калории во время тренировки:");
            String calorie = inputData.input().toString();
            try {
                workoutService.addWorkout(date, type, duration, calorie);
                outputData.output("Новая тренировок успешно добавлена!");
                processIsRun = false;
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
                outputData.output("Попытаться еще раз? 1-да, 2-нет");
                processIsRun = inputData.input().toString().equals("1");
            }
        }
    }

    public void addWorkoutInfo() {
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output("Введите дату тренировки (dd-MM-yyyy):");
            String date = inputData.input().toString();
            outputData.output("Введите заголовок для добавления дополнительной информации (например: пройденное расстояние):");
            String title = inputData.input().toString();
            outputData.output("Введите дополнительную информации (например: 3,67 км):");
            String info = inputData.input().toString();
            try {
                workoutService.addWorkoutInfo(date, title, info);
                outputData.output("дополнительную информации о тренировке успешно добавлена!");
                processIsRun = false;
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
                outputData.output("Попытаться еще раз? 1-да, 2-нет");
                processIsRun = inputData.input().toString().equals("1");
            }
        }
    }

    public void showWorkoutHistory() {
        List<WorkoutDto> workouts = workoutService.getAllWorkouts();
        if (workouts.isEmpty()) {
            outputData.errOutput("Вы еще не добавили тренировки!");
        } else outputData.output(workouts);
    }

    public void editWorkout() {
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output("Введите дату тренировки которую хотите редактировать (dd-MM-yyyy):");
            String date = inputData.input().toString();
            Optional<WorkoutDto> workoutDto = Optional.empty();
            try {
                workoutDto = workoutService.getWorkoutByDate(date);
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
            }
            if (workoutDto.isEmpty()) {
                outputData.errOutput("В этот день не было тренировки");
                outputData.output("Попытаться еще раз? 1-да, 2-нет");
                processIsRun = inputData.input().toString().equals("1");
            } else {
                outputData.output(workoutDto.get());
                outputData.output("Редактировать: " +
                                  "\n 1 - тип тренировки " +
                                  "\n 2 - длительность тренировки " +
                                  "\n 3 - количество потраченных калорий " +
                                  "\n 4 - дополнительная информация");
                String choose = inputData.input().toString();
                try {
                    switch (choose) {
                        case "1" -> {
                            outputData.output("Введите текущий тип тренировки:");
                            String oldType = inputData.input().toString();
                            outputData.output("Введите новый тип тренировки:");
                            String newType = inputData.input().toString();
                            workoutService.updateType(workoutDto.get(), oldType, newType);
                            outputData.output("Данные успешно изменены!");
                            processIsRun = false;
                        }
                        case "2" -> {
                            outputData.output("Введите новую длительность тренировки:");
                            String duration = inputData.input().toString();
                            workoutService.updateDuration(workoutDto.get(), duration);
                            outputData.output("Данные успешно изменены!");
                            processIsRun = false;
                        }
                        case "3" -> {
                            outputData.output("Введите новое количество потраченных калории:");
                            String calorie = inputData.input().toString();
                            workoutService.updateCalorie(workoutDto.get(), calorie);
                            outputData.output("Данные успешно изменены!");
                            processIsRun = false;
                        }
                        case "4" -> {
                            outputData.output("Введите заголовок дополнительной информации:");
                            String title = inputData.input().toString();
                            outputData.output("Введите новые данные для этого заголовка (например: 11,6 км):");
                            String info = inputData.input().toString();
                            workoutService.updateWorkoutInfo(workoutDto.get(), title, info);
                            outputData.output("Данные успешно изменены!");
                            processIsRun = false;
                        }
                        default -> outputData.output(ConsoleText.UNKNOWN_COMMAND);
                    }
                } catch (RuntimeException e) {
                    outputData.errOutput(e.getMessage());
                    outputData.output("Попытаться еще раз? 1-да, 2-нет");
                    processIsRun = inputData.input().toString().equals("1");
                }
            }
        }
    }

    public void deleteWorkout() {
        boolean processIsRun = true;
        while (processIsRun) {
            try {
                outputData.output("Введите дату тренировки которую хотите удалить (dd-MM-yyyy):");
                String date = inputData.input().toString();
                workoutService.deleteWorkout(date);
                processIsRun = false;
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
                outputData.output("Попытаться еще раз? 1-да, 2-нет");
                processIsRun = inputData.input().toString().equals("1");
            }
        }
    }

    public void showWorkoutStatistics() {

        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output("Показать статистики по тренировкам в определенном диапазоне:");
            try {
                outputData.output("Введите дату начала (dd-MM-yyyy):");
                String begin = inputData.input().toString();
                outputData.output("Введите дату конца (dd-MM-yyyy):");
                String end = inputData.input().toString();

                int workoutStatistics = workoutService.getWorkoutStatistics(begin, end);
                outputData.output("Количество потраченных калорий в разрезе времени: " + workoutStatistics);
                processIsRun = false;
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
                outputData.output("Попытаться еще раз? 1-да, 2-нет");
                processIsRun = inputData.input().toString().equals("1");
            }
        }
    }
}
