package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import org.pgm.reservationback.dto.ApproveResponseDTO;
import org.pgm.reservationback.model.ApproveRequest;
import org.pgm.reservationback.model.ReadyRequest;
import org.pgm.reservationback.model.ReadyResponse;
import org.pgm.reservationback.service.KakaoPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoPayServiceImpl implements KakaoPayService {

    @Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String sampleHost;

    // 결제 준비 시 발급받는 TID 저장 (간단 예시, 실제론 DB나 세션 등에 매핑 권장)
    private String tid;

    /**
     * 결제 준비 (카카오페이 Ready API)
     */
    @Override
    public ReadyResponse ready(String agent, String openType) {
        // (1) Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // (2) Request body 구성
        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)
                .partnerOrderId("1")
                .partnerUserId("1")
                .itemName("상품명")
                .quantity(1)
                .totalAmount(1100)
                .taxFreeAmount(0)
                .vatAmount(100)
                // ★ rsvId(= rsvId) 쿼리 파라미터 추가
                .approvalUrl(sampleHost + "/approve/" + agent + "/" + openType)
                .cancelUrl(sampleHost + "/cancel/" + agent + "/" + openType)
                .failUrl(sampleHost + "/fail/" + agent + "/" + openType)
                .build();

        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);

        // (3) API 호출
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        ReadyResponse readyResponse = response.getBody();

        // (4) 발급받은 TID 보관
        this.tid = readyResponse.getTid();

        return readyResponse;
    }

    @Override
    public Map<String, String> kakaoPayReady(String rsvId, String username, String itemName, int quantity, int totalAmount) {
        // 기존 구현
        Map<String, String> response = new HashMap<>();
        response.put("next_redirect_pc_url", "https://redirect.url");
        response.put("tid", "T1234567890");
        return response;
    }


    /**
     * 결제 승인 (카카오페이 Approve API)
     */
    @Override
    public ApproveResponseDTO approve(String pgToken) {
        // 1) Request header
        HttpHeaders headers = new HttpHeaders();

        // [중요] 카카오 공식 문서 기준: "KakaoAK" + {ADMIN_KEY} 형태가 일반적입니다.
        //       ("DEV_SECRET_KEY" "SECRET_KEY" 등은 현재 Kakao Pay에서 권장하지 않음)
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);

        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2) Request body (ApproveRequest) 구성
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)                  // ready에서 저장한 tid
                .partnerOrderId("1")
                .partnerUserId("1")
                .pgToken(pgToken)          // 꼭 있어야 함
                .build();

        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);

        // 3) API 호출
        try {
            // 응답을 ApproveResponseDTO로 받아서 바로 매핑
            ResponseEntity<ApproveResponseDTO> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    ApproveResponseDTO.class
            );

            // 4) 승인 결과를 DTO 형태로 반환
            return response.getBody();

        } catch (HttpStatusCodeException ex) {
            // 필요하다면 오류 내용을 직접 파싱하거나, Runtime 예외로 재포장하여 throw
            throw new RuntimeException("카카오페이 결제 승인 실패: " + ex.getResponseBodyAsString(), ex);
        }
    }

}
