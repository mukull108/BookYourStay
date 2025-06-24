package com.project.HotelBookingApp.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDto {
    private Long id;
    private String type;
    private BigDecimal basePrice;
    private Integer totalCount;
    private Integer capacity;
    private String[] photos;
    private String[] amenities;
}
