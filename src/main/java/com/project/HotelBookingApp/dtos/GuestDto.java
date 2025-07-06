package com.project.HotelBookingApp.dtos;

import com.project.HotelBookingApp.entities.User;
import com.project.HotelBookingApp.entities.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
