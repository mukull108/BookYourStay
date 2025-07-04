package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.entities.Inventory;
import com.project.HotelBookingApp.entities.Room;
import com.project.HotelBookingApp.repositories.InventoryRepository;
import com.project.HotelBookingApp.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for(;!today.isAfter(endDate); today=today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .room(room)
                    .bookedCount(0)
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
        inventoryRepository.deleteByRoom(room);

    }
}
