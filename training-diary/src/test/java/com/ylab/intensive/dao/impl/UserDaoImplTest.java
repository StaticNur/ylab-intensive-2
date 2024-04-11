package com.ylab.intensive.dao.impl;

import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("User Database Operations Testing")
class UserDaoImplTest {

    @InjectMocks
    private UserDaoImpl userDao;

    @Test
    @DisplayName("Save user")
    void testSave() {
        User user = new User(1, "test@example.com", "password",
                new ArrayList<>(), new ArrayList<>(), Role.USER);

        boolean result = userDao.save(user);

        assertTrue(result);
        assertEquals(1, userDao.getSize());
    }

    @Test
    @DisplayName("Find user by email - User exists")
    void testFindByEmail_UserExists() {
        String email = "test@example.com";
        User user = new User(1, email, "password", new ArrayList<>(), new ArrayList<>(), Role.USER);
        userDao.save(user);

        Optional<User> result = userDao.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    @DisplayName("Find user by email - User does not exist")
    void testFindByEmail_UserDoesNotExist() {
        String email = "nonexistent@example.com";

        Optional<User> result = userDao.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Update user role - User exists")
    void testUpdateUserRole_UserExists() {
        String email = "test@example.com";
        User user = new User(1, email, "password", new ArrayList<>(), new ArrayList<>(), Role.USER);
        userDao.save(user);
        Role newRole = Role.ADMIN;

        Optional<User> result = userDao.updateUserRole(email, newRole);

        assertTrue(result.isPresent());
        assertEquals(newRole, result.get().getRole());
    }

    @Test
    @DisplayName("Update user role - User does not exist")
    void testUpdateUserRole_UserDoesNotExist() {
        String email = "nonexistent@example.com";
        Role newRole = Role.ADMIN;

        Optional<User> result = userDao.updateUserRole(email, newRole);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Save user action - User exists")
    void testSaveAction_UserExists() {
        String email = "test@example.com";
        String action = "Action";
        User user = new User(1, email, "password", new ArrayList<>(), new ArrayList<>(), Role.USER);
        userDao.save(user);

        userDao.saveAction(email, action);

        assertEquals(action, userDao.findByEmail(email).get().getAction().get(0));
    }

    @Test
    @DisplayName("Save user action - User does not exist")
    void testSaveAction_UserDoesNotExist() {
        String email = "nonexistent@example.com";
        String action = "Action";

        assertDoesNotThrow(() -> userDao.saveAction(email, action));

        assertEquals(0, userDao.getSize());
    }
}
