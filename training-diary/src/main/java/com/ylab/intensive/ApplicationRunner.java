package com.ylab.intensive;

import com.ylab.intensive.controller.TrainingController;
import com.ylab.intensive.controller.UserController;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.model.security.Session;

import java.util.Optional;

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

    private boolean processAuthenticatedUser() {
        UserDto user = (UserDto) session.getAttribute("authorizedUser");
        outputData.output(AUTHORIZED_USER, user.getEmail() + " "+ user.getRole());
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

    private Endpoints getInputAndMapToEndpoint() {
        String action = inputData.input().toString();
        return Endpoints.fromValue(action);
    }
}
