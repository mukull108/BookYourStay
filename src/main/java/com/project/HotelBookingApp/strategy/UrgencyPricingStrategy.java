package com.project.HotelBookingApp.strategy;

import com.project.HotelBookingApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{
    private final PricingStrategy pricingStrategy;
    @Override
    public BigDecimal calculatePricing(Inventory inventory) {
        BigDecimal price = pricingStrategy.calculatePricing(inventory);

        LocalDate date = inventory.getDate();
        LocalDate today = LocalDate.now();

        if(!date.isBefore(today) && date.isBefore(today.plusDays(7))){
            //update the price
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }
}
