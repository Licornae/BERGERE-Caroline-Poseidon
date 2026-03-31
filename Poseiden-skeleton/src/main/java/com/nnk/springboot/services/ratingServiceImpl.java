package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ratingServiceImpl implements ratingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public ratingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Rating> findAll() {
        List<Rating> ratings = ratingRepository.findAll();
        return ratings;
    }

    @Override
    public Optional<Rating> findById(Integer id) {

        Optional<Rating> rating = ratingRepository.findById(id);

        if (rating.isEmpty()) {
            String errorMsg = "Rating not found for id: " + id;
            throw new IllegalArgumentException(errorMsg);
        }
        return rating;
    }

    @Override
    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public void deleteById(Integer id) {
    }
}
