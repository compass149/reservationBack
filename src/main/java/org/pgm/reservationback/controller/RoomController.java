package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import org.pgm.reservationback.dto.RoomDTO;
import org.pgm.reservationback.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
@CrossOrigin(origins = "http://localhost:3000")

public class RoomController {
    private final RoomService roomService;
    @PostMapping
    public ResponseEntity<Object> saveRoom(@RequestBody RoomDTO roomDTO){
        return new ResponseEntity<>(roomService.saveRoom(roomDTO) , HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<Object> getAllRooms(){
        return new ResponseEntity<>(roomService.findAllRooms(), HttpStatus.OK);
    }
    @DeleteMapping("{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable  Long roomId){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
