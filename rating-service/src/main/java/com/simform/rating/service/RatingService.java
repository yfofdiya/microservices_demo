package com.simform.rating.service;

import com.simform.rating.entity.Rating;

import java.util.List;

public interface RatingService {

    Rating createRating(Rating rating);

    List<Rating> getAllRatings();

    Rating getRatingByRatingId(String ratingId);

    List<Rating> getAllRatingsByHotelId(String hotelId);
}
