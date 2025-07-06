package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.BookingDto;
import com.project.HotelBookingApp.dtos.BookingRequest;
import com.project.HotelBookingApp.entities.*;
import com.project.HotelBookingApp.entities.enums.BookingStatus;
import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import com.project.HotelBookingApp.repositories.BookingRepository;
import com.project.HotelBookingApp.repositories.HotelRepository;
import com.project.HotelBookingApp.repositories.InventoryRepository;
import com.project.HotelBookingApp.repositories.RoomRepository;
import com.project.HotelBookingApp.services.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final InventoryRepository inventoryRepository;

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest bookingRequest) {
        //hold the booking between given days -> hold the final price and hold the room for 10 mins

        log.info("Initializing booking for Hotel : {}, room: {}, from {} to {}",
                bookingRequest.getHotelId(),bookingRequest.getRoomId()
                ,bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());

        //get hotel
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found id: "+bookingRequest.getHotelId()));

        //get room
        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(()->new ResourceNotFoundException("Room not found id: "+bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount());
        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate()) +1;

        if(inventoryList.size() != daysCount){
            throw new IllegalStateException("Room is not available any more");
        }

        //reserve the room, update the booked count in inventory
        for(Inventory inventory:inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }
        //save the inventory
        inventoryRepository.saveAll(inventoryList);

        //create the booking object
        User user = new User(); //todo: remove this dummy user
        user.setId(1L);
        //todo: calculate dynamic pricing

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .roomsCount(bookingRequest.getRoomsCount())
                .hotel(hotel)
                .room(room)
                .user(user)
                .amount(BigDecimal.TEN) //todo: remove this temp price
                .build();
        Booking savedBooking = bookingRepository.save(booking);
        return modelMapper.map(savedBooking, BookingDto.class);
    }

    @Override
    public BookingDto addGuestsToBooking(List<GuestDto> guestDtoList, Long bookingId) {
        log.info("Adding guests for booking with id : {},", bookingId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found with given Id: " + bookingId));

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking has already expired!");
        }
        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, can not add guests");
        }
        for(GuestDto guestDto: guestDtoList){
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(getCurrentUser());
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);

        }
        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        Booking savedBooking = bookingRepository.save(booking);

        return modelMapper.map(savedBooking,BookingDto.class);
    }

    public boolean hasBookingExpired(Booking booking){
        //booking created + 10 min < current time that means expired
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }
    public User getCurrentUser(){
        User user = new User(); //todo: remove this dummy user
        user.setId(1L);
        return user;
    }
}
