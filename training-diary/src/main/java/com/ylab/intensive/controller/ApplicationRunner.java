package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.ui.AnsiColor;

import java.util.Optional;

import static com.ylab.intensive.ui.ConsoleText.*;

/**
 * It handles user interaction and controls the flow of the program.
 */
public class ApplicationRunner {
    /**
     * ANSI Color.
     * This class provides ANSI color codes for console output.
     */
    @Inject
    private AnsiColor ansiColor;

    /**
     * Input Data.
     * This class allows access to the input data provided by the user.
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
     * Training Controller.
     * This controller is responsible for managing training-related operations.
     */
    @Inject
    private TrainingController trainingController;

    /**
     * User Controller.
     * This controller allows management of system users.
     */
    @Inject
    private UserController userController;

    /**
     * Session.
     * This class represents the current session or state of the user.
     */
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
        Optional<Endpoints> selectedOption = getInputAndMapToEndpoint();
        if (selectedOption.isEmpty()) {
            outputData.errOutput(ansiColor.redText(UNKNOWN_COMMAND));
        } else {
            switch (selectedOption.get()) {
                case REGISTRATION -> userController.registration();
                case LOGIN -> userController.login();
                case EXIT -> {
                    outputData.output(ansiColor.greenText(GOODBYE));
                    return false;
                }
                default -> outputData.errOutput(ansiColor.redText(UNKNOWN_COMMAND));
            }
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
        Optional<Endpoints> selectedOption = getInputAndMapToEndpoint();

        if (selectedOption.isEmpty()) {
            outputData.errOutput(ansiColor.redText(UNKNOWN_COMMAND));
        } else {
            switch (selectedOption.get()) {
                case ADD_TYPE_WORKOUT -> trainingController.addTrainingType();
                case ADD_TRAINING -> trainingController.addWorkout();
                case ADD_ADDITIONAL_INFORMATION -> trainingController.addWorkoutInfo();
                case VIEW_TRAININGS -> trainingController.showWorkoutHistory();
                case EDIT_TRAINING -> trainingController.editWorkout();
                case DELETE_TRAINING -> trainingController.deleteWorkout();
                case CHANGE_USER_RIGHTS -> userController.changeUserPermissions();
                case VIEW_STATISTICS -> trainingController.showWorkoutStatistics();
                case VIEW_All_TRAININGS -> userController.showAllUserWorkouts();
                case AUDIT -> userController.showAuditLog();
                case EXIT -> userController.logout();
                default -> outputData.errOutput(ansiColor.redText(UNKNOWN_COMMAND));
            }
        }
        return true;
    }

    /**
     * Reads user input and maps it to an Endpoint enum value.
     *
     * @return The Endpoint enum value representing the user's input
     */
    private Optional<Endpoints> getInputAndMapToEndpoint() {
        String action = inputData.input().toString();
        try {
            return Optional.of(Endpoints.fromValue(action));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
