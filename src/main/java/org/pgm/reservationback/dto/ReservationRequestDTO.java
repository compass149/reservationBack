package org.pgm.reservationback.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {
    private Long roomId;
    private String userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer totalUser;
    private String itemName;
    private Long totalAmount;
}

