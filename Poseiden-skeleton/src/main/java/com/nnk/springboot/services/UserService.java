package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;

import java.util.List;
import java.util.Optional;


/**
 * Service interface for managing operations related to the User entity.
 * Provides methods for CRUD (Create, Read, Update, Delete) operations.
 */
public interface UserService {

    List<User> findAll();

    Optional<User> findById(Integer id);

    User save(User user);

    User update(int id, User user);

    void deleteById(Integer id);
}
