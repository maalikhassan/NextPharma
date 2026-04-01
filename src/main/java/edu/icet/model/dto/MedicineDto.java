package edu.icet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDto {
    private String medicineCode;
    private String name;
    private String brand;
    private String supplierId;
    private LocalDate expiryDate;
    private Integer qtyOnHand;
    private Double unitPrice;
}
