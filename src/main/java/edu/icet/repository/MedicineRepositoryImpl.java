package edu.icet.repository;

import edu.icet.db.DBConnection;
import edu.icet.entity.MedicineEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineRepositoryImpl implements CrudRepository<MedicineEntity, String> {

    @Override
    public boolean save(MedicineEntity entity) throws SQLException {
        String sql = "INSERT INTO Medicine (medicine_code, name, brand, supplier_id, expiry_date, qty_on_hand, unit_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, entity.getMedicineCode());
        pstm.setString(2, entity.getName());
        pstm.setString(3, entity.getBrand());
        pstm.setString(4, entity.getSupplierId());
        pstm.setObject(5, entity.getExpiryDate()); // LocalDate maps nicely using setObject
        pstm.setInt(6, entity.getQtyOnHand());
        pstm.setDouble(7, entity.getUnitPrice());

        return pstm.executeUpdate() > 0;
    }
    @Override
    public boolean update(MedicineEntity entity) throws SQLException {
        String sql = "UPDATE Medicine SET name=?, brand=?, supplier_id=?, expiry_date=?, qty_on_hand=?, unit_price=? WHERE medicine_code=?";
        java.sql.PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, entity.getName());
        pstm.setString(2, entity.getBrand());
        pstm.setString(3, entity.getSupplierId());
        pstm.setObject(4, entity.getExpiryDate());
        pstm.setInt(5, entity.getQtyOnHand());
        pstm.setDouble(6, entity.getUnitPrice());
        pstm.setString(7, entity.getMedicineCode());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Medicine WHERE medicine_code=?";
        java.sql.PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, id);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public MedicineEntity search(String id) throws SQLException {
        String sql = "SELECT * FROM Medicine WHERE medicine_code = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet rst = pstm.executeQuery();

        if (rst.next()) {
            return new MedicineEntity(
                    rst.getString("medicine_code"),
                    rst.getString("name"),
                    rst.getString("brand"),
                    rst.getString("supplier_id"),
                    rst.getDate("expiry_date").toLocalDate(),
                    rst.getInt("qty_on_hand"),
                    rst.getDouble("unit_price")
            );
        }
        return null;
    }
    @Override
    public List<MedicineEntity> getAll() throws SQLException {
        List<MedicineEntity> medicineList = new ArrayList<>();
        String sql = "SELECT * FROM Medicine";
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();

        while (rst.next()) {
            medicineList.add(new MedicineEntity(
                    rst.getString("medicine_code"),
                    rst.getString("name"),
                    rst.getString("brand"),
                    rst.getString("supplier_id"),
                    rst.getDate("expiry_date").toLocalDate(),
                    rst.getInt("qty_on_hand"),
                    rst.getDouble("unit_price")
            ));
        }
        return medicineList;
    }

    public boolean updateStock(String code, int qty) throws SQLException {
        String sql = "UPDATE Medicine SET qty_on_hand = qty_on_hand - ? WHERE medicine_code = ?";
        java.sql.PreparedStatement pstm = edu.icet.db.DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setInt(1, qty);
        pstm.setString(2, code);
        return pstm.executeUpdate() > 0;
    }
}