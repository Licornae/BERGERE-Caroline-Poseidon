package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import java.util.List;

/**
 * UserController is responsible for handling web requests related to user management.
 * It provides operations for listing users, adding a new user, updating an existing user,
 * validating user details, and deleting a user.
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles requests to the "/user/list" endpoint and adds a list of users to the model.
     *
     * @param model the Model object to which user data will be added
     * @return the name of the view to be rendered, in this case "user/list"
     */
    @RequestMapping("/user/list")
    public String home(Model model)
    {
        log.info("Listing all User entities");
        model.addAttribute("users", userService.findAll());
        log.debug("Found {} User entities", ((List<User>)model.getAttribute("users")).size());
        return "user/list";
    }

    /**
     * Handles a GET request for adding a new user by rendering the user addition page.
     *
     * @param bid an instance of the User object that can be used to bind form data
     * @return the name of the view template for adding a user
     */
    @GetMapping("/user/add")
    public String addUser(User bid) {
        log.info("Displaying add user form");
        return "user/add";
    }

    /**
     * Validates the provided user details and performs user creation if the input is valid.
     *
     * @param user the {@code User} object containing the details of the user to be validated and saved
     * @param result the {@code BindingResult} object containing validation results for the user object
     * @param model the {@code Model} object used to pass attributes to the view
     * @return a string representing the view name to be rendered:
     *         - "user/add" if there are validation errors or an exception during saving
     *         - "redirect:/user/list" if the user is successfully saved
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        log.info("Validating new user: {}", user);

        if (result.hasErrors()) {
            log.warn("Validation errors found for user: {}", result.getAllErrors());
            return "user/add";
        }
        try {
            log.info("Saving new user: {}", user);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.save(user);
            model.addAttribute("users", userService.findAll());
            log.info("Successfully saved user: {}", user);
            return "redirect:/user/list";
        } catch (Exception e) {
            log.error("Error saving user: {}", user, e);
            model.addAttribute("errorMessage", "Error saving user");
            return "user/add";
        }
    }

    /**
     * Displays the update form for a specific user.
     * If the user with the given ID exists, it is loaded into the model and the update form is shown.
     * If the user with the given ID does not exist, redirects to the user list page.
     *
     * @param id the ID of the user to update
     * @param model the model used to pass user data to the view
     * @return the name of the view to display (update form or redirection to the user list)
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        log.info("Displaying update form for user with ID: {}", id);

        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            user.setPassword("");
            model.addAttribute("user", user);
            log.debug("Found user for update: {}", user);
            return "user/update";
        } catch (IllegalArgumentException e) {
            log.warn("User not found with ID: {}", id);
            return "redirect:/user/list";
        }
    }

    /**
     * Updates an existing user's details based on the provided ID and form data.
     *
     * @param id the ID of the user to be updated
     * @param user the updated user details provided via the form
     * @param result the binding result containing validation results for the user object
     * @param model the model used to pass attributes to the view
     * @return the name of the view to render or a redirect URL
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {

        log.info("Updating user with ID: {} - Data: {}", id, user);

        if (result.hasErrors()) {
            log.warn("Validation errors found for user update: {}", result.getAllErrors());
            return "user/update";
        }
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setId(id);
            userService.save(user);
            model.addAttribute("users", userService.findAll());
            log.info("Successfully updated user with ID: {}", id);
            return "redirect:/user/list";
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error updating user");
            return "user/update";
        }
    }

    /**
     * Deletes a user by their unique identifier and redirects to the user list page.
     * If an error occurs during the deletion, an error message is added to the model
     * and the user is redirected to the user list page.
     *
     * @param id the unique identifier of the user to be deleted
     * @param model the model object used to add error messages if an exception occurs
     * @return a redirect URL to the user list page
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {

        log.info("Deleting user with ID: {}", id);

        try {
            userService.deleteById(id);
            log.info("Successfully deleted user with ID: {}", id);
            return "redirect:/user/list";
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error deleting user");
            return "redirect:/user/list";
        }
    }
}
