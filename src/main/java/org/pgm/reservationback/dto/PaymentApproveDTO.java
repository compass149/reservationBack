package org.pgm.reservationback.dto;

import lombok.Data;

@Data
public class PaymentApproveDTO {
    private String orderId;
    private String userId;
    private String tid;
}
