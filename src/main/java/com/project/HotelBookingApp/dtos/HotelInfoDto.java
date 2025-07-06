package com.project.HotelBookingApp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class HotelInfoDto {
    private HotelDto hotel;
    private Set<RoomDto> rooms;
}
