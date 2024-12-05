package org.pgm.reservationback.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rooms {

    /** 방 고유 ID (PK) **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rId;

    /** 방 이름 **/
    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    /** 방 설명 **/
    @Column(name = "description", length = 255)
    private String description;

    /** 방 타입 (성별로 구분) **/
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    /** 수용 인원 **/
    @Column(name = "capacity", nullable = false)
    private int capacity;

    /** 방 생성 시간 **/
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 방 수정 시간 **/
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** 방 타입 ENUM **/
    public enum RoomType {
        MALE, FEMALE
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
