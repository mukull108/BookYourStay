package com.project.HotelBookingApp.strategy;

import com.project.HotelBookingApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy{
    private final PricingStrategy wrappedPricingStrategy;
    @Override
    public BigDecimal calculatePricing(Inventory inventory) {
        BigDecimal price= wrappedPricingStrategy.calculatePricing(inventory); //base price same as basePricingStrategy method (sending inventory and calling calculatePricing() )
        return price.multiply(inventory.getSurgeFactor());
    }
}
