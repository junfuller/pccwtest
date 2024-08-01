package org.example.pccwtest.controller;

import org.example.pccwtest.dto.UserDTO;
import org.example.pccwtest.model.User;
import org.example.pccwtest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        UserDTO userDTO = new UserDTO("testuser", "test@example.com", "password123");
        User user = new User(1L, "testuser", "test@example.com", "password123", true);

        when(userService.registerUser(any(UserDTO.class))).thenReturn(user);

        ResponseEntity<User> response = userController.register(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("test@example.com", response.getBody().getEmail());
    }

}

