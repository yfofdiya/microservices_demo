package com.simform.hotel.repository;

import com.simform.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, String> {

        List<Hotel> findByUserId(String userId);
}
