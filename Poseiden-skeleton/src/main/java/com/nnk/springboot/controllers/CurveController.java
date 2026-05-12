package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import jakarta.persistence.EntityNotFoundException;
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

/**
 * The CurveController is a Spring MVC Controller that handles HTTP requests related to
 * the management of CurvePoint entities. This includes displaying the list of CurvePoints,
 * managing the creation of new CurvePoints, updating existing CurvePoints, and deleting CurvePoints.
 * The methods within this controller provide the necessary mappings for managing these
 * operations via web interfaces.
 */
@Slf4j
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    /**
     * Handles the request to list all CurvePoint entities and display them on the "curvePoint/list" view.
     *
     * @param model the model object used to pass attributes to the view, including the list of CurvePoint entities.
     * @return the name of the view ("curvePoint/list") to render the response.
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        log.info("Listing all CurvePoint entities");
        model.addAttribute("curvePoints", curvePointService.findAll());
        log.info("CurvePoint entities listed successfully");
        return "curvePoint/list";
    }

    /**
     * Displays the form to add a new CurvePoint entity.
     *
     * @param curvePoint the CurvePoint object to bind the form inputs to, typically an empty instance for form initialization.
     * @return the name of the view template ("curvePoint/add") to render the add CurvePoint form.
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        log.info("Displaying add CurvePoint form");
        return "curvePoint/add";
    }

    /**
     * Validates and processes the submission of a new CurvePoint entity. If the validation
     * fails, it redirects back to the CurvePoint form view; otherwise, the CurvePoint is
     * saved to the database, and the user is redirected to the CurvePoint list view.
     *
     * @param curvePoint the CurvePoint object submitted by the user, containing the data to validate and save.
     * @param result the BindingResult object that holds the results of validation and binding, including errors if any.
     * @param model the Model object used to pass attributes to the view in case of validation errors.
     * @return a String representing the name of the view to be rendered. It returns "curvePoint/add" if validation fails
     *         or saving the CurvePoint encounters an error, and "redirect:/curvePoint/list" if the CurvePoint is successfully saved.
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {

        log.info("Validating new CurvePoint: {}", curvePoint);

        if (result.hasErrors()) {
            log.warn("Validation errors found for CurvePoint: {}", result.getAllErrors());
            return "curvePoint/add";
        }
        try {
            curvePointService.save(curvePoint);
            log.info("Successfully saved CurvePoint with ID: {}", curvePoint.getId());
            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            log.error("Error saving CurvePoint: {}", curvePoint, e);
            return "curvePoint/add";
        }
    }

    /**
     * Displays the form for updating an existing CurvePoint entity.
     * This method retrieves the CurvePoint entity with the specified ID from the database
     * and fills the model with its data. If the entity is found, it forwards the user
     * to the "curvePoint/update" view for editing. If the entity is not found, the user is
     * redirected to the "curvePoint/list" view.
     *
     * @param id the ID of the CurvePoint entity to be updated
     * @param model the model object used to pass the CurvePoint data to the view
     * @return the name of the view to render. Returns "curvePoint/update" if the entity is
     *         found, otherwise redirects to "curvePoint/list"
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        log.info("Displaying update form for CurvePoint with ID: {}", id);

        try{
            CurvePoint curvePoint = curvePointService.findById(id);
            model.addAttribute("curvePoint", curvePoint);
            log.debug("Found CurvePoint for update: {}", curvePoint);
            return "curvePoint/update";
        } catch(EntityNotFoundException e){
            log.warn("CurvePoint not found with ID: {}", id);
            return "redirect:/curvePoint/list";
        }
    }

    /**
     * Updates an existing CurvePoint entity identified by its ID. The updated data
     * is validated before the entity is persisted. If validation errors exist, the
     * user is redirected back to the update form. On successful update, the user is
     * redirected to the CurvePoint list view. In the case of an error during the
     * update operation, an error message is displayed on the update form view.
     *
     * @param id the unique identifier of the CurvePoint entity to be updated
     * @param curvePoint the updated CurvePoint object containing the new values
     * @param result the BindingResult object that holds the results of validation and binding
     * @param model the model object used to pass attributes to the view, including error messages
     * @return a String representing the name of the view to render or the URL to redirect to.
     *         Returns "curvePoint/update" if validation fails or there is an update error.
     *         Returns "redirect:/curvePoint/list" if the update is successful.
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {

        log.info("Updating CurvePoint with ID: {} - Data: {}", id, curvePoint);

        if (result.hasErrors()) {
            log.warn("Validation errors found for CurvePoint update: {}", result.getAllErrors());
            return "curvePoint/update";
        }
        try {
            curvePoint.setId(id);
            CurvePoint updatedcurvePoint = curvePointService.save(curvePoint);
            log.info("Successfully updated CurvePoint with ID: {}", updatedcurvePoint.getId());
            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            log.error("Error updating CurvePoint with ID: {}", curvePoint.getId(), e);
            model.addAttribute("errorMessage", "Failed to update CurvePoint: " + e.getMessage());
            return "curvePoint/update";
        }
    }

    /**
     * Deletes a CurvePoint entity identified by its ID. If the deletion is successful,
     * the user is redirected to the list view of all CurvePoints. If an error occurs during
     * the deletion process, an error message is added to the model, and the user is returned
     * to the CurvePoint list view.
     *
     * @param id the ID of the CurvePoint entity to be deleted
     * @param model the Model object used to pass attributes, including error messages, to the view
     * @return a String representing the name of the view to render or the URL to redirect to.
     *         Returns "redirect:/curvePoint/list" if the deletion is successful, or "curvePoint/list"
     *         if an error occurs during the process.
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {

        log.info("Deleting CurvePoint with ID: {}", id);

        try {
            curvePointService.deleteById(id);
            log.info("Successfully deleted CurvePoint with ID: {}", id);
            return "redirect:/curvePoint/list";
        }
        catch (Exception e) {
            log.error("Error deleting CurvePoint with ID: {}", id, e);
            model.addAttribute("errorMessage", "Failed to delete CurvePoint: " + e.getMessage());
            return "curvePoint/list";
        }
    }
}
