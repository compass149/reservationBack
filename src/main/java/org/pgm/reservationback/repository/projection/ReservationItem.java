package org.pgm.reservationback.repository.projection;

import java.time.LocalDateTime;

public interface ReservationItem {
    String getName();
    Integer getQuantity();
    LocalDateTime getReserveTime();
}
