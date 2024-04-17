package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.service.UserManagementService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.ui.AnsiColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * The UserController class handles user-related operations such as registration,
 * login, permissions management, audit logging, and logout.
 */
public class UserController {
    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Input data.
     * Provides access to the data entered by the user.
     */
    @Inject
    private InputData inputData;

    /**
     * Output data.
     * Allows outputting data to the user.
     */
    @Inject
    private OutputData outputData;

    /**
     * User Management Service.
     * Provides functionality for working with users.
     */
    @Inject
    private UserManagementService userManagementService;

    /**
     * Workout Service.
     * Provides functionality for working with workouts.
     */
    @Inject
    private WorkoutService workoutService;

    /**
     * ANSI color.
     * Allows setting the color for console output.
     */
    @Inject
    private AnsiColor color;


    /**
     * Registers a new user.
     */
    public void registration() {
        userOperation(color.yellowText(" Регистрация "), () -> {
            String email = readInput(color.yellowBackground(" логин: "));
            String password = readInput(color.yellowBackground(" пароль: "));
            String role = readInput(color.yellowBackground(" роль (ADMIN / USER): "));
            userManagementService.registerUser(email, password, role);
            log.info("The player trying to register with login " + email + " and password " + password);
        });
    }

    /**
     * Logs in a user.
     */
    public void login() {
        userOperation(color.yellowText(" Авторизоваться "), () -> {
            String email = readInput(color.yellowBackground(" логин: "));
            String password = readInput(color.yellowBackground(" пароль: "));
            Optional<User> user = userManagementService.login(email, password);
            if (user.isEmpty()) {
                outputData.errOutput(" Не правильный логин или пароль!");
            } else {
                workoutService.setAuthorizedWorkoutDB(user.get().getWorkout());
                outputData.output(color.greenBackground(" Пользователь успешно авторизовался: " + user.get().getEmail() + " " + user.get().getRole()));
            }
        });
    }

    /**
     * Changes user permissions.
     */
    public void changeUserPermissions() {
        userOperation(color.yellowText("Изменение разрешений пользователя"), () -> {
            String email = readInput(color.yellowBackground("логин: "));
            String role = readInput(color.yellowBackground("роль (ADMIN / USER): "));
            Optional<UserDto> userDto = userManagementService.changeUserPermissions(email, role);
            if (userDto.isEmpty()) {
                outputData.errOutput("Не удалось изменить права пользователя! Это действие может выполнить админ.");
            }
            outputData.output(color.greenBackground("Роль пользователя была успешно изменена на: " + userDto.get().getRole()));
        });
    }

    /**
     * Shows the audit log.
     */
    public void showAuditLog() {
        List<String> listAudit = userManagementService.getAudit();
        StringBuilder auditsForView = new StringBuilder();
        for (String action : listAudit) {
            auditsForView.append(action).append("\n");
        }
        outputData.output(color.greenBackground(auditsForView.toString()));
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        outputData.output(color.greenBackground(" Вы успешно разлогинились!"));
        userManagementService.logout();
    }

    /**
     * Shows all user workouts.
     */
    public void showAllUserWorkouts() {
        List<User> userList = userManagementService.getAllUser();
        if (userList.isEmpty()) {
            outputData.errOutput("Для этого запроса нужны права администратора!");
        } else {
            for (User user : userList) {
                outputData.output(color.greenBackground(user.getEmail() + " " + user.getRole()));
                StringBuilder formattedWorkouts = new StringBuilder();

                for (Workout workout : user.getWorkout()) {
                    Duration duration = workout.getDuration();
                    String viewDuration = duration.toHours() + "ч. " + duration.toMinutesPart() + "м. " + duration.toSecondsPart() + "c. ";
                    formattedWorkouts.append("date = ").append(workout.getDate()).append(", ");
                    formattedWorkouts.append("types = ").append(workout.getType()).append(", ");
                    formattedWorkouts.append("duration = ").append(viewDuration).append(", ");
                    formattedWorkouts.append("calorie = ").append(workout.getCalorie()).append(", ");
                    formattedWorkouts.append("info = ").append(workout.getInfo()).append("\n");
                }

                if (user.getWorkout().isEmpty()) {
                    outputData.output(color.greenBackground("Тренировок нет"));
                } else {
                    outputData.output(color.greenBackground(formattedWorkouts.toString()));
                }
            }
        }
    }

    // Private helper methods

    /**
     * Executes a user operation.
     *
     * @param operation The operation to perform
     * @param action    The action to execute
     */
    private void userOperation(String operation, Runnable action) {
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output(operation);
            try {
                action.run();
                processIsRun = false;
            } catch (RuntimeException e) {
                outputData.errOutput(e.getMessage());
                processIsRun = repeatOperation();
            }
        }
    }

    /**
     * Reads user input.
     *
     * @param prompt The prompt to display to the user
     * @return The user input
     */
    private String readInput(String prompt) {
        outputData.output(prompt);
        return inputData.input().toString().trim();
    }

    /**
     * Asks the user if they want to repeat the operation.
     *
     * @return true if the operation should be repeated, false otherwise
     */
    private boolean repeatOperation() {
        outputData.output(color.greyBackground("Повторить? 1-да, 2-нет"));
        return inputData.input().toString().trim().equals("1");
    }
}
