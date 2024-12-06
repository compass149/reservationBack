package org.pgm.reservationback.repository;

import org.pgm.reservationback.model.Reservation;
import org.pgm.reservationback.repository.projection.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select " + "rooms.roomName as name, rsv.totalUser as quantity, rsv.createdAt as reservationTime " +
            "from Reservation rsv left join Rooms rooms on rooms.rId = rsv.rooms.rId " +
            "where rsv.user.username = :username")
    List<ReservationItem> findAllReservationsOfUser(@Param("username") String username);

}

