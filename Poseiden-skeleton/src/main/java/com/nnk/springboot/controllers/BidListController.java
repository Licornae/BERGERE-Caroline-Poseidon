package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller class for handling HTTP requests related to BidList operations.
 * Provides methods for listing, adding, validating, updating, and deleting bids.
 * It uses the BidListService to perform business logic and interacts with the views
 * for displaying data or forms to the users.
 */
@Slf4j
@Controller
public class BidListController {

    @Autowired
    private BidListService bidListService;

    /**
     * Customizes the initialization of the {@link WebDataBinder} to handle specific data binding cases.
     * Registers a custom editor for {@link Timestamp} to parse string representations into {@link Timestamp} objects.
     * This method ensures that text inputs representing timestamps are correctly converted to {@link Timestamp} instances
     * using specific date and time formatting rules.
     *
     * @param binder the {@link WebDataBinder} used to bind request parameters to JavaBean objects
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                    return;
                }

                String value = text.trim();
                try {
                    if (value.contains("T")) {
                        LocalDateTime localDateTime = LocalDateTime.parse(
                                value,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                        );
                        setValue(Timestamp.valueOf(localDateTime));
                    } else {
                        setValue(Timestamp.valueOf(value));
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Invalid timestamp format: " + value, ex);
                }
            }
        });
    }

    /**
     * Handles requests for the endpoint "/bidList/list" to retrieve and display all bid lists.
     *
     * @param model the Model object that holds attributes for rendering the view
     * @return the name of the view template to render, specifically "bidList/list"
     */
    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        log.info("Retrieving all bid lists for display");
        model.addAttribute("bidLists", bidListService.findAll());
        log.info("Bid lists retrieved successfully");
        return "bidList/list";
    }

    /**
     * Handles requests to display the form for adding a new BidList.
     *
     * @param bid the BidList object to be populated with user input
     * @return the name of the view template to render, specifically "bidList/add"
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        log.info("Displaying add bid form");
        return "bidList/add";
    }

    /**
     * Handles the submission of a new bid and validates the received data.
     * If validation errors are present, returns the form view for correction.
     * If the bid is valid, attempts to save it using the service and redirects to the list view.
     * Logs details of the process including validation errors and exceptions.
     *
     * @param bid the BidList object containing the details of the bid to be validated and saved
     * @param result the BindingResult object holding validation errors, if any
     * @param model the Model object for adding attributes used in rendering views
     * @return the name of the view template to display; either "bidList/add" if validation fails
     *         or saving fails, or "redirect:/bidList/list" if the bid is saved successfully
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        log.info("Validating new bid: {}", bid);
        if (result.hasErrors()) {
            log.warn("Validation errors found for bid: {}", result.getAllErrors());
            return "bidList/add";
        }
        try {
            bidListService.save(bid);
            log.info("Bid saved successfully: {}", bid);
            return "redirect:/bidList/list";
        } catch (Exception e) {
            log.error("Error saving bid: {}", e.getMessage());
            return "bidList/add";
        }
    }

    /**
     * Displays the update form for a specific BidList identified by its ID.
     * Fetches the BidList from the database using the provided ID and fills the model
     * with the retrieved data to render the update form view. If an exception occurs during
     * data retrieval, redirects to the list view.
     *
     * @param id the ID of the BidList to be updated
     * @param model the Model object used to pass attributes to the view
     * @return the name of the view template to render for updating the BidList,
     *         or redirects to the list view if an error occurs
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        log.info("Displaying update form for bid with ID: {}", id);

        try{
            BidList bidList = bidListService.findById(id);
            model.addAttribute("bidList", bidList);
            log.debug("Found bid for update: {}", bidList);
            return "bidList/update";
        } catch (Exception e) {
            log.warn("Error fetching bid for update: {}", e.getMessage());
            return "redirect:/bidList/list";
        }
    }

    /**
     * Handles the update of an existing BidList identified by its ID.
     * Validates the incoming data and, if valid, saves the updated BidList.
     * If validation errors occur or an exception is thrown during the update process,
     * the method will return the form view for correction or display an error message.
     *
     * @param id the ID of the BidList to be updated
     * @param bidList the BidList object containing the updated details
     * @param result the BindingResult object holding validation errors, if any
     * @param model the Model object used to add attributes for rendering views
     * @return the name of the view template to display; either "bidList/update" if validation
     *         fails or an error occurs, or "redirect:/bidList/list" if the update is successful
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {

        log.info("Updating bid with ID: {} - Data: {}", id, bidList);

        if (result.hasErrors()) {
            log.warn("Validation errors found for bid update: {}", result.getAllErrors());
            return "bidList/update";
        }
        try {
            bidListService.save(bidList);
            log.info("Successfully updated bid with ID: {}", id);
            return "redirect:/bidList/list";
        } catch (Exception e) {
            log.error("Error updating bid with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error updating bid");
            return "bidList/update";
        }
    }

    /**
     * Deletes a BidList entry identified by its ID and redirects to the bid list view.
     * If an error occurs during deletion, adds an error message to the model and redirects
     * to the bid list view.
     *
     * @param id the ID of the BidList to be deleted
     * @param model the Model object used to add attributes for rendering views
     * @return the name of the view template to redirect to, specifically "redirect:/bidList/list"
     */
    @PostMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {

        log.info("Deleting bid with ID: {}", id);

        try{
            bidListService.deleteById(id);
            log.info("Successfully deleted bid with ID: {}", id);
            return "redirect:/bidList/list";
        }catch (Exception e) {
            log.error("Error deleting bid with ID: {}", id, e);
            model.addAttribute("errorMessage", "Error deleting bid");
            return "redirect:/bidList/list";
        }
    }
}
