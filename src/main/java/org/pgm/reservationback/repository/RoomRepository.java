package org.pgm.reservationback.repository;

import org.pgm.reservationback.model.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Rooms, Long> {
}
