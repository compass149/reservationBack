package org.pgm.reservationback.dto;

import lombok.Data;
import org.pgm.reservationback.model.Rooms;
import org.pgm.reservationback.model.User;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {
    private Rooms room; // 방 정보
    private User user; // 사용자 정보
    private Integer totalUser; // 예약 인원
    private LocalDate checkIn; // 체크인 날짜
    private LocalDate checkOut; // 체크아웃 날짜
}
