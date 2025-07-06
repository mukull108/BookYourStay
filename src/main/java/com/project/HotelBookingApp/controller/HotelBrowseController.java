package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.dtos.HotelInfoDto;
import com.project.HotelBookingApp.dtos.HotelSearchRequest;
import com.project.HotelBookingApp.services.HotelService;
import com.project.HotelBookingApp.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;
    @GetMapping(path = "/search")
    public ResponseEntity<Page<HotelDto>> searchHotel(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelDto> page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping(path = "/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        HotelInfoDto hotelInfoDto = hotelService.getHotelInfoById(hotelId);
        return ResponseEntity.ok(hotelInfoDto);
    }
}
