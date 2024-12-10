package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.model.RoomImages;
import org.pgm.reservationback.model.Rooms;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface RoomService {
    //RoomDTO saveRoom(RoomDTO room);

    RoomDTO saveRoom(RoomDTO roomDTO);
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

        if (roomDTO.getImageUrls() != null) {
            for (int i = 0; i < roomDTO.getImageUrls().size(); i++) {
                String imageUrl = roomDTO.getImageUrls().get(i);

                // RoomImages 객체 생성
                RoomImages image = RoomImages.builder()
                        .uuid("generated-uuid-" + i)
                        .fileName(imageUrl)
                        .rooms(rooms) // 연관관계 설정
                        .ord(i)
                        .build();

                // Rooms에 이미지 추가
                rooms.addImage(image);
            }
        }
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

        // Rooms 엔티티의 RoomImages 리스트 -> DTO의 문자열 리스트 변환
        if (rooms.getImages() != null && !rooms.getImages().isEmpty()) {
            List<String> imageUrls = rooms.getImages().stream()
                    .map(RoomImages::getFileName)
                    .collect(Collectors.toList());
            roomDTO.setImageUrls(imageUrls);
        }
        return roomDTO;  //만약 room에 image가 추가된다면 entity와 dto간의 변환이 필요함
    }

    RoomDTO getRoomById(Long id);
}
