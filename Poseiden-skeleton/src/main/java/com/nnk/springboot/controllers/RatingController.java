package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "rating/add";
        }
        try {
            ratingService.save(rating);
            return "redirect:/rating/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to save rating: " + e.getMessage());
            return "rating/add";
        }
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

       try {
           Rating rating = ratingService.findById(id).orElseThrow(() ->
                   new IllegalArgumentException("Rating not found for id: " + id));
           model.addAttribute("rating", rating);
           return "rating/update";
       }
       catch (IllegalArgumentException e) {
            return "redirect:/rating/list";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        try {
            rating.setId(id);
            ratingService.save(rating);
            return "redirect:/rating/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update rating: " + e.getMessage());
            return "rating/update";
        }
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        try {
            ratingService.deleteById(id);
            return "redirect:/rating/list";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to delete rating: " + e.getMessage());
            return "redirect:/rating/list";
        }
    }
}
