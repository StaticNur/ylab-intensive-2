package com.ylab.intensive.controller;

import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.service.UserManagementService;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.ui.AnsiColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
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

    @Mock
    private WorkoutService workoutService;

    @Mock
    private AnsiColor color;

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
        User user = new User();
        user.setEmail("testEmail");
        user.setRole(Role.USER);
        when(inputData.input()).thenReturn("testEmail", "testPassword");
        when(userManagementService.login("testEmail", "testPassword")).thenReturn(Optional.of(user));
        when(color.yellowText(anyString())).thenReturn("text");
        when(color.yellowBackground(anyString())).thenReturn("text");
        when(color.greenBackground(anyString())).thenReturn("Пользователь успешно авторизовался: testEmail USER");

        userController.login();

        verify(outputData).output("Пользователь успешно авторизовался: testEmail USER");
    }

    @Test
    @DisplayName("Test user login - failure")
    void testLogin_Failure() {
        when(inputData.input()).thenReturn("testEmail", "testPassword");
        when(userManagementService.login("testEmail", "testPassword")).thenReturn(Optional.empty());
        when(color.yellowText(anyString())).thenReturn("text");
        when(color.yellowBackground(anyString())).thenReturn("text");
        when(color.greenBackground(anyString())).thenReturn("text");

        userController.login();

        verify(outputData).errOutput(" Не правильный логин или пароль!");
    }

    @Test
    @DisplayName("Test change user permissions - success")
    void testChangeUserPermissions_Success() {
        when(inputData.input()).thenReturn("testEmail", "ADMIN");
        when(userManagementService.changeUserPermissions("testEmail", "ADMIN")).thenReturn(Optional.of(new UserDto("testEmail", Role.ADMIN)));
        when(color.yellowText(anyString())).thenReturn("text");
        when(color.yellowBackground(anyString())).thenReturn("text");
        when(color.greenBackground(anyString())).thenReturn("Роль пользователя была успешно изменена на: ADMIN");

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
        when(color.greenBackground(anyString())).thenReturn("Audit log");

        userController.showAuditLog();

        verify(outputData).output("Audit log");
    }

    @Test
    @DisplayName("Test user logout")
    void testLogout() {
        when(color.greenBackground(anyString())).thenReturn(" Вы успешно разлогинились!");

        userController.logout();

        verify(outputData).output(" Вы успешно разлогинились!");
        verify(userManagementService).logout();
    }

    @Test
    @DisplayName("Show all user workouts - admin user")
    void testShowAllUserWorkouts_AdminUser() {
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ADMIN);

        List<User> userList = new ArrayList<>();
        userList.add(adminUser);
        userList.get(0).setWorkout(Collections.emptyList());

        when(userManagementService.getAllUser()).thenReturn(userList);
        when(workoutService.getAllUsersWorkouts(userList)).thenReturn(userList);
        when(color.greenBackground(anyString()))
                .thenReturn("Test")
                .thenReturn("Test");

        userController.showAllUserWorkouts();

        verify(outputData, times(2)).output(anyString());
    }

    @Test
    @DisplayName("Show all user workouts - regular user")
    void testShowAllUserWorkouts_RegularUser() {
        User regularUser = new User();
        regularUser.setEmail("regular@example.com");
        regularUser.setRole(Role.USER);

        when(userManagementService.getAllUser()).thenReturn(Collections.emptyList());

        userController.showAllUserWorkouts();

        verify(outputData).errOutput("Для этого запроса нужны права администратора!");
    }
}
