package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.services.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto> addNewHotel(@RequestBody HotelDto hotelDto){
        log.info("Attempting to create a new hotel with " + hotelDto.getName());
        HotelDto newHotel = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(newHotel,HttpStatus.CREATED);
    }

    @GetMapping(path = "/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId){
        HotelDto hotelById = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotelById);
    }

    @PutMapping(path = "/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId,
                                                    @RequestBody HotelDto hotelDto){
        HotelDto updated = hotelService.updateHotelById(hotelId, hotelDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path = "/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){
        Boolean result = hotelService.deleteHotelById(hotelId);
        log.info("Deleted the hotel by given Id: " + result);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{hotelId}")
    public ResponseEntity<HotelDto> activateHotel(@PathVariable Long hotelId){
        HotelDto hotelDto = hotelService.activateHotel(hotelId);
        return new ResponseEntity<>(hotelDto,HttpStatus.OK);
    }
}
