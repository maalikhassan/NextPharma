package edu.icet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineEntity {
    private String medicineCode;
    private String name;
    private String brand;
    private String supplierId;
    private LocalDate expiryDate;
    private Integer qtyOnHand;
    private Double unitPrice;
}