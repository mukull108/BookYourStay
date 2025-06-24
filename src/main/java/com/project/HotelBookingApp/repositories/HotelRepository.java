package com.project.HotelBookingApp.repositories;

import com.project.HotelBookingApp.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
