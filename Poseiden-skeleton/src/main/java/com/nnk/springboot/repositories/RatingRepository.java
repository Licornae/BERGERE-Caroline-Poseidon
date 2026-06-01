package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the Rating entity.
 * Extends JpaRepository to provide standard database operations and
 * query methods for interacting with the "rating" table in the database.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
