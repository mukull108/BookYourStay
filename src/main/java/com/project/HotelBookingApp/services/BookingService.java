package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;
import com.project.HotelBookingApp.dtos.GuestDto;

import java.util.List;

public interface BookingService {
    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addGuestsToBooking(List<GuestDto> guestDtoList, Long bookingId);

    String initiatePayments(Long bookingId);

}
