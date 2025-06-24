package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.HotelDto;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id, HotelDto hotelDto);
    Boolean deleteHotelById(Long id);
    HotelDto activateHotel(Long id);
}
