package com.simform.hotel.feign.service;

import com.simform.hotel.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {

    @GetMapping("/ratings/hotels/{hotelId}")
    List<Rating> getAllRatingsByHotelId(@PathVariable String hotelId);
}
