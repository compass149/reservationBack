package org.pgm.reservationback.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AmountDTO {
    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;
}
