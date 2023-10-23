package com.simform.rating.controller;

import com.simform.rating.entity.Rating;
import com.simform.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return new ResponseEntity<List<Rating>>(ratings, HttpStatus.OK);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<Rating> getRatingByRatingId(@PathVariable String ratingId) {
        Rating rating = ratingService.getRatingByRatingId(ratingId);
        return new ResponseEntity<Rating>(rating, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        Rating createdRating = ratingService.createRating(rating);
        return new ResponseEntity<Rating>(createdRating, HttpStatus.CREATED);
    }

    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable String hotelId) {
        List<Rating> ratingsByHotelId = ratingService.getAllRatingsByHotelId(hotelId);
        return new ResponseEntity<List<Rating>>(ratingsByHotelId, HttpStatus.OK);
    }
}
