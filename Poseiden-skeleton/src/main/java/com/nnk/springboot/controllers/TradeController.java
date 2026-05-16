package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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

import java.util.List;

/**
 * Controller class responsible for handling HTTP requests related to the Trade entity.
 * Provides endpoints for creating, reading, updating, and deleting Trade entities.
 */
@Slf4j
@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * Handles the HTTP request for displaying a list of Trade entities.
     * Adds all Trades retrieved from the service layer to the model as an attribute.
     *
     * @param model the model object used to store attributes to be rendered in the view
     * @return the name of the view to be rendered, which is "trade/list"
     */
    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        log.info("Listing all Trade entities");
        List<Trade> trades = tradeService.findAll();
        model.addAttribute("trades", trades);
        log.debug("Found {} trades", trades.size());
        return "trade/list";
    }

    /**
     * Handles the HTTP GET request to display the form for adding a new Trade entity.
     *
     * @param trade the Trade object that can be used to capture form data
     * @return the name of the view to be rendered, which is "trade/add"
     */
    @GetMapping("/trade/add")
    public String addUser(Trade trade) {
        log.info("Displaying add trade form");
        return "trade/add";
    }

    /**
     * Validates the given trade object and attempts to save it if it passes validation.
     * If validation errors are detected, the user is redirected back to the trade form.
     * If no errors are found, the trade is saved, and the user is redirected to the trade list view.
     *
     * @param trade the trade object to be validated and saved
     * @param result the binding result containing validation errors, if any
     * @param model the model object used to store attributes for rendering the view
     * @return the name of the view to be rendered, or a redirection to another endpoint
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        log.info("Validating new trade: {}", trade);
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Validation failed");
            log.warn("Validation errors found for trade: {}", result.getAllErrors());
            return "trade/add";
        }
        try {
            tradeService.save(trade);
            log.info("Successfully saved trade: {}", trade);
            return "redirect:/trade/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Saving error");
            log.error("Error saving trade: {}", e.getMessage());
            return "trade/add";
        }
    }

    /**
     * Handles the HTTP GET request to display the update form for a specific Trade entity.
     * If the Trade entity is found, it is added to the model as an attribute for rendering the update form.
     * If the Trade entity is not found, the request is redirected to the Trade list view.
     *
     * @param id the identifier of the Trade entity to be updated
     * @param model the model object used to store attributes for rendering the view
     * @return the name of the view to be rendered for updating the Trade entity or a redirection to another endpoint
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        log.info("Displaying update trade form for ID: {}", id);

        try {
            Trade trade = tradeService.findById(id);
            model.addAttribute("trade", trade);
            log.debug("Found trade for update: {}", trade);
            return "trade/update";
        } catch (EntityNotFoundException e) {
            log.warn("Trade not found with ID: {}", id);
            return "redirect:/trade/list";
        }
    }

    /**
     * Updates an existing Trade entity with new data provided in the request.
     * If validation errors occur, the user is redirected back to the update form.
     * On successful update, the user is redirected to the Trade list view.
     * If an exception occurs during the update process, an error message is added to the model.
     *
     * @param id the identifier of the Trade entity to update
     * @param trade the Trade object containing updated data
     * @param result the BindingResult object used to capture validation errors
     * @param model the model object used to store attributes for rendering the view
     * @return the name of the view to be rendered, or a redirection to another endpoint
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        log.info("Updating trade with ID: {} - Data: {}", id, trade);

        if (result.hasErrors()) {
            log.warn("Validation errors found for trade update: {}", result.getAllErrors());
            return "trade/update";
        }
        try {
            trade.setTradeId(id);
            Trade updatedTrade = tradeService.save(trade);
            log.info("Successfully updated trade with ID: {}", updatedTrade.getTradeId());
            return "redirect:/trade/list";
        } catch (Exception e) {
            log.error("Error updating trade with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error updating trade" + e.getMessage());
            return "trade/update";
        }

    }

    /**
     * Handles the HTTP request to delete a specific Trade entity identified by its ID.
     * If the deletion succeeds, the user is redirected to the Trade list view.
     * If an error occurs during the deletion process, an error message is added to the model,
     * and the user remains on the Trade list view.
     *
     * @param id the identifier of the Trade entity to be deleted
     * @param model the model object used to store attributes for rendering the view
     * @return a string representing the name of the view to be rendered or
     *         a redirection endpoint in case of successful deletion
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {

        log.info("Deleting trade with ID: {}", id);

        try {
            tradeService.deleteById(id);
            log.info("Successfully deleted trade with ID: {}", id);
            return "redirect:/trade/list";
        } catch (Exception e) {
            log.error("Error deleting trade with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error deleting trade : " + e.getMessage());
            return "trade/list";
        }
    }
}
