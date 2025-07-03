package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.entities.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);
}
