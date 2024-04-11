package com.ylab.intensive.controller;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.service.UserManagementService;

import java.util.Optional;

/**
 * The UserController class handles user-related operations such as registration,
 * login, permissions management, audit logging, and logout.
 */
public class UserController {
    @Inject
    private InputData inputData;
    @Inject
    private OutputData outputData;
    @Inject
    private UserManagementService userManagementService;

    /**
     * Registers a new user.
     */
    public void registration() {
        userOperation("Регистрация", () -> {
            String email = readInput("логин: ");
            String password = readInput("пароль: ");
            String role = readInput("Role (ADMIN / USER): ");
            userManagementService.registerUser(email, password, role);
        });
    }

    /**
     * Logs in a user.
     */
    public void login() {
        userOperation("Авторизоваться", () -> {
            String email = readInput("логин: ");
            String password = readInput("пароль: ");
            Optional<UserDto> userDto = userManagementService.login(email, password);
            if (userDto.isEmpty()) {
                outputData.errOutput("Не правильный логин или пароль!");
            } else {
                outputData.output("Пользователь успешно авторизовался: " + userDto.get().getEmail() + " " + userDto.get().getRole());
            }
        });
    }

    /**
     * Changes user permissions.
     */
    public void changeUserPermissions() {
        userOperation("Изменение разрешений пользователя", () -> {
            String email = readInput("логин: ");
            String role = readInput("роль: ");
            Optional<UserDto> userDto = userManagementService.changeUserPermissions(email, role);
            if (userDto.isEmpty()) {
                outputData.errOutput("Не удалось изменить права пользователя! Это действие может выполнить админ.");
            }
            outputData.output("Роль пользователя была успешно изменена на: " + userDto.get().getRole());
        });
    }

    /**
     * Shows the audit log.
     */
    public void showAuditLog() {
        outputData.output(userManagementService.getAudit());
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        outputData.output("Вы успешно разлогинились!");
        userManagementService.logout();
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
        outputData.output("Повторить? 1-да, 2-нет");
        return inputData.input().toString().trim().equals("1");
    }
}
