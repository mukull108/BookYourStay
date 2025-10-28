package com.project.HotelBookingApp.dtos;

import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.entities.Room;
import com.project.HotelBookingApp.entities.User;
import com.project.HotelBookingApp.entities.enums.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {
    private Long id;
//    private Hotel hotel;
//    private Room room;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal amount;
}
