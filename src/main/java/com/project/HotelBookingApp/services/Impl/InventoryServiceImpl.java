package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.dtos.HotelPriceDto;
import com.project.HotelBookingApp.dtos.HotelSearchRequest;
import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.entities.Inventory;
import com.project.HotelBookingApp.entities.Room;
import com.project.HotelBookingApp.repositories.HotelMinPriceRepository;
import com.project.HotelBookingApp.repositories.InventoryRepository;
import com.project.HotelBookingApp.services.InventoryService;
import com.project.HotelBookingApp.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for(;!today.isAfter(endDate); today=today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .hotel(room.getHotel())
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        //delete for future inventories after today date inventories should be deleted
        log.info("Deleting the inventories of room with id: {}", room.getId());
        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for {} city, from {} to {}",
                hotelSearchRequest.getCity(),hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate());

        Pageable pageable = PageRequest.of
                (hotelSearchRequest.getPage(),hotelSearchRequest.getSize());

        log.info("Searching hotel!");
        long dateCount = ChronoUnit.DAYS.between
                (hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()) +1;

        //business logic - 90 days
        Page<HotelPriceDto> hotelPage = hotelMinPriceRepository.findHotelWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount,
                pageable
        );

        return hotelPage.map((element) -> modelMapper.map(element, HotelPriceDto.class));
    }
}
