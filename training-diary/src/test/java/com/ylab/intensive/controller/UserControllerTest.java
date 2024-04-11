package com.ylab.intensive.controller;


import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Mock
    private InputData inputData;

    @Mock
    private OutputData outputData;

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Test user registration")
    void testRegistration() {
        when(inputData.input()).thenReturn("testEmail", "testPassword", "USER");

        userController.registration();

        verify(userManagementService).registerUser("testEmail", "testPassword", "USER");
    }

    @Test
    @DisplayName("Test user login - success")
    void testLogin_Success() {
        when(inputData.input()).thenReturn("testEmail", "testPassword");
        when(userManagementService.login("testEmail", "testPassword")).thenReturn(Optional.of(new UserDto("testEmail", Role.USER)));

        userController.login();

        verify(outputData).output("Пользователь успешно авторизовался: testEmail USER");
    }

    @Test
    @DisplayName("Test user login - failure")
    void testLogin_Failure() {
        when(inputData.input()).thenReturn("testEmail", "testPassword");
        when(userManagementService.login("testEmail", "testPassword")).thenReturn(Optional.empty());

        userController.login();

        verify(outputData).errOutput("Не правильный логин или пароль!");
    }

    @Test
    @DisplayName("Test change user permissions - success")
    void testChangeUserPermissions_Success() {
        when(inputData.input()).thenReturn("testEmail", "ADMIN");
        when(userManagementService.changeUserPermissions("testEmail", "ADMIN")).thenReturn(Optional.of(new UserDto("testEmail", Role.ADMIN)));

        userController.changeUserPermissions();

        verify(outputData).output("Роль пользователя была успешно изменена на: ADMIN");
    }

    @Test
    @DisplayName("Test change user permissions - failure")
    void testChangeUserPermissions_Failure() {
        when(inputData.input()).thenReturn("testEmail", "ADMIN");
        when(userManagementService.changeUserPermissions("testEmail", "ADMIN")).thenReturn(Optional.empty());

        userController.changeUserPermissions();

        verify(outputData).errOutput("Не удалось изменить права пользователя! Это действие может выполнить админ.");
    }

    @Test
    @DisplayName("Test show audit log")
    void testShowAuditLog() {
        when(userManagementService.getAudit()).thenReturn(List.of("Audit log"));

        userController.showAuditLog();

        verify(outputData).output(List.of("Audit log"));
    }

    @Test
    @DisplayName("Test user logout")
    void testLogout() {
        userController.logout();

        verify(outputData).output("Вы успешно разлогинились!");
        verify(userManagementService).logout();
    }
}
