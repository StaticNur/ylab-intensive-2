package com.ylab.intensive.service.impl;


import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.model.User;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.WorkoutService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("User Management Service Tests")
class UserManagementServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private WorkoutService workoutService;

    @Mock
    private Session authorizedUser;

    @InjectMocks
    private UserManagementServiceImpl userManagementService;

    @Test
    @DisplayName("Register user - User does not exist")
    void testRegisterUser_UserDoesNotExist() {
        String email = "test@example.com";
        String password = "password";
        String roleStr = "USER";

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        when(userDao.getSize()).thenReturn(0);
        when(userDao.save(any())).thenReturn(true);

        assertTrue(userManagementService.registerUser(email, password, roleStr));
    }

    @Test
    @DisplayName("Register user - User already exists")
    void testRegisterUser_UserAlreadyExists() {
        String email = "test@example.com";
        String password = "password";
        String roleStr = "USER";

        when(userDao.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(RegisterException.class, () -> userManagementService.registerUser(email, password, roleStr));
    }

    @Test
    @DisplayName("Login - Successful")
    void testLogin_Successful() {
        String email = "test@example.com";
        String password = "password";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setWorkout(new ArrayList<>());

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        doNothing().when(userDao).saveAction(email, "Action 1");
        doNothing().when(workoutService).setAuthorizedWorkoutDB(user.getWorkout());

        Optional<UserDto> loggedInUser = userManagementService.login(email, password);

        assertTrue(loggedInUser.isPresent());
        assertEquals(email, loggedInUser.get().getEmail());
    }

    @Test
    @DisplayName("Login - Incorrect password")
    void testLogin_IncorrectPassword() {
        String email = "test@example.com";
        String password = "password";
        String incorrectPassword = "incorrect";

        User user = new User();
        user.setPassword(password);

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserDto> loggedInUser = userManagementService.login(email, incorrectPassword);

        assertFalse(loggedInUser.isPresent());
    }

    @Test
    @DisplayName("Logout")
    void testLogout() {
        String email = "test@example.com";
        String password = "password";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        doNothing().when(userDao).saveAction(email, "Action 1");

        userManagementService.login(email, password);
        userManagementService.logout();

        verify(authorizedUser).clearSession();
    }

    @Test
    @DisplayName("Change user permissions - Admin success")
    void testChangeUserPermissions_AdminSuccess() {
        String email = "test@example.com";
        String roleStr = "ADMIN";

        UserDto adminUserDto = new UserDto();
        adminUserDto.setRole(Role.ADMIN);
        User user = new User();
        user.setEmail(email);
        user.setRole(Role.ADMIN);
        user.setWorkout(new ArrayList<>());

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(adminUserDto);
        when(userDao.updateUserRole(email, Role.ADMIN)).thenReturn(Optional.of(user));

        Optional<UserDto> updatedUser = userManagementService.changeUserPermissions(email, roleStr);

        assertTrue(updatedUser.isPresent());
        assertEquals(Role.ADMIN, updatedUser.get().getRole());
    }

    @Test
    @DisplayName("Change user permissions - Admin failure")
    void testChangeUserPermissions_AdminFailure() {
        String email = "test@example.com";
        String roleStr = "USER";

        UserDto nonAdminUserDto = new UserDto();
        nonAdminUserDto.setRole(Role.USER);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(nonAdminUserDto);

        Optional<UserDto> updatedUser = userManagementService.changeUserPermissions(email, roleStr);

        assertFalse(updatedUser.isPresent());
    }

    @Test
    @DisplayName("Get audit log for user")
    void testGetAudit() {
        String email = "test@example.com";
        List<String> actions = new ArrayList<>();
        actions.add("Action 1");
        actions.add("Action 2");

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        User user = new User();
        user.setAction(actions);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        List<String> audit = userManagementService.getAudit();

        assertEquals(actions, audit);
    }

    @Test
    @DisplayName("Save action")
    void testSaveAction() {
        String email = "test@example.com";
        String action = "Test action";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);

        userManagementService.saveAction(action);

        verify(userDao).saveAction(email, action);
    }
}
