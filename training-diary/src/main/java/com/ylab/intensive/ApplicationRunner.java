package com.ylab.intensive;

import com.ylab.intensive.controller.TrainingController;
import com.ylab.intensive.controller.UserController;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.model.security.Session;

import static com.ylab.intensive.ui.ConsoleText.*;

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

    private boolean processUnauthenticatedUser() {
        outputData.output(MENU_NOT_AUTH);
        Endpoints selectedOption = getInputAndMapToEndpoint();

        switch (selectedOption) {
            case REGISTRATION -> userController.registration();
            case LOGIN -> userController.login();
            case EXIT -> {
                outputData.output("Завершить приложения");
                return false;
            }
            default -> outputData.errOutput(UNKNOWN_COMMAND);
        }

        return true;
    }

    private boolean processAuthenticatedUser() {
        outputData.output(AUTHORIZED_USER, session.getAttribute("authorizedUser"));
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
                //return false;
            }
            default -> outputData.errOutput(UNKNOWN_COMMAND);
        }

        return true;
    }

    private Endpoints getInputAndMapToEndpoint() {
        String action = inputData.input().toString();
        return Endpoints.fromValue(action);
    }
}
