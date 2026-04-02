package edu.icet.repository;

import edu.icet.db.DBConnection;
import edu.icet.dto.OrderDetailDto;
import edu.icet.dto.OrderDto;
import edu.icet.entity.OrderDetailEntity;
import edu.icet.entity.OrderEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl {

    public boolean saveOrder(OrderEntity entity) throws SQLException {
        String sql = "INSERT INTO Orders (order_id, order_date, total_amount) VALUES (?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, entity.getOrderId());
        pstm.setObject(2, entity.getOrderDate());
        pstm.setDouble(3, entity.getTotalAmount());
        return pstm.executeUpdate() > 0;
    }

    public boolean saveOrderDetail(OrderDetailEntity entity) throws SQLException {
        String sql = "INSERT INTO Order_Detail (order_id, medicine_code, qty, unit_price) VALUES (?, ?, ?, ?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, entity.getOrderId());
        pstm.setString(2, entity.getMedicineCode());
        pstm.setInt(3, entity.getQty());
        pstm.setDouble(4, entity.getUnitPrice());
        return pstm.executeUpdate() > 0;
    }

    // Add this to OrderRepositoryImpl.java
    public String getLastOrderId() throws SQLException {
        String sql = "SELECT order_id FROM Orders ORDER BY order_id DESC LIMIT 1";
        java.sql.ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
        if (rst.next()) {
            return rst.getString("order_id");
        }
        return null;
    }

    public List<OrderEntity> getAllOrders() throws SQLException {
        List<OrderEntity> list = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Orders");
        while (rst.next()) {
            list.add(new OrderEntity(
                    rst.getString("order_id"),
                    rst.getDate("order_date").toLocalDate(),
                    rst.getDouble("total_amount")
            ));
        }
        return list;
    }

    public List<OrderDetailEntity> getOrderDetails(String orderId) throws SQLException {
        List<OrderDetailEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM Order_Detail WHERE order_id = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, orderId);
        ResultSet rst = pstm.executeQuery();
        while (rst.next()) {
            list.add(new edu.icet.entity.OrderDetailEntity(
                    rst.getString("order_id"),
                    rst.getString("medicine_code"),
                    rst.getInt("qty"),
                    rst.getDouble("unit_price")
            ));
        }
        return list;
    }
}