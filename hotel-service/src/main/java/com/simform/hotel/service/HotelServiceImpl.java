package com.simform.hotel.service;

import com.simform.hotel.entity.Hotel;
import com.simform.hotel.entity.Rating;
import com.simform.hotel.exception.ResourceNotFoundException;
import com.simform.hotel.feign.service.RatingService;
import com.simform.hotel.repository.HotelRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RatingService ratingService;

    public static final String RATING_SERVICE_URL = "http://RATING-SERVICE/ratings/hotels/";

    @Override
    public Hotel createHotel(Hotel hotel) {
        log.info("Creating Hotel");
        String randomHotelId = UUID.randomUUID().toString();
        hotel.setHotelId(randomHotelId);
        Hotel createdHotel = hotelRepository.save(hotel);
        log.info("New Hotel Created");
        return createdHotel;
    }

    @Override
    @CircuitBreaker(name = "ratingsCircuitBreaker", fallbackMethod = "getAllHotels")
//    @Retry(name = "ratingsRetry", fallbackMethod = "getAllHotels")
//    @RateLimiter(name = "ratingsRateLimiter", fallbackMethod = "getAllHotels")
    public List<Hotel> getAllHotels() {
        log.info("Fetching All Hotels");
        int attempt = 1;
        log.info("Attempt -> {}", attempt);
        attempt++;
        List<Hotel> hotels = hotelRepository.findAll();
        for (Hotel h : hotels) {
            List<Rating> allRatings = getAllRatingsByHotelId(h.getHotelId());
            h.setRatings(allRatings);
        }
        log.info("Fetched All Hotels and it's ratings");
        return hotels;
    }

    @Override
    @CircuitBreaker(name = "ratingsCircuitBreaker", fallbackMethod = "getHotelByHotelId")
//    @Retry(name = "ratingsRetry", fallbackMethod = "getHotelByHotelId")
//    @RateLimiter(name = "ratingsRateLimiter", fallbackMethod = "getHotelByHotelId")
    public Hotel getHotelByHotelId(String hotelId) {
        log.info("Fetching Hotel by given hotel id {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () ->
                        new ResourceNotFoundException(
                                "Hotel with given id is not found " + hotelId)
        );
        List<Rating> allRatings = getAllRatingsByHotelId(hotelId);
        hotel.setRatings(allRatings);
        log.info("Fetched Hotel and it's ratings");
        return hotel;
    }

    @Override
    @CircuitBreaker(name = "ratingsCircuitBreaker", fallbackMethod = "getAllHotelsByUserId")
//    @Retry(name = "ratingsRetry", fallbackMethod = "getAllHotelsByUserId")
//    @RateLimiter(name = "ratingsRateLimiter", fallbackMethod = "getAllHotelsByUserId")
    public List<Hotel> getAllHotelsByUserId(String userId) {
        log.info("Fetching Hotel by given user id {}", userId);
        List<Hotel> hotels = hotelRepository.findByUserId(userId);
        for (Hotel h : hotels) {
            log.info("Fetching all ratings for hotel id {}", h.getHotelId());
            List<Rating> allRatings = getAllRatingsByHotelId(h.getHotelId());
            h.setRatings(allRatings);
        }
        log.info("Fetched all the hotels by user id {}", userId);
        return hotels;
    }

    public List<Hotel> getAllHotels(Exception e) {
        log.info("Fetching All Hotels");
        List<Hotel> hotels = hotelRepository.findAll();
        log.info("Adding Dummy Ratings as rating service is down");
        for (Hotel h : hotels) {
            h.setRatings(getDummyRating(h.getHotelId()));
        }
        return hotels;
    }

    public Hotel getHotelByHotelId(String hotelId, Exception e) {
        log.info("Fetching Hotel by given hotel id {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () ->
                        new ResourceNotFoundException(
                                "Hotel with given id is not found " + hotelId)
        );
        log.info("Adding Dummy Ratings as rating service is down");
        hotel.setRatings(getDummyRating(hotelId));
        log.info("Fetched Hotel and it's ratings");
        return hotel;
    }

    public List<Hotel> getAllHotelsByUserId(String userId, Exception e) {
        log.info("Fetching Hotel by given user id {}", userId);
        List<Hotel> hotels = hotelRepository.findByUserId(userId);
        log.info("Adding Dummy Ratings as rating service is down");
        for (Hotel h : hotels) {
            log.info("Fetching all ratings for hotel id {}", h.getHotelId());
            h.setRatings(getDummyRating(h.getHotelId()));
        }
        log.info("Fetched all the hotels by user id {}", userId);
        return hotels;
    }

    private List<Rating> getAllRatingsByHotelId(String hotelId) {
        log.info("Fetching all ratings for provided hotel id {}", hotelId);
//        return restTemplate.getForObject(RATING_SERVICE_URL + hotelId, List.class);
        return ratingService.getAllRatingsByHotelId(hotelId);
    }

    private List<Rating> getDummyRating(String hotelId) {
        return List.of(Rating.builder()
                .ratingId(UUID.randomUUID().toString())
                .hotelId(hotelId)
                .rating(5)
                .feedback("Average")
                .build()
        );
    }
}
