package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import org.pgm.reservationback.dto.PaymentApproveDTO;
import org.pgm.reservationback.dto.PaymentRequestDTO;
import org.pgm.reservationback.service.KakaoPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public ResponseEntity<Map<String, String>> readyPayment(@RequestBody PaymentRequestDTO requestDTO) {
        // 결제 준비 수행
        Map<String, String> response = kakaoPayService.kakaoPayReady(
                requestDTO.getOrderId(),
                requestDTO.getUserId(),
                requestDTO.getItemName(),
                requestDTO.getQuantity(),
                requestDTO.getTotalAmount()
        );

        return ResponseEntity.ok(response); // next_redirect_pc_url 반환
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approvePayment(
            @RequestParam String pgToken,
            @RequestBody PaymentApproveDTO approveDTO
    ) {
        // 결제 승인 수행
        String result = kakaoPayService.kakaoPayApprove(
                approveDTO.getOrderId(),
                approveDTO.getUserId(),
                approveDTO.getTid(),
                pgToken
        );

        return ResponseEntity.ok(result);
    }
}
