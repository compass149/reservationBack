package org.pgm.reservationback.repository.projection;

import org.pgm.reservationback.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReservationItem {
    Long getRsvId();
    String getRoomName();
    LocalDate getCheckIn();
    LocalDate getCheckOut();
    Reservation.Status getStatus();
}
