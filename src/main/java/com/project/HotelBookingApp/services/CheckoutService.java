package com.project.HotelBookingApp.services;

import com.project.HotelBookingApp.entities.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);
}
