package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;

public interface BookingService {
    BookingDto initializeBooking(BookingRequest bookingRequest);
}
