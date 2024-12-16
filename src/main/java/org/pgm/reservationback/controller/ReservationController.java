package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.dto.ReservationRequestDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.model.User;
import org.pgm.reservationback.repository.projection.ReservationItem;
import org.pgm.reservationback.security.UserPrinciple;
import org.pgm.reservationback.service.ReservationService;
import org.pgm.reservationback.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

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

    @PostMapping("/ready")
    public ResponseEntity<Map<String, String>> readyPayment(@RequestBody ReservationRequestDTO request) {
        // ReservationRequest DTO를 사용하여 예약 처리
        Reservation reservation = reservationService.createReservation(request);

        // 결제 준비 로직 수행 (KakaoPayService와 연동)
        Map<String, String> response = reservationService.preparePayment(reservation);

        return ResponseEntity.ok(response); // next_redirect_pc_url 등 반환
    }

    @GetMapping
    public ResponseEntity<Object> getAllReservationsOfUser(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("사용자 예약 목록 요청: 사용자 - {}", userPrinciple.getUsername());
        try {
            // reservationService.findReservationItemsOfUser(username) 호출
            List<ReservationItem> items = reservationService.findReservationItemsOfUser(userPrinciple.getUsername());
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("사용자 예약 목록 조회 오류: ", e);
            return new ResponseEntity<>("예약 목록 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{rsvId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long rsvId, @AuthenticationPrincipal UserPrinciple userPrinciple) {
        log.info("예약 취소 요청: 사용자 - {}, 예약번호 - {}", userPrinciple.getUsername(), rsvId);
        try {
            reservationService.cancelReservation(rsvId, userPrinciple.getUsername());
            return ResponseEntity.ok("예약이 취소되었습니다.");
        } catch (Exception e) {
            log.error("예약 취소 오류: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 중 오류가 발생했습니다.");
        }
    }


}
