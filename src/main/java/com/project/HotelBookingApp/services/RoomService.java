package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.RoomDto;
import java.util.*;

public interface RoomService {
    RoomDto createNewRoom(Long hotelId, RoomDto roomDto);
    List<RoomDto> getAllRoomsOfHotel(Long hotelId);
    RoomDto getRoomById(Long roomId);
    void deleteRoomById(Long roomId);
}
