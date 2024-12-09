package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.model.Reservation;
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
                                                  @AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("예약 요청: 사용자 - {}", userPrinciple.getUsername());

        reservationDTO.setUsername(userPrinciple.getUsername());
        if (reservationDTO.getStatus() == null) {
            reservationDTO.setStatus(Reservation.Status.valueOf("대기"));
        }
        try {
            // reservationService.saveReservation에서 이미 ReservationDTO가 반환되므로
            // 별도의 변환 메서드는 필요없음
            ReservationDTO savedReservation = reservationService.saveReservation(reservationDTO);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("예약 저장 오류: ", e);
            return new ResponseEntity<>("예약 저장 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllReservationsOfUser(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("사용자 예약 목록 요청: 사용자 - {}", userPrinciple.getUsername());
        try {
            return ResponseEntity.ok(reservationService.findReservationItemsOfUser(userPrinciple.getUsername()));
        } catch (Exception e) {
            log.error("사용자 예약 목록 조회 오류: ", e);
            return new ResponseEntity<>("예약 목록 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
