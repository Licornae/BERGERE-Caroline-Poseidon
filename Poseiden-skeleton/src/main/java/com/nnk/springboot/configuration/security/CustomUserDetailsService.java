package com.nnk.springboot.configuration.security;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for loading user-specific data.
 * This class implements the {@link UserDetailsService} interface, which is
 * a core component of Spring Security for retrieving user-related data
 * by username during authentication.
 * The class interacts with the {@link UserRepository} to fetch User entities
 * from the database and convert them into {@link UserDetails} objects used
 * by Spring Security.
 * It supports the authentication process by providing user credentials
 * and roles to Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details by the provided username.
     * This method retrieves the user from the database using the UserRepository
     * and converts it into a Spring Security UserDetails object.
     *
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} object for the given username
     * @throws UsernameNotFoundException if no user is found with the provided username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
