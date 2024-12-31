package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.ApproveResponseDTO;
import org.pgm.reservationback.model.ReadyResponse;

import java.util.Map;

public interface KakaoPayService {


        /**
         * 결제 준비 요청
         * @param agent 사용자 디바이스 정보 (mobile, app, pc)
         * @param openType 결제 화면 종류
         * @return ReadyResponse 객체 (결제 준비 결과)
         */
        ReadyResponse ready(String agent, String openType, Long reservationId);

        /**
         * 결제 승인 요청
         * @param pgToken 승인 토큰
         * @return 승인 결과 문자열
         */
      //  String approve(String pgToken);
    ApproveResponseDTO approve(String pgToken);
    Map<String, String> kakaoPayReady(String rsvId, String username, String itemName, int quantity, int totalAmount);
}


