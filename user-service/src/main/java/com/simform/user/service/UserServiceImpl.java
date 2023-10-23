package com.simform.user.service;

import com.simform.user.entity.Hotel;
import com.simform.user.entity.Rating;
import com.simform.user.entity.User;
import com.simform.user.exception.ResourceNotFoundException;
import com.simform.user.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public static final String HOTEL_SERVICE_URL = "http://HOTEL-SERVICE/hotels/users/";

    @Override
    public User createUser(User user) {
        log.info("Creating user");
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        User createdUser = userRepository.save(user);
        log.info("User created successfully");
        return createdUser;
    }

    @Override
    @CircuitBreaker(name = "hotelsCircuitBreaker", fallbackMethod = "getAllUsers")
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        for (User u: users) {
            List<Hotel> allCheckedInHotels = getAllCheckedInHotels(u.getUserId());
            u.setHotels(allCheckedInHotels);
        }
        log.info("Fetched all users with it's checked in hotels");
        return users;
    }

    @Override
    @CircuitBreaker(name = "hotelsCircuitBreaker", fallbackMethod = "getUserByUserId")
    public User getUserByUserId(String userId) {
        log.info("Fetching User by given user id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () ->
                        new ResourceNotFoundException(
                                "User with given id is not found " + userId)
        );
        List<Hotel> allCheckedInHotels = getAllCheckedInHotels(userId);
        user.setHotels(allCheckedInHotels);
        log.info("Fetched user and it's checked in hotels");
        return user;
    }

    public List<User> getAllUsers(Exception e) {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.info("Adding dummy hotels as hotel service is down");
        for (User u: users) {
            u.setHotels(fetchDummyHotels(u.getUserId()));
        }
        log.info("Fetched all users with it's checked in hotels");
        return users;
    }

    public User getUserByUserId(String userId, Exception e) {
        log.info("Fetching User by given user id {}", userId);
        User user = userRepository.findById(userId).orElseThrow(
                () ->
                        new ResourceNotFoundException(
                                "User with given id is not found " + userId)
        );
        log.info("Adding dummy hotels as hotel service is down");
        user.setHotels(fetchDummyHotels(userId));
        log.info("Fetched user and it's checked in hotels");
        return user;
    }

    private List<Hotel> fetchDummyHotels(String userId) {
        String hotelId = UUID.randomUUID().toString();
        return List.of(Hotel
                .builder()
                .hotelId(hotelId)
                .name("The City")
                .location("Kutch")
                .about("Hotel and resort")
                .userId(userId)
                .ratings(List.of(Rating.builder()
                        .ratingId(UUID.randomUUID().toString())
                        .hotelId(hotelId)
                        .rating(5)
                        .feedback("Average")
                        .build())
                )
                .build()
        );
    }

    private List<Hotel> getAllCheckedInHotels(String userId) {
        log.info("Fetching all hotels for provided user id {}", userId);
        return restTemplate.getForObject(HOTEL_SERVICE_URL + userId, List.class);
    }
}
