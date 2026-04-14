package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
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

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.save(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving user");
            return "user/add";
        }
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            user.setPassword("");
            model.addAttribute("user", user);
            return "user/update";
        } catch (IllegalArgumentException e) {
            return "redirect:/user/list";
        }
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setId(id);
            userService.save(user);
            model.addAttribute("users", userService.findAll());
            return "redirect:/user/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating user");
            return "user/update";
        }
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {

        try {
            userService.deleteById(id);
            return "redirect:/user/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting user");
            return "redirect:/user/list";
        }
    }
}
