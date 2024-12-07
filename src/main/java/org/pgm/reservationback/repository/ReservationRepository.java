package org.pgm.reservationback.repository;

import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.repository.projection.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 사용자별 예약 목록 조회
    @Query("SELECT rooms.roomName as name, rsv.totalUser as quantity, rsv.createdAt as reservationTime " +
            "FROM Reservation rsv " +
            "LEFT JOIN Rooms rooms ON rooms.rId = rsv.rooms.rId " +
            "WHERE rsv.user.username = :username")
    List<ReservationItem> findAllReservationsOfUser(@Param("username") String username);

    // 특정 날짜 범위에서 방의 예약 충돌 여부 확인
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.rooms.rId = :roomId " +
            "AND ((r.checkIn < :checkOut AND r.checkOut > :checkIn))")
    int countOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
