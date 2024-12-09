package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.model.Rooms;
import org.pgm.reservationback.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public RoomDTO saveRoom(RoomDTO roomDTO) {
        Rooms room = dtoToEntity(roomDTO);

        if (room.getId() != null && roomRepository.existsById(room.getId())) {
            // 수정 작업: 기존 생성일 유지
            Rooms existingRoom = roomRepository.findById(room.getId()).orElseThrow();
            room.setCreatedAt(existingRoom.getCreatedAt());
        } else {
            // 새로운 방 추가: 현재 시간을 생성일로 설정
            room.setCreatedAt(LocalDateTime.now());
        }

        Rooms savedRoom = roomRepository.save(room);
        return entityToDto(savedRoom);
    }
    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<RoomDTO> findAllRooms() {
        List<Rooms> rooms = roomRepository.findAll();
        List<RoomDTO> roomDTOs = rooms.stream()
                .map(room -> entityToDto(room))
                .collect(Collectors.toList());
        return roomDTOs;
    }
}
