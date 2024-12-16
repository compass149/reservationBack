package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgm.reservationback.service.KakaoPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public ResponseEntity<Object> readyPayment(@RequestParam Long reservationId,
                                               @RequestParam String itemName,
                                               @RequestParam Integer quantity,
                                               @RequestParam Integer totalAmount) {
        try {
            Map<String, String> response = kakaoPayService.createPayment(reservationId, itemName, quantity, totalAmount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("결제 준비 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("결제 준비 중 오류 발생");
        }
    }
}
