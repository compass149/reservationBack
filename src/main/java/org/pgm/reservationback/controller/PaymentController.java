package org.pgm.reservationback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pgm.reservationback.model.ReadyResponse;
import org.pgm.reservationback.service.KakaoPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j

public class PaymentController {

    @Autowired
    private KakaoPayServiceImpl paymentService;

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
    @PostMapping("/ready")
    public ResponseEntity<Object> ready(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
        try {
            System.out.println("[INFO] 결제 준비 요청 - Agent: " + agent + ", OpenType: " + openType);
            ReadyResponse readyResponse = paymentService.ready(agent, openType);

            // 모바일 환경에서 리디렉션 URL 반환
            if (agent.equals("mobile")) {
                System.out.println("[INFO] 모바일 환경 리디렉션 URL 반환");
                return ResponseEntity.ok().body(Map.of("redirectUrl", readyResponse.getNext_redirect_mobile_url()));
            }

            // 앱 환경에서 웹뷰 URL 반환
            if (agent.equals("app")) {
                System.out.println("[INFO] 앱 환경 웹뷰 URL 반환");
                return ResponseEntity.ok().body(Map.of("webviewUrl", "app://webview?url=" + readyResponse.getNext_redirect_app_url()));
            }

            // PC 환경에서 전체 응답 반환
            System.out.println("[INFO] PC 환경 응답 반환");
            return ResponseEntity.ok().body(readyResponse);
        } catch (Exception e) {
            System.err.println("[ERROR] 결제 준비 중 오류 발생: " + e.getMessage());
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
    @PostMapping("/approve")
    public ResponseEntity<Object> approve(@PathVariable("agent") String agent, @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken) {
        try {
            System.out.println("[INFO] 결제 승인 요청 - Agent: " + agent + ", OpenType: " + openType);
            String approveResponse = paymentService.approve(pgToken);
            System.out.println("[INFO] 결제 승인 성공");
            return ResponseEntity.ok().body(Map.of("response", approveResponse));
        } catch (Exception e) {
            System.err.println("[ERROR] 결제 승인 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(500).body("결제 승인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 결제 취소 엔드포인트
     * @param agent 사용자 디바이스 정보
     * @param openType 결제 화면 종류
     * @return 결제 취소 결과 메시지
     */
    @PostMapping("/cancel")
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
