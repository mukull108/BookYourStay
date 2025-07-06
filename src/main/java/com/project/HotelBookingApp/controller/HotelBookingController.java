package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;
import com.project.HotelBookingApp.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest bookingRequest){
        BookingDto bookingDto = bookingService.initializeBooking(bookingRequest);
        return ResponseEntity.ok(bookingDto);
    }

}
