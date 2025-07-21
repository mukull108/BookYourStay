package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.entities.HotelMinPrice;
import com.project.HotelBookingApp.entities.Inventory;
import com.project.HotelBookingApp.repositories.HotelMinPriceRepository;
import com.project.HotelBookingApp.repositories.HotelRepository;
import com.project.HotelBookingApp.repositories.InventoryRepository;
import com.project.HotelBookingApp.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingUpdateService {
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    private final PricingService pricingService;

    //Scheduler to update the inventory and HotelMinPrice table every hour
    @Scheduled(cron = "*/5 * * * * *")
    public void updatePrice(){
        int page = 0;
        int batchSize = 100;

        while(true){
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page,batchSize));
            if (hotelPage.isEmpty()){
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrice);
            page++;
        }

    }

    private void updateHotelPrice(Hotel hotel){
        log.info("pdating hotel prices for hotel id: {}",hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate,endDate);

        //Update inventories
        updateInventoryPrices(inventoryList);
        //HotelMinPrice table update
        updateHotelMinPrice(hotel,inventoryList,startDate,endDate);

    }

    private void updateInventoryPrices(List<Inventory> inventoryList){
        for (Inventory inventory:inventoryList){
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        }
        inventoryRepository.saveAll(inventoryList);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        //compute min price per day for the hotel
//        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
//                .collect(Collectors.groupingBy(
//                        Inventory::getDate,
//                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
//                ))
//                .entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        Map<LocalDate, BigDecimal> dailyMinPrices = new HashMap<>();

        for (Inventory inventory : inventoryList) {
            LocalDate date = inventory.getDate();
            BigDecimal price = inventory.getPrice();

            if (!dailyMinPrices.containsKey(date)) {
                dailyMinPrices.put(date, price);
            } else {
                // Retrieve the current minimum price for this date from the map
                BigDecimal currentMin = dailyMinPrices.get(date);

                // Compare the new price with the current minimum
                // If the new price is lower, update the map with this lower price
                if (price.compareTo(currentMin) < 0) {
                    dailyMinPrices.put(date, price);  // Update with new minimum price
                }

            }
        }


        //prepare HotelMinPrice entities in bulk
        List<HotelMinPrice> hotelMinPrices = new ArrayList<>();
        dailyMinPrices.forEach((date,price)->{
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelPrice.setMinPrice(price);
            hotelMinPrices.add(hotelPrice);
        });

        //save all HotelMinPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelMinPrices);
    }

}
