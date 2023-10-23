package com.simform.hotel.service;

import com.simform.hotel.entity.Hotel;

import java.util.List;

public interface HotelService {
    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();

    Hotel getHotelByHotelId(String hotelId);

    List<Hotel> getAllHotelsByUserId(String userId);
}
