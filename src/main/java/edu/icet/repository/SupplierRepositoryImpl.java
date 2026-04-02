package edu.icet.repository;

import edu.icet.db.DBConnection;
import edu.icet.entity.SupplierEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements CrudRepository<SupplierEntity, String> {

    @Override
    public boolean save(SupplierEntity entity) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("INSERT INTO supplier (supplier_id, name, contact_number) VALUES (?, ?, ?)");
        pst.setString(1, entity.getSupplierId());
        pst.setString(2, entity.getName());
        pst.setString(3, entity.getContactNumber());
        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean update(SupplierEntity entity) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("UPDATE supplier SET name = ?, contact_number = ? WHERE supplier_id = ?");
        pst.setString(1, entity.getName());
        pst.setString(2, entity.getContactNumber());
        pst.setString(3, entity.getSupplierId());
        return pst.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("DELETE FROM supplier WHERE supplier_id = ?");
        pst.setString(1, id);
        return pst.executeUpdate() > 0;
    }

    @Override
    public SupplierEntity search(String id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM supplier WHERE supplier_id = ?");
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return new SupplierEntity(
                    rs.getString("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact_number")
            );
        }
        return null;
    }

    @Override
    public List<SupplierEntity> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM supplier");
        ResultSet rs = pst.executeQuery();
        List<SupplierEntity> supplierList = new ArrayList<>();
        while (rs.next()) {
            supplierList.add(new SupplierEntity(
                    rs.getString("supplier_id"),
                    rs.getString("name"),
                    rs.getString("contact_number")
            ));
        }
        return supplierList;
    }
}
