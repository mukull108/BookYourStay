package com.project.HotelBookingApp.strategy;

import com.project.HotelBookingApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{
    private final PricingStrategy wrappedPricingStrategy;
    @Override
    public BigDecimal calculatePricing(Inventory inventory) {
        BigDecimal price = wrappedPricingStrategy.calculatePricing(inventory);
        //occupancy logic
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();
        if(occupancyRate > 0.8){
            //occupancy greater than 80% so increase price
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
