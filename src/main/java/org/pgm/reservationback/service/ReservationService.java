package org.pgm.reservationback.service;

import org.pgm.reservationback.dto.ReservationDTO;
import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.repository.projection.ReservationItem;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    // 예약 저장
    Reservation saveReservation(ReservationDTO reservationDTO);

    // 사용자별 예약 내역 조회
    List<ReservationItem> findReservationItemsOfUser(String username);

    // 모든 예약 조회
    List<Reservation> findAllReservations();

    // 예약 삭제
    void deleteReservation(Long id);

    // 특정 날짜 범위에서 방 예약 가능 여부 확인
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
