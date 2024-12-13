package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.repository.projection.ReservationItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReservationService {
    // 예약 저장
    ReservationDTO saveReservation(ReservationDTO reservationDTO);

    // 사용자별 예약 내역 조회
    List<ReservationItem> findReservationItemsOfUser(String username);

    // 모든 예약 조회
    List<Reservation> findAllReservations();

    // 예약 삭제
    void cancelReservation(Long rsvId, String username);
}
