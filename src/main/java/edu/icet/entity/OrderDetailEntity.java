package edu.icet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEntity {
    private String orderId;
    private String medicineCode;
    private Integer qty;
    private Double unitPrice;
}