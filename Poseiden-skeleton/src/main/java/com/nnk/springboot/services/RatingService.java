package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing operations related to the Rating entity.
 * Provides methods for CRUD (Create, Read, Update, Delete) operations.
 */
public interface RatingService {

    List<Rating> findAll();

    Optional<Rating> findById(Integer id);

    Rating save(Rating rating);

    void deleteById(Integer id);
}
