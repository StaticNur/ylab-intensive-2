package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.exception.AuthorizeException;
import com.ylab.intensive.exception.ChangeUserPermissionsException;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.mapper.UserMapper;
import com.ylab.intensive.security.Authentication;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.service.AuditService;
import com.ylab.intensive.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("User Management Service Tests")
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuditService auditService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userManagementService;

    @Test
    @DisplayName("Register user - success")
    void testRegisterUser_Success() {
        String email = "test@email.com";
        String password = "password";
        Role role = Role.USER;
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setRole(role);

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        when(roleService.getIdByName(role)).thenReturn(1);
        when(userDao.save(any(User.class), anyInt())).thenReturn(user);

        User result = userManagementService.registerUser(new RegistrationDto(email, password, role));

        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("Register user - user already exists")
    void testRegisterUser_UserExists() {
        String email = "test@email.com";
        String password = "password";
        Role role = Role.USER;

        when(userDao.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userManagementService.registerUser(new RegistrationDto(email, password, role)))
                .isInstanceOf(RegisterException.class)
                .hasMessage("Такой пользователь уже существует!");
    }

    @Test
    @DisplayName("Change user permissions - Failed")
    void testChangeUserPermissions_Failed() {
        String uuid = "123e4567-e89b-12d3-a456-428613178104";
        Role role = Role.USER;

        when(roleService.getIdByName(role)).thenReturn(1);
        when(userDao.updateUserRole(UUID.fromString(""),1)).thenReturn(false);

        assertThatThrownBy(() -> userManagementService.changeUserPermissions(uuid, new ChangeUserRightsDto(role)))
                .isInstanceOf(ChangeUserPermissionsException.class)
                .hasMessage("Failed to change user role");
    }

    @Test
    @DisplayName("Find by email - user not found")
    void testFindByEmail_UserNotFound() {
        String email = "test@email.com";

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        assertThat(userManagementService.findByEmail(email)).isEmpty();
    }

    @Test
    @DisplayName("Login - success")
    void testLogin_Success() {
        String email = "key";
        String password = "password";
        LoginDto loginDto = new LoginDto(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.USER);

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtTokenService.createAccessToken(email, Role.USER)).thenReturn("key");
        when(jwtTokenService.createRefreshToken(email, Role.USER)).thenReturn("key");
        when(jwtTokenService.authentication("key")).thenReturn(new Authentication());

        JwtResponse result = userManagementService.login(loginDto);

        assertThat(result.login()).isEqualTo(email);
        assertThat(result.accessToken()).isEqualTo("key");
    }

    @Test
    @DisplayName("Login - user not found")
    void testLogin_UserNotFound() {
        String email = "test@email.com";
        String password = "password";

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userManagementService.login(new LoginDto(email, password)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("There is no user with this login in the database.");
    }

    @Test
    @DisplayName("Login - wrong password")
    void testLogin_WrongPassword() {
        String email = "test@email.com";
        String password = "password";
        User user = new User();
        user.setPassword(password);

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userManagementService.login(new LoginDto(email, "password787")))
                .isInstanceOf(AuthorizeException.class)
                .hasMessage("Incorrect password.");
    }

    @Test
    @DisplayName("Change user permissions - success")
    void testChangeUserPermissions_Success() {
        String uuidStr = "123e4567-e89b-12d3-a456-426614174000";
        ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.USER);

        UUID uuid = UUID.fromString(uuidStr);
        int roleId = 2;
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUuid(uuid);
        existingUser.setRole(Role.USER);

        when(roleService.getIdByName(Role.USER)).thenReturn(roleId);
        when(userDao.updateUserRole(uuid, roleId)).thenReturn(true);
        when(userDao.findByUUID(uuid)).thenReturn(Optional.of(existingUser));

        User updatedUser = userManagementService.changeUserPermissions(uuidStr, changeUserRightsDto);

        assertThat(updatedUser.getRole()).isEqualTo(Role.USER);
        verify(auditService, times(1)).saveAction(existingUser.getId(), "Пользователь изменил роль на: USER");
    }

    @Test
    @DisplayName("Get all users - unauthorized")
    void testChangeUserPermissions_UserNotFound() {
        String uuidStr = "123e4567-e89b-12d3-a456-426614174000";
        ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.ADMIN);

        UUID uuid = UUID.fromString(uuidStr);
        int roleId = 2;

        when(roleService.getIdByName(Role.ADMIN)).thenReturn(roleId);
        when(userDao.updateUserRole(uuid, roleId)).thenReturn(true);
        when(userDao.findByUUID(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userManagementService.changeUserPermissions(uuidStr, changeUserRightsDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с uuid = " + uuidStr + " не существует!");
    }

    @Test
    @DisplayName("Find by email")
    void testFindByEmail() {
        String email = "test@email.com";
        User user = new User();
        user.setEmail(email);

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userManagementService.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }
}
