package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;
import com.project.HotelBookingApp.repositories.BookingRepository;
import com.project.HotelBookingApp.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public BookingDto initializeBooking(BookingRequest bookingRequest) {
        //hold the booking between given days -> hold the final price and hold the room for 10 mins

        return null;
    }
}
