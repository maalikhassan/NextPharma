package edu.icet.service;

import edu.icet.db.DBConnection;
import edu.icet.dto.OrderDetailDto;
import edu.icet.dto.OrderDto;
import edu.icet.entity.OrderDetailEntity;
import edu.icet.entity.OrderEntity;
import edu.icet.repository.MedicineRepositoryImpl;
import edu.icet.repository.OrderRepositoryImpl;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderServiceImpl {

    private final OrderRepositoryImpl orderRepo = new OrderRepositoryImpl();
    private final MedicineRepositoryImpl medicineRepo = new MedicineRepositoryImpl();

    public String generateNextOrderId() throws SQLException {
        String lastId = orderRepo.getLastOrderId();
        if (lastId != null) {
            // Splits "ORD-003" into "ORD" and "003", then adds 1 to the number
            int idNum = Integer.parseInt(lastId.split("-")[1]);
            idNum++;
            return String.format("ORD-%03d", idNum); // Formats back to ORD-004
        }
        return "ORD-001"; // If the database is completely empty
    }

    public boolean placeOrder(OrderDto orderDto) throws SQLException {
        // 1. Get connection and turn OFF auto-commit
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            // 1. Map OrderDto -> OrderEntity
            OrderEntity orderEntity = new OrderEntity(
                    orderDto.getOrderId(),
                    orderDto.getOrderDate(),
                    orderDto.getTotalAmount()
            );

            // 2. Save the Entity
            boolean isOrderSaved = orderRepo.saveOrder(orderEntity);
            if (isOrderSaved) {

                // 3. Loop through the cart and save details + update stock
                for (OrderDetailDto detailDto : orderDto.getOrderDetails()) {

                    // Map OrderDetailDto -> OrderDetailEntity
                    OrderDetailEntity detailEntity = new OrderDetailEntity(
                            detailDto.getOrderId(),
                            detailDto.getMedicineCode(),
                            detailDto.getQty(),
                            detailDto.getUnitPrice()
                    );

                    boolean isDetailSaved = orderRepo.saveOrderDetail(detailEntity);
                    if (isDetailSaved) {
                        boolean isStockUpdated = medicineRepo.updateStock(detailEntity.getMedicineCode(), detailEntity.getQty());
                        if (!isStockUpdated) {
                            connection.rollback(); // Failed to update stock, cancel everything!
                            return false;
                        }
                    } else {
                        connection.rollback(); // Failed to save detail, cancel everything!
                        return false;
                    }
                }

                // 4. If we made it here, everything worked perfectly.
                connection.commit();
                return true;
            }

            connection.rollback(); // Failed to save order
            return false;

        } catch (SQLException e) {
            connection.rollback(); // If any SQL error happens, cancel everything!
            throw e;
        } finally {
            // 5. ALWAYS turn auto-commit back on before closing the transaction
            connection.setAutoCommit(true);
        }
    }
}