package com.project.HotelBookingApp.entities;

import java.util.Set;

import com.project.HotelBookingApp.entities.enums.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; //we will encode it.

    @Column(nullable = true)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER) //this will create another table name=app_users_role
    @Enumerated(EnumType.STRING)
    private Set<Roles> role;



}
