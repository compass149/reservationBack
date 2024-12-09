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
}
