package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${kakao.pay.admin-key}")
    private String adminKey;

    @Value("${kakao.pay.cid}")
    private String cid;

    @Value("${kakao.pay.success-url}")
    private String successUrl;

    @Value("${kakao.pay.cancel-url}")
    private String cancelUrl;

    @Value("${kakao.pay.fail-url}")
    private String failUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> createPayment(Long reservationId, String itemName, Integer quantity, Integer totalAmount) {
        String paymentUrl = "https://kapi.kakao.com/v1/payment/ready";

        // 요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("partner_order_id", "order-" + reservationId);
        params.add("partner_user_id", "user-" + reservationId);
        params.add("item_name", itemName);
        params.add("quantity", String.valueOf(quantity));
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", "0");
        params.add("approval_url", successUrl);
        params.add("cancel_url", cancelUrl);
        params.add("fail_url", failUrl);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(paymentUrl, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("결제 생성 중 오류 발생: {}", response.getBody());
            throw new RuntimeException("결제 생성 실패");
        }
    }
}
