package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService interface for managing operations related to the User entity.
 * Provides methods to perform CRUD (Create, Read, Update, Delete) operations using the UserRepository.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users from the data source.
     *
     * @return a list of all User entities.
     */
    @Override
    public List<User> findAll() {
        log.debug("Finding all User entities");
        List<User> users = userRepository.findAll();
        log.debug("Found {} User entities", users.size());
        return users;
    }

    /**
     * Finds a User by their unique identifier.
     *
     * @param id the unique identifier of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional if no User is found
     * @throws IllegalArgumentException if no User is found for the provided id
     */
    @Override
    public Optional<User> findById(Integer id) {

        log.debug("Finding User with ID: {}", id);

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            String errorMsg = "User not found for id: " + id;
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        log.debug("Found User: {}", user.get());
        return user;
    }

    /**
     * Saves a User entity in the data source.
     * Logs the save operation, delegates persistence to the UserRepository,
     * and returns the persisted User with its generated identifier.
     *
     * @param user the User entity to save
     * @return the saved User entity
     * @throws RuntimeException if an error occurs while saving the User
     */
    @Override
    public User save(User user) {

        log.debug("Saving User: {}", user);

        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            log.info("Successfully saved User with ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e){
            log.error("Error saving User: {}", user, e);
            throw new RuntimeException("Failed to save User: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing User entity with the provided details. The user's
     * username, fullname, and role are updated. If a new password is provided,
     * it is encoded and updated as well.
     *
     * @param id the unique identifier of the User to update
     * @param user a User object containing the updated details
     * @return the updated User entity
     * @throws IllegalArgumentException if no User is found for the provided id
     */
    @Override
    public User update(int id, User user) {
        User existingUser = findById(id).get();

        existingUser.setUsername(user.getUsername());
        existingUser.setFullname(user.getFullname());
        existingUser.setRole(user.getRole());

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    /**
     * Deletes a User entity from the data source by its unique identifier.
     * Checks that the User exists before deletion, logs the operation,
     * and raises an error if no User is found for the provided id.
     *
     * @param id the unique identifier of the User to delete
     * @throws IllegalArgumentException if no User is found for the provided id
     * @throws RuntimeException if an error occurs while deleting the User
     */
    @Override
    public void deleteById(Integer id) {

        log.debug("Deleting User with ID: {}", id);

        try{
            if (!userRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent User with ID: {}", id);
                throw new IllegalArgumentException("User not found for id: " + id);
            }
            userRepository.deleteById(id);
            log.info("Successfully deleted User with ID: {}", id);
        } catch (Exception e){
            log.error("Error deleting User with ID: {}", id, e);
            throw new RuntimeException("Failed to delete User: " + e.getMessage(), e);
        }
    }
}
