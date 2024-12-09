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
    @Query("SELECT rsv.rsvId as rsvId, rooms.roomName as roomName, rsv.checkIn as checkIn, rsv.checkOut as checkOut, rsv.status as status " +
            "FROM Reservation rsv " +
            "LEFT JOIN Rooms rooms ON rooms.id = rsv.rooms.id " +
            "JOIN rsv.user user " +
            "WHERE user.username = :username")
    List<ReservationItem> findAllReservationsOfUser(@Param("username") String username);

    // 특정 날짜 범위에서 방의 예약 충돌 여부 확인
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.rooms.id = :roomId " +
            "AND ((r.checkIn < :checkOut AND r.checkOut > :checkIn))")
    int countOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
