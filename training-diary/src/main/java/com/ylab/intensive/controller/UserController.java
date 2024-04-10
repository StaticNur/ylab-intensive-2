package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.dto.UserDto;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.service.UserManagementService;

import java.util.Optional;

public class UserController {
    @Inject
    private InputData inputData;
    @Inject
    private OutputData outputData;
    @Inject
    private UserManagementService userManagementService;

    public void registration() {
        userOperation("Регистрация", () -> {
            String email = readInput("логин: ");
            String password = readInput("пароль: ");
            String role = readInput("Role (ADMIN / USER): ");
            userManagementService.registerUser(email, password, role);
        });
    }

    public void login() {
        userOperation("Авторизоваться", () -> {
            String email = readInput("логин: ");
            String password = readInput("пароль: ");
            Optional<UserDto> userDto = userManagementService.login(email, password);
            if (userDto.isEmpty()) {
                outputData.errOutput("Не правильный логин или пароль!");
            } else {
                userManagementService.saveAction("Пользователь " + userDto.get().getEmail() + " авторизовался");
                outputData.output("Пользователь успешно авторизовался: " + userDto.get().getEmail() + " " + userDto.get().getRole());
            }
        });
    }

    public void changeUserPermissions() {
        userOperation("Изменение разрешений пользователя", () -> {
            String email = readInput("логин: ");
            String role = readInput("роль: ");
            Optional<UserDto> userDto = userManagementService.changeUserPermissions(email, role);
            if (userDto.isEmpty()) {
                outputData.errOutput("Не удалось изменить права пользователя!");
            }
            userManagementService.saveAction("Пользователь изменил роль на: " + userDto.get().getRole());
            outputData.output("Роль пользователя была успешно изменена на: " + userDto.get().getRole());
        });
    }

    public void showAuditLog() {
        outputData.output(userManagementService.getAudit());
    }

    public void logout() {
        outputData.output("Вы успешно разлогинились!");
        userManagementService.saveAction("Пользователь разлогинился");
        userManagementService.logout();
    }

    private void userOperation(String operation, Runnable action) {
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
        return inputData.input().toString().trim();
    }

    private boolean repeatOperation() {
        outputData.output("Повторить? 1-да, 2-нет");
        return inputData.input().toString().equals("1");
    }
}
