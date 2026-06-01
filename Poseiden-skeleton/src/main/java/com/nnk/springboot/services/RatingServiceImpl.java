package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the RatingService interface for managing operations related to the Rating entity.
 * Provides methods for performing CRUD (Create, Read, Update, Delete) operations using the RatingRepository.
 * This service interacts with the database to retrieve, save, update, and delete Rating records.
 * It also includes basic validation to ensure data integrity and proper error handling when entities are not found.
 */
@Slf4j
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Retrieves all Rating entities from the database.
     *
     * @return a list of all Rating entities.
     */
    @Override
    public List<Rating> findAll() {

        log.debug("Finding all Rating entities");

        List<Rating> ratings = ratingRepository.findAll();

        log.debug("Found {} Rating entities", ratings.size());
        return ratings;
    }

    /**
     * Retrieves a Rating entity by its unique identifier.
     * If the Rating is not found, an IllegalArgumentException is thrown.
     *
     * @param id the unique identifier of the Rating to retrieve
     * @return an Optional containing the found Rating entity
     * @throws IllegalArgumentException if no Rating is found for the specified id
     */
    @Override
    public Optional<Rating> findById(Integer id) {

        log.debug("Finding Rating with ID: {}", id);

        Optional<Rating> rating = ratingRepository.findById(id);

        if (rating.isEmpty()) {
            String errorMsg = "Rating not found for id: " + id;
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        log.debug("Found Rating: {}", rating.get());
        return rating;
    }

    /**
     * Saves the specified Rating entity to the database.
     * If the entity already exists, it will be updated.
     *
     * @param rating the Rating entity to be saved
     * @return the saved Rating entity
     */
    @Override
    public Rating save(Rating rating) {

        log.debug("Saving Rating: {}", rating);

        try{ Rating savedRating = ratingRepository.save(rating);
            log.info("Successfully saved Rating with ID: {}", savedRating.getId());
            return savedRating;
        } catch (Exception e) {
            log.error("Error saving Rating: {}", rating, e);
            throw new RuntimeException("Failed to save rating: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a Rating entity by its unique identifier.
     * If the entity does not exist, an IllegalArgumentException is thrown.
     * Handles database exceptions and wraps them in a RuntimeException.
     *
     * @param id the unique identifier of the Rating to be deleted
     * @throws IllegalArgumentException if no Rating is found for the specified id
     * @throws RuntimeException if an error occurs while attempting to delete the Rating
     */
    @Override
    public void deleteById(Integer id) {

        log.debug("Deleting Rating with ID: {}", id);

        try {
            if (!ratingRepository.existsById(id)) {
                log.warn("Attempted to delete non-existent Rating with ID: {}", id);
                String errorMsg = "Rating not found for id: " + id;
                throw new IllegalArgumentException(errorMsg);
            }
            ratingRepository.deleteById(id);
            log.info("Successfully deleted Rating with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting Rating with ID: {}", id, e);
            throw new RuntimeException("Error deleting rating with id: " + id + ". "+ e.getMessage());
        }
    }
}
