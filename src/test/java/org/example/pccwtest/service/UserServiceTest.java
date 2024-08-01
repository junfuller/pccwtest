package org.example.pccwtest.service;

import org.example.pccwtest.dto.UserDTO;
import org.example.pccwtest.model.User;
import org.example.pccwtest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private UserService userService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        UserDTO userDTO = new UserDTO("testuser", "test@example.com", "password123");
        User user = new User(1L, "testuser", "test@example.com", "password123", true);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(userDTO);

        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("test@example.com", registeredUser.getEmail());
    }

    @Test
    public void testRegisterEmailAlreadyInUse() {
        UserDTO userDTO = new UserDTO("testuser", "test@example.com", "password123");
        User existingUser = new User(1L, "existinguser", "test@example.com", "password123", true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userDTO);
        });

        assertEquals("Email is already in use", exception.getMessage());
    }

}