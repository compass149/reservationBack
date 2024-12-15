package org.pgm.reservationback.controller;

import org.pgm.reservationback.service.KakaoPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class KakaoPayController {

    @Autowired
    private KakaoPayService kakaoPayService;

    @GetMapping("/kakaoPayReady")
    public String kakaoPayReady() {
        // 예: 파라미터들(orderId, userId, itemName, quantity, totalAmount)을 실제 로직에서 받아온다고 가정
        String orderId = "order1234";
        String userId = "user1234";
        String itemName = "TestItem";
        int quantity = 1;
        int totalAmount = 1000;

        Map<String, String> response = kakaoPayService.kakaoPayReady(orderId, userId, itemName, quantity, totalAmount);
        String redirectUrl = response.get("next_redirect_pc_url");
        // response 안에 tid 등의 정보도 있으므로 DB나 세션에 저장 후 사용 필요
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/kakaoPaySuccess")
    public Map<String, Object> kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
        // 실제로는 orderId, userId, tid 등을 세션이나 DB에서 불러옴
        String orderId = "order1234";
        String userId = "user1234";
        String tid = "결제 준비 단계에서 받은 tid";

        Map<String, Object> approvalResponse = kakaoPayService.kakaoPayApprove(orderId, userId, tid, pgToken);
        // 결제 승인 결과 처리 로직 (DB update, 영수증 처리 등)

        return approvalResponse;
    }

    @GetMapping("/kakaoPayCancel")
    public String kakaoPayCancel() {
        return "결제가 취소되었습니다.";
    }

    @GetMapping("/kakaoPayFail")
    public String kakaoPayFail() {
        return "결제 실패하였습니다.";
    }
}
