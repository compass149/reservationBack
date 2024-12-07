package org.pgm.reservationback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;

    /** 예약한 사용자 */
    private String username;

    /** 방 ID (자동으로 선택됨) */
    private Long roomId;

    /** 예약 인원 수 */
    private Integer totalUser;

    /** 예약 생성 시간 */
    private LocalDateTime reserveTime;

    /** 체크인 날짜 */
    private LocalDate checkIn;

    /** 체크아웃 날짜 */
    private LocalDate checkOut;
}
