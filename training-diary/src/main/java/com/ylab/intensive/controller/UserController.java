package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.User;
import com.ylab.intensive.service.UserManagementService;

public class UserController {
    @Inject
    private InputData inputData;
    @Inject
    private OutputData outputData;
    @Inject
    private UserManagementService userManagementService;

    public void registration() {
        UserOperation("Регистрация", () -> {
            String email = readInput("логин: ");
            String password = readInput("пароль: ");
            String role = readInput("Role (ADMIN / USER): ");
            userManagementService.registerUser(email, password, role);
        });
    }

    public void login() {
        UserOperation("Авторизоваться", () -> {
            String email = readInput("логин: ");
            String password = readInput("пароль: ");
            userManagementService.login(email, password);
        });
    }

    public void changeUserPermissions() {
        UserOperation("Изменение разрешений пользователя", () -> {
            String email = readInput("логин: ");
            String role = readInput("роль: ");
            User user = userManagementService.changeUserPermissions(email, role);
            outputData.output("Роль пользователя была успешно изменена на:: " + user.getRole());
        });
    }

    public void showAuditLog() {
        outputData.output(userManagementService.getAudit());
    }

    public void logout() {
        userManagementService.logout();
    }

    private void UserOperation(String operation, Runnable action) {
        boolean processIsRun = true;
        while (processIsRun) {
            outputData.output(operation);
            try {
                action.run();
                processIsRun = false;
            } catch (RegisterException e) {
                outputData.errOutput(e);
                processIsRun = repeatOperation();
            }
        }
    }

    private String readInput(String prompt) {
        outputData.output(prompt);
        return inputData.input().toString();
    }

    private boolean repeatOperation() {
        outputData.output("Повторить? 1-да, 2-нет");
        return inputData.input().toString().equals("1");
    }
}
