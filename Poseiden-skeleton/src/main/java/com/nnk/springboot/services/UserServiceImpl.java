package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Override
    public User save(User user) {

        log.debug("Saving User: {}", user);

        try{
            User savedUser = userRepository.save(user);
            log.info("Successfully saved User with ID: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e){
            log.error("Error saving User: {}", user, e);
            throw new RuntimeException("Failed to save RuleName: " + e.getMessage(), e);
        }
    }

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
            throw new RuntimeException("Failed to delete RuleName: " + e.getMessage(), e);
        }
    }
}