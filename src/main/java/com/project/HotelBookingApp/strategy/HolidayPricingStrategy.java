package com.project.HotelBookingApp.strategy;

import com.project.HotelBookingApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{
    private final PricingStrategy wrappedPricingStrategy;
    @Override
    public BigDecimal calculatePricing(Inventory inventory) {
        BigDecimal price = wrappedPricingStrategy.calculatePricing(inventory);
        boolean isTodayHoliday = true; //call third party api to check holiday or check with localData
        if(isTodayHoliday){
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}
