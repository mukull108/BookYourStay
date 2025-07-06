package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.dtos.HotelInfoDto;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id, HotelDto hotelDto);
    Boolean deleteHotelById(Long id);
    HotelDto activateHotel(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
