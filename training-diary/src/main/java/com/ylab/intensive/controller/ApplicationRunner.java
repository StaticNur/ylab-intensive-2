package com.ylab.intensive.controller;

import com.ylab.intensive.controller.TrainingController;
import com.ylab.intensive.controller.UserController;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.ui.AnsiColor;

import static com.ylab.intensive.ui.ConsoleText.*;

/**
 * It handles user interaction and controls the flow of the program.
 */
public class ApplicationRunner {
    @Inject
    private AnsiColor ansiColor;
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
        outputData.output(ansiColor.welcome(WELCOME));
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
        outputData.output(ansiColor.greyBackground(MENU_NOT_AUTH));
        Endpoints selectedOption = getInputAndMapToEndpoint();

        switch (selectedOption) {
            case REGISTRATION -> userController.registration();
            case LOGIN -> userController.login();
            case EXIT -> {
                outputData.output(ansiColor.greenText(GOODBYE));
                return false;
            }
            default -> outputData.errOutput(ansiColor.redText(UNKNOWN_COMMAND));
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
        String authUser = String.format(AUTHORIZED_USER, user.getEmail() + " " + user.getRole());
        outputData.output(ansiColor.blueBackground(authUser));
        outputData.output(ansiColor.greyBackground(MENU_AUTH));
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
            case EXIT -> userController.logout();
            default -> outputData.errOutput(ansiColor.redText(UNKNOWN_COMMAND));
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
