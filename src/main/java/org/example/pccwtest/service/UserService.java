package org.example.pccwtest.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.example.pccwtest.dto.UserDTO;
import org.example.pccwtest.model.User;
import org.example.pccwtest.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public User registerUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        final User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setIsActive(true);
        User savedUser = userRepository.save(user);

        sendWelcomeEmail(savedUser);

        return savedUser;
    }

    private void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome");
        message.setText("Welcome to our company, " + user.getUsername());
        mailSender.send(message);
    }
}