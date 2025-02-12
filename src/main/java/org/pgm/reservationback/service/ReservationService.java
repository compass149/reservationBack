package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.dto.ReservationRequestDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.repository.projection.ReservationItem;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    // 예약 저장
    ReservationDTO saveReservation(ReservationDTO reservationDTO);

    // 사용자별 예약 내역 조회
    List<ReservationItem> findReservationItemsOfUser(String username);

    // 모든 예약 조회
    List<Reservation> findAllReservations();
    //예약 상태 갱신
    void updateReservationPaid(Long rsvId);
    // 예약 삭제
    void cancelReservation(Long rsvId, String username);

    // 예약 생성 (추가)
    Reservation createReservation(ReservationRequestDTO requestDTO);

    // 결제 준비 (추가)
    Map<String, String> preparePayment(Reservation reservation);
}
