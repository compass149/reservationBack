package org.pgm.reservationback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "room_availability")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailability {

    /** 예약 가능 데이터 ID (PK) **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 방 ID (FK) **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "rid", nullable = false)
    private Rooms rooms;

    /** 예약 가능 날짜 **/
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /** 이벤트 **/
    @Column(name = "event", length = 255)
    private String event;

    /** 예약 가능 여부 **/
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

}
