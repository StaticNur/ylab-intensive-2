package com.ylab.intensive;

import com.ylab.intensive.controller.TrainingController;
import com.ylab.intensive.controller.UserController;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.model.security.Session;

import static com.ylab.intensive.ui.ConsoleText.*;

/**
 * It handles user interaction and controls the flow of the program.
 */
public class ApplicationRunner {
    @Inject
    private InputData inputData;
    @Inject
    private OutputData outputData;
    @Inject
    private TrainingController trainingController;
    @Inject
    private UserController userController;
    @Inject
    private Session session;

    /**
     * Runs the main loop.
     */
    public void run() {
        outputData.output(WELCOME);
        boolean processIsRun = true;

        while (processIsRun) {
            if (session.getAttribute("authorizedUser") == null) {
                processIsRun = processUnauthenticatedUser();
            } else {
                processIsRun = processAuthenticatedUser();
            }
        }

        inputData.closeInput();
    }

    /**
     * Handles the actions for an unauthenticated user.
     * Displays the menu options and performs the selected action.
     *
     * @return true if the process should continue, false if the application should exit
     */
    private boolean processUnauthenticatedUser() {
        outputData.output(MENU_NOT_AUTH);
        Endpoints selectedOption = getInputAndMapToEndpoint();

        switch (selectedOption) {
            case REGISTRATION -> userController.registration();
            case LOGIN -> userController.login();
            case EXIT -> {
                outputData.output("Приложение успешно завершилось. \n" +
                                  "Благодарим вас за использование нашего консольного приложения. \n" +
                                  "Если у вас есть какие-либо вопросы, не стесняйтесь обращаться к нам https://t.me/orifov_nur. \n" +
                                  "Удачи!");
                return false;
            }
            default -> outputData.errOutput(UNKNOWN_COMMAND);
        }

        return true;
    }

    /**
     * Handles the actions for an authenticated user.
     * Displays the menu options and performs the selected action.
     *
     * @return true if the process should continue, false if the application should exit
     */
    private boolean processAuthenticatedUser() {
        UserDto user = (UserDto) session.getAttribute("authorizedUser");
        outputData.output(AUTHORIZED_USER, user.getEmail() + " " + user.getRole());
        outputData.output(MENU_AUTH);
        Endpoints selectedOption = getInputAndMapToEndpoint();

        switch (selectedOption) {
            case ADD_TYPE_WORKOUT -> trainingController.addTrainingType();
            case ADD_TRAINING -> trainingController.addWorkout();
            case ADD_ADDITIONAL_INFORMATION -> trainingController.addWorkoutInfo();
            case VIEW_TRAININGS -> trainingController.showWorkoutHistory();
            case EDIT_TRAINING -> trainingController.editWorkout();
            case DELETE_TRAINING -> trainingController.deleteWorkout();
            case CHANGE_USER_RIGHTS -> userController.changeUserPermissions();
            case VIEW_STATISTICS -> trainingController.showWorkoutStatistics();
            case AUDIT -> userController.showAuditLog();
            case EXIT -> {
                userController.logout();
            }
            default -> outputData.errOutput(UNKNOWN_COMMAND);
        }

        return true;
    }

    /**
     * Reads user input and maps it to an Endpoint enum value.
     *
     * @return The Endpoint enum value representing the user's input
     */
    private Endpoints getInputAndMapToEndpoint() {
        String action = inputData.input().toString();
        return Endpoints.fromValue(action);
    }
}
