package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.dtos.HotelSearchRequest;
import com.project.HotelBookingApp.entities.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);

    Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
