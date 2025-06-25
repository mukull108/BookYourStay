package com.project.HotelBookingApp.services.Impl;

import com.project.HotelBookingApp.dtos.HotelDto;
import com.project.HotelBookingApp.dtos.RoomDto;
import com.project.HotelBookingApp.entities.Hotel;
import com.project.HotelBookingApp.entities.Room;
import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import com.project.HotelBookingApp.repositories.HotelRepository;
import com.project.HotelBookingApp.repositories.RoomRepository;
import com.project.HotelBookingApp.services.InventoryService;
import com.project.HotelBookingApp.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper mapper;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating new room in hotel with id: {}", hotelId);
        Hotel hotelEntity = hotelRepository.findById(hotelId)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ hotelId)
                );
        Room roomEntity = mapper.map(roomDto, Room.class);
        roomEntity.setHotel(hotelEntity);
        roomEntity = roomRepository.save(roomEntity);

        //create inventory as your room created if hotel is active
        if(hotelEntity.getActive()){
            //if hotel is active then only create inventory for this room for a year
            inventoryService.initializeRoomForAYear(roomEntity);
        }
        return mapper.map(roomEntity, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsOfHotel(Long hotelId) {
        log.info("Fetching rooms in hotel with id: {}", hotelId);
        Hotel hotelEntity = hotelRepository.findById(hotelId)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Hotel was not available with given id: "+ hotelId)
                );

        List<Room> rooms = hotelEntity.getRooms();
        List<RoomDto> roomDtoList = rooms
                .stream()
                .map((element) -> mapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
        return roomDtoList;
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Fetching room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Room is not available with given Id: " + roomId)
                );
        return mapper.map(room,RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting room with id: {}", roomId);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Room is not available with given Id: " + roomId)
                );
        roomRepository.deleteById(roomId);

        //delete all future inventory for this room
        inventoryService.deleteFutureInventories(room);
    }
}
