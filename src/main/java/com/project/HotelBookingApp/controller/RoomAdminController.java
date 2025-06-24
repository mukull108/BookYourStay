package com.project.HotelBookingApp.controller;

import com.project.HotelBookingApp.dtos.RoomDto;
import com.project.HotelBookingApp.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(path = "/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
        RoomDto newRoom = roomService.createNewRoom(hotelId, roomDto);
        return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsForHotel(@PathVariable Long hotelId){
        List<RoomDto> allRoomsOfHotel = roomService.getAllRoomsOfHotel(hotelId);
        return ResponseEntity.ok(allRoomsOfHotel);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId){
        RoomDto roomById = roomService.getRoomById(roomId);
        return ResponseEntity.ok(roomById);
    }

    @DeleteMapping("/roomId")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

}
