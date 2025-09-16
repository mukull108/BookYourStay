package com.project.HotelBookingApp.dtos;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String email;
    private String password;
    private String name;

}
