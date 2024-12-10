package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.model.RoomImages;
import org.pgm.reservationback.model.Rooms;
import org.pgm.reservationback.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final FileService fileService;

    @Override
    public RoomDTO saveRoom(RoomDTO roomDTO) {
        Rooms room;
        if (roomDTO.getId() != null && roomDTO.getId() > 0) {
            // 수정 로직
            room = roomRepository.findById(roomDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));
            room.setRoomName(roomDTO.getName());
            room.setDescription(roomDTO.getDescription());
            room.setPricePerNight(roomDTO.getPrice());
            room.setCapacity(roomDTO.getCapacity());
            room.clearImages(); // 기존 이미지 모두 제거
        } else {
            // 신규 생성 로직
            room = new Rooms();
            room.setRoomName(roomDTO.getName());
            room.setDescription(roomDTO.getDescription());
            room.setPricePerNight(roomDTO.getPrice());
            room.setCapacity(roomDTO.getCapacity());
        }

        // 이미지 추가
        if (roomDTO.getImageUrls() != null && !roomDTO.getImageUrls().isEmpty()) {
            int order = 0;
            for (String fileName : roomDTO.getImageUrls()) {
                RoomImages image = RoomImages.builder()
                        .uuid(java.util.UUID.randomUUID().toString())
                        .fileName(fileName)
                        .ord(order++)
                        .build();
                room.addImage(image);
            }
        }

        Rooms saved = roomRepository.save(room);
        return convertToDTO(saved);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<RoomDTO> findAllRooms() {
        List<Rooms> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Rooms room = roomRepository.findById(id).orElse(null);
        if (room == null) {
            return null;
        }
        return convertToDTO(room);
    }

    private RoomDTO convertToDTO(Rooms room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getRoomName());
        dto.setDescription(room.getDescription());
        dto.setPrice(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setUpdatedAt(room.getUpdatedAt());

        // RoomImages에서 fileName 추출하여 DTO의 imageUrls에 설정
        List<String> imageUrls = room.getImages().stream()
                .sorted() // ord 기준으로 정렬
                .map(RoomImages::getFileName)
                .collect(Collectors.toList());
        dto.setImageUrls(imageUrls);

        return dto;
    }
}

