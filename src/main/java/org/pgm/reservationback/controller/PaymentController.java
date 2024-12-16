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
    public ResponseEntity<Object> kakaoPayReady(@RequestParam String orderId,
                                                @RequestParam String userId,
                                                @RequestParam String itemName,
                                                @RequestParam Integer quantity,
                                                @RequestParam Integer totalAmount) {
        try {
            Map<String, String> response = kakaoPayService.kakaoPayReady(orderId, userId, itemName, quantity, totalAmount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("결제 준비 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("결제 준비 중 오류 발생");
        }
    }

    @GetMapping("/success")
    public ResponseEntity<Object> kakaoPaySuccess(@RequestParam String pgToken,
                                                  @RequestParam String orderId,
                                                  @RequestParam String userId,
                                                  @RequestParam String tid) {
        try {
            Map<String, Object> response = kakaoPayService.kakaoPayApprove(orderId, userId, tid, pgToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("카카오페이 결제 승인 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("카카오페이 결제 승인 중 오류 발생");
        }
    }

    @GetMapping("/cancel")
    public String kakaoPayCancel() {
        return "결제가 취소되었습니다.";
    }

    @GetMapping("/fail")
    public String kakaoPayFail() {
        return "결제 실패하였습니다.";
    }
}
