package edu.icet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String orderId;
    private LocalDate orderDate;
    private Double totalAmount;
    private List<OrderDetailDto> orderDetails; // Holds the cart items!
}
