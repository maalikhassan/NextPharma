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

    // You can leave these returning false/null for now to satisfy the interface
    @Override public boolean update(MedicineEntity entity) throws SQLException { return false; }
    @Override public boolean delete(String s) throws SQLException { return false; }
    @Override public MedicineEntity search(String s) throws SQLException { return null; }

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
}