/*
package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.UserDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.RegisterException;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import com.ylab.intensive.mapper.UserMapper;
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

import java.util.Optional;

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
        String roleStr = "USER";
        Role role = Role.USER;

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        when(roleService.getIdByName(role)).thenReturn(1);
        when(userDao.save(any(User.class), anyInt())).thenReturn(true);

        boolean result = userManagementService.registerUser(email, password, roleStr);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Register user - user already exists")
    void testRegisterUser_UserExists() {
        String email = "test@email.com";
        String password = "password";
        String roleStr = "USER";

        when(userDao.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userManagementService.registerUser(email, password, roleStr))
                .isInstanceOf(RegisterException.class)
                .hasMessage("Такой пользователь уже существует!");
    }

    @Test
    @DisplayName("Change user permissions - unauthorized")
    void testChangeUserPermissions_Unauthorized() {
        String email = "test@email.com";
        String roleStr = "USER";

        UserDto unauthorizedUser = new UserDto();
        unauthorizedUser.setRole(Role.USER);
        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(unauthorizedUser);

        assertThat(userManagementService.changeUserPermissions(email, roleStr)).isEmpty();
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
        String email = "test@email.com";
        String password = "password";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.USER);

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.entityToDto(user)).thenReturn(userDto);
        doNothing().when(auditService).saveAction(1, "Action 1");
        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);

        Optional<User> result = userManagementService.login(email, password);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Login - user not found")
    void testLogin_UserNotFound() {
        String email = "test@email.com";
        String password = "password";

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userManagementService.login(email, password))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Пользователь с email = " + email + " не существует!");
    }

    @Test
    @DisplayName("Login - wrong password")
    void testLogin_WrongPassword() {
        String email = "test@email.com";
        String password = "wrongpassword";
        User user = new User();
        user.setPassword("password");

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        assertThat(userManagementService.login(email, password)).isEmpty();
    }

    @Test
    @DisplayName("Logout")
    void testLogout() {
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        userManagementService.logout();

        verify(auditService).saveAction(anyInt(), eq("Пользователь разлогинился"));
        verify(authorizedUser).clearSession();
    }

    @Test
    @DisplayName("Change user permissions - success")
    void testChangeUserPermissions_Success() {
        String email = "test@email.com";
        String roleStr = "ADMIN";
        Role role = Role.ADMIN;
        UserDto authorizedUserDto = new UserDto();
        authorizedUserDto.setRole(Role.ADMIN);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(authorizedUserDto);
        when(roleService.getIdByName(role)).thenReturn(1);
        when(userDao.updateUserRole(email, 1)).thenReturn(true);
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(auditService).saveAction(1, "Action 1");
        doNothing().when(authorizedUser).removeAttribute("authorizedUser");
        doNothing().when(authorizedUser).setAttribute("authorizedUser", authorizedUserDto);
        when(userMapper.entityToDto(user)).thenReturn(authorizedUserDto);

        Optional<UserDto> result = userManagementService.changeUserPermissions(email, roleStr);

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("Get all users - unauthorized")
    void testGetAllUser_Unauthorized() {
        String email = "test@email.com";
        UserDto unauthorizedUserDto = new UserDto();
        unauthorizedUserDto.setRole(Role.USER);
        unauthorizedUserDto.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(unauthorizedUserDto);
        when(userDao.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThat(userManagementService.getAllUser()).isEmpty();
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
*/
