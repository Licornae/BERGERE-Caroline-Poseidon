package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public Optional<User> findById(Integer id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            String errorMsg = "User not found for id: " + id;
            throw new IllegalArgumentException(errorMsg);
        }
        return user;
    }

    @Override
    public User save(User user) {
        try{
            return userRepository.save(user);
        } catch (Exception e){
            throw new RuntimeException("Failed to save RuleName: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {

        try{
            if (!userRepository.existsById(id)) {
                throw new IllegalArgumentException("User not found for id: " + id);
            }
            userRepository.deleteById(id);
        } catch (Exception e){
            throw new RuntimeException("Failed to delete RuleName: " + e.getMessage(), e);
        }
    }
}
