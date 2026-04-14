package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Controller class responsible for handling HTTP requests related to the Rating entity.
 * It provides operations to list, add, update, and delete Rating entries in the system.
 * Dependency Injection:
 * - Injects the RatingService to handle business logic and data persistence for Rating.
 */
@Slf4j
@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * Handles the HTTP GET request for listing all Rating entities.
     * Adds the list of Rating entities to the model and returns the name of the view to render the list.
     *
     * @param model the Model object used to pass data to the view
     * @return the name of the view for displaying the list of Rating entities
     */
    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        log.info("Listing all Rating entities");
        model.addAttribute("ratings", ratingService.findAll());
        log.debug("Found {} RuleName entities", ((List<Rating>)model.getAttribute("ratings")).size());
        return "rating/list";
    }

    /**
     * Handles the HTTP GET request to display the form for adding a new Rating entity.
     *
     * @param rating an instance of Rating used to bind form data
     * @return the name of the view for displaying the add rating form
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        log.info("Displaying add Rating form");
        return "rating/add";
    }

    /**
     * Validates and saves a Rating entity based on the provided form data.
     * If validation errors are detected, returns to the form view for correction.
     * If validation succeeds, saves the entity and redirects to the list view.
     *
     * @param rating the Rating entity to be validated and persisted
     * @param result the BindingResult object containing validation results and error details
     * @param model the Model object used to pass data to the view
     * @return the name of the view to render, either the form view for correction in case of errors
     *         or a redirect to the list view upon successful validation and persistence
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {

        log.info("Validating new Rating: {}", rating);

        if (result.hasErrors()) {
            log.warn("Validation errors found for Rating: {}", result.getAllErrors());
            return "rating/add";
        }
        try {
            Rating savedRating = ratingService.save(rating);
            log.info("Successfully saved Rating with ID: {}", savedRating.getId());
            return "redirect:/rating/list";
        } catch (Exception e) {
            log.error("Error saving RuleName: {}", rating, e);
            model.addAttribute("errorMessage", "Failed to save rating: " + e.getMessage());
            return "rating/add";
        }
    }

    /**
     * Handles the HTTP GET request to display the update form for a specific Rating entity.
     * Retrieves the Rating entity by its ID, adds it to the model, and returns the name of the view to render the form.
     *
     * @param id the ID of the Rating entity to be updated
     * @param model the Model object used to pass data to the view
     * @return the name of the view for displaying the update form
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("Displaying update form for Rating with ID: {}", id);

       try {
           Rating rating = ratingService.findById(id).orElseThrow(() ->
                   new IllegalArgumentException("Rating not found for id: " + id));
           model.addAttribute("rating", rating);
           log.debug("Found Rating for update: {}", rating);
           return "rating/update";
       }
       catch (IllegalArgumentException e) {
           log.warn("Rating not found with ID: {}", id);
            return "redirect:/rating/list";
        }
    }


    /**
     * Updates an existing Rating entity with the specified ID using the provided data.
     * If validation errors are present, returns back to the update form.
     * Otherwise, persists the changes and redirects to the list view.
     *
     * @param id the ID of the Rating entity to update
     * @param rating the Rating entity containing updated data
     * @param result the BindingResult object containing validation results
     * @param model the Model object used to pass data to the view
     * @return the name of the view to render, either the update form in case of errors
     *         or a redirect to the list view upon a successful update
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {

        log.info("Updating Rating with ID: {} - Data: {}", id, rating);

        if (result.hasErrors()) {
            log.warn("Validation errors found for Rating update: {}", result.getAllErrors());
            return "rating/update";
        }
        try {
            rating.setId(id);
            Rating updatedRating = ratingService.save(rating);
            log.info("Rating with ID {} updated successfully", updatedRating.getId());
            return "redirect:/rating/list";
        } catch (Exception e) {
            log.error("Error updating Rating with ID: {}", id, e);
            model.addAttribute("errorMessage", "Failed to update rating: " + e.getMessage());
            return "rating/update";
        }
    }

    /**
     * Handles the HTTP GET request to delete a specific Rating entity by its ID.
     * Invokes the delete operation on the associated service and redirects to the list view.
     *
     * @param id    the ID of the Rating entity to be deleted
     * @param model the Model object used to pass data to the view
     * @return a redirect string to the list view of Rating entities
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {

        log.info("Deleting Rating with ID: {}", id);

        try {
            ratingService.deleteById(id);
            log.info("Successfully deleted Rating with ID: {}", id);
            return "redirect:/rating/list";
        }
        catch (Exception e) {
            log.error("Error deleting Rating with ID: {}", id, e);
            model.addAttribute("errorMessage", "Failed to delete rating: " + e.getMessage());
            return "redirect:/rating/list";
        }
    }
}
