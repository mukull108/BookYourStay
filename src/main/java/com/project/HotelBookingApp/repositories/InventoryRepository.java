package com.project.HotelBookingApp.repositories;

import com.project.HotelBookingApp.entities.Inventories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventories,Long> {

}
