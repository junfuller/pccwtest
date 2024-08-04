package org.example.pccwtest.service;

import java.time.LocalDateTime;
import java.util.List;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.example.pccwtest.dto.UserDTO;
import org.example.pccwtest.model.LoginUser;
import org.example.pccwtest.model.User;
import org.example.pccwtest.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling user-related operations.
 * <p>
 * This service includes methods for user registration, login, retrieving users from the cache or repository,
 * and sending welcome emails.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenService userTokenService;
    private final AuthenticationManager authenticationManager;
    private final Cache<String, LoginUser> tokenCache;

    /**
     * Registers a new user.
     * <p>
     * Checks if the provided email or username already exists. If not, creates a new user with the provided
     * details, saves it to the repository, and sends a welcome email.
     * </p>
     *
     * @param userDTO the details of the user to be registered
     * @return the newly created {@link User}
     * @throws IllegalArgumentException if the email or username is already in use
     */
    public User registerUser(UserDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exist");
        }

        final User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setIsActive(true);
        User savedUser = userRepository.save(user);

        sendWelcomeEmail(savedUser);

        return savedUser;
    }

    /**
     * Authenticates a user and returns a JWT token.
     * <p>
     * Attempts to authenticate the user with the provided credentials. If authentication is successful, generates
     * a JWT token, updates the login time, stores the token in the cache, and returns the token.
     * </p>
     *
     * @param userDTO the credentials of the user trying to log in
     * @return a {@link ResponseEntity} containing the JWT token if authentication is successful, or an error message
     */
    public ResponseEntity<String> loginUser(UserDTO userDTO) {
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken( userDTO.getUsername(), userDTO.getPassword() );
            // Execute CustomUserDetailsService.loadUserByUsername
            authentication = authenticationManager.authenticate( authenticationToken );
        } catch ( BadCredentialsException e ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }catch ( Exception e ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        LoginUser loginUser = ( LoginUser ) authentication.getPrincipal();
        final String token = userTokenService.generateToken(loginUser.getUser().getUsername());
        loginUser.setLoginTime(LocalDateTime.now());
        tokenCache.put(token, loginUser);
        return ResponseEntity.ok(token);
    }

    /**
     * Retrieves a list of all users currently logged in (from the cache).
     *
     * @return a list of {@link LoginUser} objects representing logged-in users
     */
    public List<LoginUser> listAllLoggedInUsers() {
        return tokenCache.asMap().values().stream().toList();
    }

    /**
     * Retrieves a list of all users from the repository.
     *
     * @return a list of {@link User} objects
     */
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    /**
     * Sends a welcome email to the newly registered user.
     *
     * @param user the {@link User} to whom the welcome email will be sent
     */
    private void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome");
        message.setText("Welcome to our company, " + user.getUsername());
        mailSender.send(message);
    }
}