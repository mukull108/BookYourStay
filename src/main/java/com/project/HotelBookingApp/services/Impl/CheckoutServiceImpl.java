package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.entities.Booking;
import com.project.HotelBookingApp.entities.User;
import com.project.HotelBookingApp.repositories.BookingRepository;
import com.project.HotelBookingApp.services.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImpl implements CheckoutService {

    private final BookingRepository bookingRepository;


    @Override
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {
        log.info("Creating session for booking with id: "+ booking.getId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            //Creating a customer
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();

            Customer customer = Customer.create(customerCreateParams);



            //Creating a Session
            SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)

                    //adding details of the order
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("inr")
                                            .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(booking.getHotel().getName() + " : " + booking.getRoom().getType())
                                                    .setDescription("Booking ID: "+booking.getId())
                                            .build())
                                    .build())
                            .build()
                    )
                    .build();
            log.info("Booking amount is: "+ booking.getAmount());

            Session session = Session.create(sessionCreateParams);
            booking.setPaymentSessionId(session.getId());
            bookingRepository.save(booking);

            log.info("Session created for booking with id: "+ booking.getId());
            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
