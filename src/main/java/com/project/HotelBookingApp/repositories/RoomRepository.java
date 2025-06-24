package com.project.HotelBookingApp.repositories;

import com.project.HotelBookingApp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {

}
