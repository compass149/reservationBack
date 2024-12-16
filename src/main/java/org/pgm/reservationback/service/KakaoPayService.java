package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
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

    @Value("${kakao.pay.ready-url}")
    private String readyUrl;

    @Value("${kakao.pay.approve-url}")
    private String approveUrl;

    @Value("${kakao.pay.approval-url}")
    private String approvalUrl;
    @Value("${kakao.pay.success-url}")
    private String successUrl;

    @Value("${kakao.pay.cancel-url}")
    private String cancelUrl;

    @Value("${kakao.pay.fail-url}")
    private String failUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> createPayment(Long reservationId, String itemName, Integer quantity, Integer totalAmount) {
        String paymentUrl = "https://kapi.kakao.com/v1/payment/ready";
    public Map<String, String> kakaoPayReady(String orderId, String userId, String itemName, int quantity, int totalAmount) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + adminKey);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", userId);
        params.put("item_name", itemName);
        params.put("quantity", quantity);
        params.put("total_amount", totalAmount);
        params.put("tax_free_amount", 0);
        params.put("approval_url", approvalUrl);
        params.put("cancel_url", cancelUrl);
        params.put("fail_url", failUrl);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

        Map<String, String> response = restTemplate.postForObject(URI.create(readyUrl), entity, Map.class);
        return response;
    }

    public Map<String, Object> kakaoPayApprove(String orderId, String userId, String tid, String pgToken) {
        RestTemplate restTemplate = new RestTemplate();

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
        headers.add("Authorization", "KakaoAK " + adminKey);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", userId);
        params.put("pg_token", pgToken);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(paymentUrl, requestEntity, Map.class);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("결제 생성 중 오류 발생: {}", response.getBody());
            throw new RuntimeException("결제 생성 실패");
        }
        Map<String, Object> response = restTemplate.postForObject(URI.create(approveUrl), requestEntity, Map.class);
        return response;
    }
}
