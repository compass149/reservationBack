package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.security.UserPrinciple;
import org.pgm.reservationback.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "http://localhost:3000")

public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Object> saveReservation(@RequestBody ReservationDTO reservationDTO,
                                   @AuthenticationPrincipal UserPrinciple userPrinciple){
        log.info(userPrinciple.getUsername());
        reservationDTO.setUsername(userPrinciple.getUsername());
        return new ResponseEntity<>(reservationService.saveReservation(reservationDTO), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<Object> getAllReservationsOfUser(@AuthenticationPrincipal
                                                            UserPrinciple userPrinciple){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+userPrinciple.getUser());
        return ResponseEntity.ok(reservationService.findReservationItemsOfUser(userPrinciple.getUsername()));
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @RequestParam Long roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut
    ) {
        log.info("Checking availability for Room ID: {} from {} to {}", roomId, checkIn, checkOut);

        // 문자열 날짜를 LocalDate로 변환
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        boolean isAvailable = reservationService.isRoomAvailable(roomId, checkInDate, checkOutDate);
        return ResponseEntity.ok(isAvailable);
    }
}
