package com.project.HotelBookingApp.strategy;

import com.project.HotelBookingApp.entities.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{
    @Override
    public BigDecimal calculatePricing(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
