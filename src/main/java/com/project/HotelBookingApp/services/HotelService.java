package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.HotelDto;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);

}
