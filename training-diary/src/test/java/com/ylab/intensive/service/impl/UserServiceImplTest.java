package com.ylab.intensive.service.impl;

import com.ylab.intensive.exception.AuthorizeException;
import com.ylab.intensive.exception.DaoException;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.RegistrationException;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.service.AuditService;
import com.ylab.intensive.service.RoleService;
import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.service.security.impl.JwtUserDetailsService;
import com.ylab.intensive.util.converter.Converter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    private JwtUserDetailsService jwtUserDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Converter converter;

    @InjectMocks
    private UserServiceImpl userService;

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
        when(passwordEncoder.encode(any())).thenReturn(password);
        when(userDao.save(any(User.class), anyInt())).thenReturn(user);

        User result = userService.registerUser(new RegistrationDto(email, password, role));

        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("Register user - user already exists")
    void testRegisterUser_UserExists() {
        String email = "test@email.com";
        String password = "password";
        Role role = Role.USER;

        when(userDao.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(new RegistrationDto(email, password, role)))
                .isInstanceOf(RegistrationException.class)
                .hasMessage("Такой пользователь уже существует!");
    }

    @Test
    @DisplayName("Change user permissions - Failed")
    void testChangeUserPermissions_Failed() {
        String uuid = "123e4567-e89b-12d3-a456-428613178104";
        Role role = Role.USER;

        when(roleService.getIdByName(role)).thenReturn(1);
        when(converter.convert(uuid, UUID::fromString, "Invalid UUID")).thenReturn(UUID.randomUUID());
        when(userDao.updateUserRole(UUID.fromString(uuid), 1)).thenReturn(false);

        assertThatThrownBy(() -> userService.changeUserPermissions(uuid, new ChangeUserRightsDto(role)))
                .isInstanceOf(DaoException.class)
                .hasMessage("Failed to change user role. Invalid uuid");
    }

    @Test
    @DisplayName("Find by email - user not found")
    void testFindByEmail_UserNotFound() {
        String email = "test@email.com";

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        assertThat(userService.findByEmail(email)).isEmpty();
    }

    @Test
    @DisplayName("Login - success")
    void testLogin_Success() {
        String email = "key";
        String password = "password";
        LoginDto loginDto = new LoginDto(email, password);
        var userDetails = new org.springframework.security.core.userdetails.User(email, password, Collections.emptyList());

        when(jwtUserDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenService.createAccessToken(any())).thenReturn("key");
        when(jwtTokenService.createRefreshToken(any())).thenReturn("key");

        JwtResponse result = userService.login(loginDto);

        assertThat(result.login()).isEqualTo(email);
        assertThat(result.accessToken()).isEqualTo("key");
    }

    @Test
    @DisplayName("Login - wrong password")
    void testLogin_WrongPassword() {
        String email = "test@email.com";
        String password = "password";
        var userDetails = new org.springframework.security.core.userdetails.User(email, password, Collections.emptyList());


        when(jwtUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(new LoginDto(email, password)))
                .isInstanceOf(AuthorizeException.class)
                .hasMessage("The password for this email is incorrect.");
    }

    @Test
    @DisplayName("Change user permissions - success")
    void testChangeUserPermissions_Success() {
        String uuidStr = "123e4567-e89b-12d3-a456-426614174002";
        ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.USER);

        UUID uuid = UUID.fromString(uuidStr);
        int roleId = 2;
        User user = new User();
        user.setRole(Role.USER);

        when(roleService.getIdByName(Role.USER)).thenReturn(roleId);
        when(converter.convert(anyString(), any(), anyString())).thenReturn(uuid);
        when(userDao.updateUserRole(uuid, roleId)).thenReturn(true);
        when(userDao.findByUUID(uuid)).thenReturn(Optional.of(user));

        User updatedUser = userService.changeUserPermissions(uuidStr, changeUserRightsDto);

        assertThat(updatedUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Change User Permissions - failed")
    void testChangeUserPermissions_UserNotFound() {
        String uuidStr = "123e4567-e89b-12d3-a456-426614174002";
        ChangeUserRightsDto changeUserRightsDto = new ChangeUserRightsDto(Role.USER);

        UUID uuid = UUID.fromString(uuidStr);
        int roleId = 2;

        when(roleService.getIdByName(Role.USER)).thenReturn(roleId);
        when(converter.convert(anyString(), any(), anyString())).thenReturn(uuid);
        when(userDao.updateUserRole(uuid, roleId)).thenReturn(true);
        when(userDao.findByUUID(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeUserPermissions(uuidStr, changeUserRightsDto))
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

        Optional<User> result = userService.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Find by email")
    void testUpdateToken() {
        when(jwtTokenService.refreshUserToken(anyString()))
                .thenReturn(new JwtResponse("login", "token", "token"));

        assertDoesNotThrow(() -> userService.updateToken("token"));
    }
}
