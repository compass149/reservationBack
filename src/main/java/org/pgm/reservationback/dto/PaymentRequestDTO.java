package org.pgm.reservationback.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String orderId;
    private String userId;
    private String itemName;
    private int quantity;
    private int totalAmount;
}
