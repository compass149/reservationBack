package org.pgm.reservationback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pgm.reservationback.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long rsvId;
    private RoomDTO room;
    /** 예약한 사용자 */
    private String username;

    /** 방 이름 */
    private Long roomId; // roomName 대신 roomId를 사용

    /** 예약 인원 수 */
    private Integer totalUser;

    /** 예약 생성 시간 (필요하다면 유지, 필요없다면 제거) */
    private LocalDateTime reserveTime;

    /** 체크인 날짜 */
    private LocalDate checkIn;

    /** 체크아웃 날짜 */
    private LocalDate checkOut;

    private Reservation.Status status;
}
