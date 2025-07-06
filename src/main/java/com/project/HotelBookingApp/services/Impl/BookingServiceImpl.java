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

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());
        if(inventoryList.size() != daysCount){
            throw new IllegalStateException("Room is not available any more");
        }

        //reserve the room, update the booked count in inventory
        for(Inventory inventory:inventoryList){
            inventory.setBookedCount(inventory.getBookedCount() + bookingRequest.getRoomsCount());
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
}
