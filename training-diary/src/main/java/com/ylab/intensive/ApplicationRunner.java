package com.ylab.intensive;

import com.ylab.intensive.controller.TrainingController;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.enums.Endpoints;
import com.ylab.intensive.security.Session;

import static com.ylab.intensive.ui.ConsoleText.*;

public class ApplicationRunner {
    @Inject
    private InputData inputData;
    @Inject
    private OutputData outputData;
    @Inject
    private TrainingController trainingController;
    @Inject
    private Session session;
    public void  run() {
        outputData.output(WELCOME);
        Endpoints selectedOption;
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output(AUTHORIZED_USER, session.getAttribute("authorizedUser"));

            outputData.output(MENU);
            String action = inputData.input().toString();
            selectedOption = Endpoints.fromValue(action);

            switch (selectedOption) {
                case REGISTRATION -> trainingController.registration();
                case LOGIN -> trainingController.login();
                case ADD_TYPE_WORKOUT -> trainingController.addTrainingType();
                case ADD_TRAINING -> trainingController.createWorkout();
                case ADD_ADDITIONAL_INFORMATION -> trainingController.addWorkoutInfo();
                case VIEW_TRAININGS -> trainingController.showWorkoutHistory();
                case EDIT_TRAINING -> trainingController.editWorkout();
                case DELETE_TRAINING -> trainingController.deleteWorkout();
                case CHANGE_USER_RIGHTS -> trainingController.changeUserPermissions();
                case VIEW_STATISTICS -> trainingController.showWorkoutStatistics();
                case AUDIT -> trainingController.showAuditLog();
                case EXIT -> {
                    trainingController.logout();
                    processIsRun = false;
                }
            }
        }
        inputData.closeInput();
    }

}
