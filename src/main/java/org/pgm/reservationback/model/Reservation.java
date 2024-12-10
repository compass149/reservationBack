package org.pgm.reservationback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    /** 예약 고유 ID (PK) **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rsvId;

    /** 예약한 사용자 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "uid", nullable = false)
    private User user;

    /** 예약된 방 **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private Rooms rooms;
    /**사람 수*/
    @Column(nullable = false)
    private Integer totalUser;

    /** 예약 상태 **/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default

    private Status status = Status.대기;

    /** 체크인 날짜 **/
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    /** 체크아웃 날짜 **/
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;


    /** 예약 생성 시간 **/
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 예약 수정 시간 **/
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 예약 상태 ENUM **/
    public enum Status {
        대기, 완료, 취소
    }

    /** 엔티티 저장 전 생성 시간 설정 **/
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    /** 엔티티 수정 전 수정 시간 갱신 **/
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
