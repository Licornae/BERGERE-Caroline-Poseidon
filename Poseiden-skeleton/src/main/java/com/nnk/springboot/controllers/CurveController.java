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

@Slf4j
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        log.info("Listing all CurvePoint entities");
        model.addAttribute("curvePoints", curvePointService.findAll());
        log.info("CurvePoint entities listed successfully");
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        log.info("Displaying add CurvePoint form");
        return "curvePoint/add";
    }

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
