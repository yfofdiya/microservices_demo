package com.simform.rating.service;

import com.simform.rating.entity.Rating;
import com.simform.rating.exception.ResourceNotFoundException;
import com.simform.rating.repository.RatingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating createRating(Rating rating) {
        log.info("Creating New Rating");
        String randomRatingId = UUID.randomUUID().toString();
        rating.setRatingId(randomRatingId);
        Rating createdRating = ratingRepository.save(rating);
        log.info("Rating is added");
        return createdRating;
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating getRatingByRatingId(String ratingId) {
        return ratingRepository.findById(ratingId).orElseThrow(
                () ->
                        new ResourceNotFoundException(
                                "Rating with given id is not found " + ratingId)
        );
    }

    @Override
    public List<Rating> getAllRatingsByHotelId(String hotelId) {
        log.info("Fetch all the ratings for provided hotel id {}", hotelId);
        List<Rating> allRatingsByHotelId = ratingRepository.findByHotelId(hotelId);
        log.info("Fetched successfully");
        return allRatingsByHotelId;
    }
}
