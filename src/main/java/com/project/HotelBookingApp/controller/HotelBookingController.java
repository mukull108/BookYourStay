package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;
import com.project.HotelBookingApp.dtos.GuestDto;
import com.project.HotelBookingApp.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest bookingRequest){
        BookingDto bookingDto = bookingService.initializeBooking(bookingRequest);
        return ResponseEntity.ok(bookingDto);
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuestToBooking(@RequestBody List<GuestDto> guestDtoList,
                                                        @PathVariable Long bookingId){
        BookingDto bookingDto = bookingService.addGuestsToBooking(guestDtoList,bookingId);
        return ResponseEntity.ok(bookingDto);
    }


}
