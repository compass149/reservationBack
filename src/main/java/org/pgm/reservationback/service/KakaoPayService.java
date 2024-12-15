package org.pgm.reservationback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
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

    @Value("${kakao.pay.cancel-url}")
    private String cancelUrl;

    @Value("${kakao.pay.fail-url}")
    private String failUrl;

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "KakaoAK " + adminKey);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", userId);
        params.put("pg_token", pgToken);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);

        Map<String, Object> response = restTemplate.postForObject(URI.create(approveUrl), requestEntity, Map.class);
        return response;
    }
}
