package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    @Value("${kakao.pay.success-url}")
    private String successUrl;

    @Value("${kakao.pay.cancel-url}")
    private String cancelUrl;

    @Value("${kakao.pay.fail-url}")
    private String failUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> kakaoPayReady(String orderId, String userId, String itemName, int quantity, int totalAmount) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + adminKey);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("cid", cid);
            params.add("partner_order_id", orderId);
            params.add("partner_user_id", userId);
            params.add("item_name", itemName);
            params.add("quantity", String.valueOf(quantity));
            params.add("total_amount", String.valueOf(totalAmount));
            params.add("tax_free_amount", "0");
            params.add("approval_url", successUrl);
            params.add("cancel_url", cancelUrl);
            params.add("fail_url", failUrl);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(readyUrl, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (Map<String, String>) response.getBody();
            } else {
                throw new RuntimeException("카카오페이 결제 준비 API 호출 실패");
            }
        } catch (Exception e) {
            log.error("카카오페이 결제 준비 중 오류: {}", e.getMessage());
            throw new RuntimeException("카카오페이 결제 준비 중 오류 발생", e);
        }
    }

    public Map<String, Object> kakaoPayApprove(String orderId, String userId, String tid, String pgToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + adminKey);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("cid", cid);
            params.add("tid", tid);
            params.add("partner_order_id", orderId);
            params.add("partner_user_id", userId);
            params.add("pg_token", pgToken);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(approveUrl, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return (Map<String, Object>) response.getBody();
            } else {
                throw new RuntimeException("카카오페이 결제 승인 API 호출 실패");
            }
        } catch (Exception e) {
            log.error("카카오페이 결제 승인 중 오류: {}", e.getMessage());
            throw new RuntimeException("카카오페이 결제 승인 중 오류 발생", e);
        }
    }
}
