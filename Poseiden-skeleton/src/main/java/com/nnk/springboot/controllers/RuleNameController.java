package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
 * Controller class responsible for handling HTTP requests related to the RuleName entity.
 * It provides operations to list, add, update, and delete RuleName entries in the system.
 * Dependency Injection:
 * - Injects the RuleNameService to handle business logic and data persistence for RuleName.
 */
@Slf4j
@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;


    /**
     * Handles the HTTP GET request for listing all RuleName entities.
     * Adds the list of RuleName entities to the model and returns the name of the view to render the list.
     *
     * @param model the Model object used to pass data to the view
     * @return the name of the view for displaying the list of RuleName entities
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        log.info("Listing all RuleName entities");
        model.addAttribute("ruleNames", ruleNameService.findAll());
        log.debug("Found {} RuleName entities", ((List<RuleName>)model.getAttribute("ruleNames")).size());
        return "ruleName/list";
    }

    /**
     * Handles the HTTP GET request to display the form for adding a new RuleName entity.
     *
     * @param bid an instance of RuleName used to bind form data
     * @return the name of the view for displaying the add rule form
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        log.info("Displaying add RuleName form");
        return "ruleName/add";
    }


    /**
     * Validates and saves a RuleName entity based on the provided form data.
     * If validation errors are detected, redirects to the form view for correction.
     * If validation succeeds, saves the entity and redirects to the list view.
     *
     * @param ruleName the RuleName entity to be validated and persisted
     * @param result the BindingResult object containing validation results and error details
     * @param model the Model object used to pass data to the view
     * @return the name of the view to render, either the form view for correction in case of errors
     *         or a redirect to the list view upon successful validation and persistence
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {

        log.info("Validating new RuleName: {}", ruleName);

        if (result.hasErrors()) {
            log.warn("Validation errors found for RuleName: {}", result.getAllErrors());
            return "ruleName/add";
        }

        try {
            RuleName savedRule = ruleNameService.save(ruleName);
            log.info("Successfully saved RuleName with ID: {}", savedRule.getId());
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            log.error("Error saving RuleName: {}", ruleName, e);
            model.addAttribute("errorMessage", "Error saving rule");
            return "ruleName/add";
        }
    }

    /**
     * Handles the HTTP GET request to display the update form for a specific RuleName entity.
     * Retrieves the RuleName entity by its ID, adds it to the model, and returns the name of the view to render the form.
     *
     * @param id the ID of the RuleName entity to be updated
     * @param model the Model object used to pass data to the view
     * @return the name of the view for displaying the update form
     * @throws IllegalArgumentException if no RuleName entity is found for the provided ID
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.info("Displaying update form for RuleName with ID: {}", id);

        try {
            RuleName ruleName = ruleNameService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));
            model.addAttribute("ruleName", ruleName);
            log.debug("Found RuleName for update: {}", ruleName);
            return "ruleName/update";
        } catch (IllegalArgumentException e) {
            log.warn("RuleName not found with ID: {}", id);
            return "redirect:/ruleName/list";
        }
    }

    /**
     * Updates an existing RuleName entity with the specified ID using the provided data.
     * If validation errors are present, redirects back to the update form.
     * Otherwise, persists the changes and redirects to the list view.
     *
     * @param id the ID of the RuleName entity to update
     * @param ruleName the RuleName entity containing updated data
     * @param result the BindingResult object containing validation results
     * @param model the Model object used to pass data to the view
     * @return the name of the view to render, either the update form in case of errors
     *         or a redirect to the list view upon successful update
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        log.info("Updating RuleName with ID: {} - Data: {}", id, ruleName);

        if (result.hasErrors()) {
            log.warn("Validation errors found for RuleName update: {}", result.getAllErrors());
            return "ruleName/update";
        }

        try {
            ruleName.setId(id);
            RuleName updatedRule = ruleNameService.save(ruleName);
            log.info("Successfully updated RuleName with ID: {}", updatedRule.getId());
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            log.error("Error updating RuleName with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error updating rule");
            return "ruleName/update";
        }
    }

    /**
     * Handles the HTTP GET request to delete a specific RuleName entity by its ID.
     * Invokes the delete operation on the associated service and redirects to the list view.
     *
     * @param id    the ID of the RuleName entity to be deleted
     * @param model the Model object used to pass data to the view
     * @return a redirect string to the list view of RuleName entities
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        log.info("Deleting RuleName with ID: {}", id);

        try {
            ruleNameService.deleteById(id);
            log.info("Successfully deleted RuleName with ID: {}", id);
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            log.error("Error deleting RuleName with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error deleting rule");
            return "redirect:/ruleName/list";
        }
    }
}
