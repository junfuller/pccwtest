package org.example.pccwtest.security.service;

import lombok.AllArgsConstructor;
import org.example.pccwtest.model.LoginUser;
import org.example.pccwtest.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link UserDetailsService}.
 * <p>
 * This service is responsible for loading user-specific data during the authentication process.
 * It retrieves user details from the {@link UserRepository} and wraps them in a {@link LoginUser}
 * object, which implements {@link UserDetails}.
 * </p>
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user-specific data based on the username.
     * <p>
     * If the user is not found in the repository, a {@link UsernameNotFoundException} is thrown.
     * </p>
     *
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} object containing user information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new LoginUser(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}
