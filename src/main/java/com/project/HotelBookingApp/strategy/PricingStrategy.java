package com.project.HotelBookingApp.strategy;

import com.project.HotelBookingApp.entities.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePricing(Inventory inventory);
}
