package com.project.HotelBookingApp.repositories;

import com.project.HotelBookingApp.entities.Inventory;
import com.project.HotelBookingApp.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    void deleteByRoom(Room room);
}
