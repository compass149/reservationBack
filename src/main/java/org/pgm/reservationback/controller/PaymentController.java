package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgm.reservationback.model.ReadyResponse;
import org.pgm.reservationback.service.KakaoPayService;
import org.pgm.reservationback.service.KakaoPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000")

@Slf4j

public class PaymentController {

    @Autowired
    private KakaoPayServiceImpl paymentService;
    private final KakaoPayService kakaoPayService;

    /**
     * 기본 엔드포인트: API 상태 확인용
     * @return API 작동 여부 메시지 반환
     */
    @GetMapping("/")
    public ResponseEntity<String> index() {
        System.out.println("[INFO] Payment API가 정상적으로 작동 중입니다.");
        return ResponseEntity.ok("Payment API is running");
    }

    /**
     * 결제 준비 엔드포인트
     * @param agent 사용자 디바이스 정보 (mobile, app, pc)
     * @param openType 결제 화면 종류
     * @return 결제 준비 결과 (리디렉션 URL 포함)
     */
    @PostMapping("/ready/{agent}/{openType}")
    public ResponseEntity<Object> ready(
            @PathVariable("agent") String agent,
            @PathVariable("openType") String openType) {
        try {
            System.out.println("[INFO] 결제 준비 요청 - Agent: " + agent + ", OpenType: " + openType);
            ReadyResponse readyResponse = kakaoPayService.ready(agent, openType);

            if ("mobile".equals(agent)) {
                return ResponseEntity.ok().body(Map.of("redirectUrl", readyResponse.getNext_redirect_mobile_url()));
            } else if ("app".equals(agent)) {
                return ResponseEntity.ok().body(Map.of("webviewUrl", "app://webview?url=" + readyResponse.getNext_redirect_app_url()));
            } else {
                return ResponseEntity.ok().body(readyResponse); // PC의 경우
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("결제 준비 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 결제 승인 엔드포인트
     * @param agent 사용자 디바이스 정보
     * @param openType 결제 화면 종류
     * @param pgToken 결제 승인 토큰
     * @return 결제 승인 결과
     */
    @GetMapping("/approve/{agent}/{openType}")
    public ResponseEntity<Object> approve(
            @PathVariable String agent,
            @PathVariable String openType,
            @RequestParam("pg_token") String pgToken) {
        try {
            // KakaoPay 결제 승인 처리
            String approvalResult = kakaoPayService.approve(pgToken);
            return ResponseEntity.ok(approvalResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 승인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 결제 취소 엔드포인트
     * @param agent 사용자 디바이스 정보
     * @param openType 결제 화면 종류
     * @return 결제 취소 결과 메시지
     */
    @PostMapping("/cancel/{agent}/{openType}")
    public ResponseEntity<Object> cancel(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
        try {
            System.out.println("[INFO] 결제 취소 요청 - Agent: " + agent + ", OpenType: " + openType);
            // 취소 처리 로직 수행 (예: KakaoPay API와 연동하여 상태 확인)
            System.out.println("[INFO] 결제 취소 처리 완료");
            return ResponseEntity.ok("결제가 취소되었습니다: Agent - " + agent + ", OpenType - " + openType);
        } catch (Exception e) {
            System.err.println("[ERROR] 결제 취소 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 결제 실패 엔드포인트
     * @param agent 사용자 디바이스 정보
     * @param openType 결제 화면 종류
     * @return 결제 실패 결과 메시지
     */
    @PostMapping("/fail/{agent}/{openType}")
    public ResponseEntity<Object> fail(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
        try {
            System.out.println("[INFO] 결제 실패 요청 - Agent: " + agent + ", OpenType: " + openType);
            // 실패 처리 로직 수행 (예: KakaoPay API와 연동하여 상태 확인)
            System.out.println("[INFO] 결제 실패 처리 완료");
            return ResponseEntity.ok("결제가 실패되었습니다: Agent - " + agent + ", OpenType - " + openType);
        } catch (Exception e) {
            System.err.println("[ERROR] 결제 실패 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body("결제 실패 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
