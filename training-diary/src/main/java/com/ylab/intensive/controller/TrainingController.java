package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.ui.AnsiColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static com.ylab.intensive.ui.ConsoleText.UNKNOWN_COMMAND;

/**
 * The Training Controller class handles user interactions related to workouts management.
 */
public class TrainingController {
    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Input Data.
     * This class provides access to the input data entered by the user.
     */
    @Inject
    private InputData inputData;

    /**
     * Output Data.
     * This class allows outputting data to the user.
     */
    @Inject
    private OutputData outputData;

    /**
     * Workout Service.
     * This service provides functionality for working with workouts.
     */
    @Inject
    private WorkoutService workoutService;

    /**
     * ANSI Color.
     * This class provides color functionality for console output.
     */
    @Inject
    private AnsiColor color;


    /**
     * Adds a new training type.
     */
    public void addTrainingType() {
        String date = getUserInput(color.yellowBackground("Введите дату тренировки (dd-MM-yyyy):"));
        String type = getUserInput(color.yellowBackground("Введите тип тренировки:"));
        try {
            workoutService.addTrainingType(date, type);
            outputData.output(color.greenBackground("Новый тип тренировок успешно добавлен!"));
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    /**
     * Adds a new workout.
     */
    public void addWorkout() {
        String date = getUserInput(color.yellowBackground("Введите дату тренировки (dd-MM-yyyy):"));
        String type = getUserInput(color.yellowBackground("Введите тип тренировки:"));
        String duration = getUserInput(color.yellowBackground("Введите длительность тренировки в формате hh:mm:ss " +
                                                              "(пример для '5 часов 7 минут 25 секунды': 5:7:25)"));
        String calorie = getUserInput(color.yellowBackground("Введите количество потраченных калорий во время тренировки:"));
        try {
            workoutService.addWorkout(date, type, duration, calorie);
            outputData.output(color.greenBackground("Новая тренировка успешно добавлена!"));
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    /**
     * Adds additional information to a workout.
     */
    public void addWorkoutInfo() {
        String date = getUserInput(color.yellowBackground("Введите дату тренировки (dd-MM-yyyy):"));
        String title = getUserInput(color.yellowBackground("Введите заголовок для добавления дополнительной " +
                                                           "информации (например: пройденное расстояние):"));
        String info = getUserInput(color.yellowBackground("Введите дополнительную информацию (например: 3,67 км):"));
        try {
            workoutService.addWorkoutInfo(date, title, info);
            outputData.output(color.greenBackground("Дополнительная информация о тренировке успешно добавлена!"));
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    /**
     * Shows the workout history.
     */
    public void showWorkoutHistory() {
        List<WorkoutDto> workouts = workoutService.getAllUserWorkouts();
        if (workouts.isEmpty()) {
            outputData.errOutput("Вы еще не добавили тренировки!");
        } else {
            StringBuilder formattedWorkouts = new StringBuilder();
            for (WorkoutDto workout : workouts) {
                buildUIForWorkout(workout, formattedWorkouts);
            }
            outputData.output(color.greenBackground(formattedWorkouts.toString()));
        }
    }

    /**
     * Edits a workout.
     */
    public void editWorkout() {
        String date = getUserInput(color.yellowBackground("Введите дату тренировки, которую хотите редактировать (dd-MM-yyyy):"));
        try {
            WorkoutDto workoutDto = workoutService.getWorkoutByDate(date);
            StringBuilder formattedWorkouts = new StringBuilder();
            buildUIForWorkout(workoutDto, formattedWorkouts);
            outputData.output(color.greenBackground(formattedWorkouts.toString()));
            outputData.output(color.greyBackground("Редактировать: " +
                                                   "\n 1 - тип тренировки " +
                                                   "\n 2 - длительность тренировки " +
                                                   "\n 3 - количество потраченных калорий " +
                                                   "\n 4 - дополнительная информация"));
            String choice = getUserInput("");
            switch (choice) {
                case "1" -> editType(workoutDto);
                case "2" -> editDuration(workoutDto);
                case "3" -> editCalories(workoutDto);
                case "4" -> editAdditionalInfo(workoutDto);
                default -> outputData.errOutput(UNKNOWN_COMMAND);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    /**
     * Deletes a workout.
     */
    public void deleteWorkout() {
        String date = getUserInput(color.yellowBackground("Введите дату тренировки, которую хотите удалить (dd-MM-yyyy):"));
        try {
            workoutService.deleteWorkout(date);
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    /**
     * Shows workout statistics.
     */
    public void showWorkoutStatistics() {
        outputData.output(color.yellowText("Показать статистики по тренировкам в определенном диапазоне:"));
        String begin = getUserInput(color.yellowBackground("Введите дату начала (dd-MM-yyyy):"));
        String end = getUserInput(color.yellowBackground("Введите дату конца (dd-MM-yyyy):"));
        try {
            int workoutStatistics = workoutService.getWorkoutStatistics(begin, end);
            outputData.output(color.greenBackground("Количество потраченных калорий в разрезе времени с "
                                                    + begin + " по " + end + ": " + workoutStatistics));
        } catch (RuntimeException e) {
            handleException(e);
        }
    }

    // Private helper methods


    private void buildUIForWorkout(WorkoutDto workout, StringBuilder formattedWorkouts) {
        Duration duration = workout.getDuration();
        String viewDuration = String.format("%dч. %dм. %dс.", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        formattedWorkouts.append("date = ").append(workout.getDate()).append(", ");
        formattedWorkouts.append("types = ").append(workout.getType()).append(", ");
        formattedWorkouts.append("duration = ").append(viewDuration).append(", ");
        formattedWorkouts.append("calorie = ").append(workout.getCalorie()).append(", ");
        formattedWorkouts.append("info = ").append(workout.getInfo()).append("\n");
    }

    /**
     * Edits the type of a workout.
     */
    private void editType(WorkoutDto workoutDto) {
        String oldType = getUserInput(color.yellowBackground("Введите текущий тип тренировки:"));
        String newType = getUserInput(color.yellowBackground("Введите новый тип тренировки:"));
        workoutService.updateType(workoutDto, oldType, newType);
        outputData.output(color.greenBackground("Тип тренировки успешно изменен!"));
    }

    /**
     * Edits the duration of a workout.
     */
    private void editDuration(WorkoutDto workoutDto) {
        String newDuration = getUserInput(color.yellowBackground("Введите новую длительность тренировки в формате" +
                                                                 " hh:mm:ss (пример для '5 часов 7 минут 25 секунды': 5:7:25)"));
        workoutService.updateDuration(workoutDto, newDuration);
        outputData.output(color.greenBackground("Длительность тренировки успешно изменена!"));
    }

    /**
     * Edits the calories of a workout.
     */
    private void editCalories(WorkoutDto workoutDto) {
        String newCalories = getUserInput(color.yellowBackground("Введите новое количество потраченных " +
                                                                 "калорий (например: 942.4):"));
        workoutService.updateCalories(workoutDto, newCalories);
        outputData.output(color.greenBackground("Количество потраченных калорий успешно изменено!"));
    }

    /**
     * Edits the additional information of a workout.
     */
    private void editAdditionalInfo(WorkoutDto workoutDto) {
        String title = getUserInput(color.yellowBackground("Введите заголовок дополнительной информации " +
                                                           "(например: пройденное расстояние):"));
        String info = getUserInput(color.yellowBackground("Введите новые данные для этого заголовка " +
                                                          "(например: 11,6 км):"));
        workoutService.updateAdditionalInfo(workoutDto, title, info);
        outputData.output(color.greenBackground("Дополнительная информация успешно изменена!"));
    }

    /**
     * Gets user input.
     *
     * @param message The message to display to the user
     * @return The user input
     */
    private String getUserInput(String message) {
        outputData.output(message);
        return inputData.input().toString();
    }

    /**
     * Handles exceptions.
     *
     * @param e The exception
     */
    private void handleException(Exception e) {
        outputData.errOutput(e.getMessage());
    }

}

