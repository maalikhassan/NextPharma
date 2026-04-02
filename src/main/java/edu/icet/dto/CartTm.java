package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartTm {
    private String code;
    private String name;
    private Double unitPrice;
    private Integer qty;
    private Double total;
}