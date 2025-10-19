package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;
import com.project.HotelBookingApp.dtos.GuestDto;
import com.project.HotelBookingApp.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/{bookingId}/initiatePayments")
    public ResponseEntity<Map<String,String>> initiatePayments(@PathVariable Long bookingId){
        String sessionUrl =  bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelPayments(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }


}
