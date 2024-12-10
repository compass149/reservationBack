package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.service.RoomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @Value("${com.pgm.upload.path}")
    private String uploadPath;

    // 특정 roomId로 방 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRoomById(@PathVariable Long id) {
        RoomDTO roomDTO = roomService.getRoomById(id);
        if (roomDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roomDTO);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> saveRoom(@RequestParam("name") String name,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") Long price,
                                           @RequestParam("capacity") Long capacity,
                                           @RequestParam(value = "images", required = false) MultipartFile[] images) {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            log.error("업로드 경로 생성 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 파일 경로를 생성하는 도중 오류가 발생했습니다.");
        }

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setName(name);
        roomDTO.setDescription(description);
        roomDTO.setPrice(price);
        roomDTO.setCapacity(capacity);

        List<String> imageUrls = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                if(!image.isEmpty()) {
                    String originalFilename = image.getOriginalFilename();
                    String uuid = UUID.randomUUID().toString();
                    String saveFileName = uuid + "_" + originalFilename;
                    Path savePath = Paths.get(uploadPath, saveFileName);
                    try {
                        image.transferTo(savePath.toFile());
                        imageUrls.add(saveFileName);
                    } catch (IOException e) {
                        log.error("이미지 저장 중 오류 발생: {}", e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 중 오류 발생");
                    }
                }
            }
        }

        roomDTO.setImageUrls(imageUrls);

        try {
            RoomDTO savedRoom = roomService.saveRoom(roomDTO);
            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("방 정보 저장 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("방 정보 저장 중 오류가 발생했습니다.");
        }
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateRoom(@PathVariable Long id,
                                             @RequestParam("name") String name,
                                             @RequestParam("description") String description,
                                             @RequestParam("price") Long price,
                                             @RequestParam("capacity") Long capacity,
                                             @RequestParam(value = "image", required = false) MultipartFile image) {
        // 파일 업로드 경로 체크 및 이미지 처리 로직 (Post와 동일)
        // ...

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(id);
        roomDTO.setName(name);
        roomDTO.setDescription(description);
        roomDTO.setPrice(price);
        roomDTO.setCapacity(capacity);
        if (image != null && !image.isEmpty()) {
            String originalFilename = image.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveFileName = uuid + "_" + originalFilename;

            Path savePath = Paths.get(uploadPath, saveFileName);
            try {
                RoomDTO savedRoom = roomService.saveRoom(roomDTO);
                return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("방 정보 저장 중 오류 발생: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("방 정보 저장 중 오류가 발생했습니다.");
            }
        }

        try {
            RoomDTO updatedRoom = roomService.saveRoom(roomDTO);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        } catch (Exception e) {
            log.error("방 정보 수정 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("방 정보 수정 중 오류가 발생했습니다.");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllRooms() {
        try {
            return new ResponseEntity<>(roomService.findAllRooms(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("방 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("방 목록 조회 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable Long roomId) {
        try {
            roomService.deleteRoom(roomId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("방 삭제 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("방 삭제 중 오류가 발생했습니다.");
        }
    }
}