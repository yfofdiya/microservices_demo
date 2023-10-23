package com.simform.hotel.controller;

import com.simform.hotel.entity.Hotel;
import com.simform.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return new ResponseEntity<List<Hotel>>(hotels, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_internal')")
    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelByHotelId(@PathVariable String hotelId) {
        Hotel hotel = hotelService.getHotelByHotelId(hotelId);
        return new ResponseEntity<Hotel>(hotel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.createHotel(hotel);
        return new ResponseEntity<Hotel>(createdHotel, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Hotel>> getHotelsByUserId(@PathVariable String userId) {
        List<Hotel> hotelsByUserId = hotelService.getAllHotelsByUserId(userId);
        return new ResponseEntity<List<Hotel>>(hotelsByUserId, HttpStatus.OK);
    }
}
