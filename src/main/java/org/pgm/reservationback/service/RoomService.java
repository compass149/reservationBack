package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.model.Rooms;

import java.util.List;

public interface RoomService {
    RoomDTO saveRoom(RoomDTO room);
    void deleteRoom(Long id);
    List<RoomDTO> findAllRooms();
    
    default Rooms dtoToEntity(RoomDTO roomDTO) {
        Rooms rooms = Rooms.builder()
                .id(roomDTO.getId())
                .roomName(roomDTO.getName())
                .capacity(roomDTO.getCapacity())
                .pricePerNight(roomDTO.getPrice())
                .description(roomDTO.getDescription())
                .build();
        return rooms;
    }
    
    default RoomDTO entityToDto(Rooms rooms) {
        RoomDTO roomDTO = RoomDTO.builder()
                .id(rooms.getId())
                .name(rooms.getRoomName())
                .price(rooms.getPricePerNight())
                .capacity(rooms.getCapacity())
                .description(rooms.getDescription())
                .createdAt(rooms.getCreatedAt())
                .build();
        return roomDTO;  //만약 room에 image가 추가된다면 entity와 dto간의 변환이 필요함
    }
}
