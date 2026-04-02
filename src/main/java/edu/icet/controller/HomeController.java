package edu.icet.controller;

import edu.icet.db.DBConnection;
import edu.icet.dto.MedicineDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private Label lblTotalMedicines, lblTotalSales, lblTotalSuppliers;

    @FXML private TableView<MedicineDto> tblLowStock;
    @FXML private TableColumn<MedicineDto, String> colLowCode, colLowName;
    @FXML private TableColumn<MedicineDto, Integer> colLowQty;

    @FXML private TableView<MedicineDto> tblExpiring;
    @FXML private TableColumn<MedicineDto, String> colExpCode, colExpName;
    @FXML private TableColumn<MedicineDto, LocalDate> colExpDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colLowCode.setCellValueFactory(new PropertyValueFactory<>("medicineCode"));
        colLowName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colLowQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        colExpCode.setCellValueFactory(new PropertyValueFactory<>("medicineCode"));
        colExpName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        loadAlerts();
        loadAnalytics(); // Load the cards!
    }

    private void loadAlerts() {
        ObservableList<MedicineDto> lowStockList = FXCollections.observableArrayList();
        ObservableList<MedicineDto> expiringList = FXCollections.observableArrayList();
        try {
            ResultSet rsLow = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Medicine WHERE qty_on_hand < 20");
            while (rsLow.next()) lowStockList.add(new MedicineDto(rsLow.getString("medicine_code"), rsLow.getString("name"), null, null, null, rsLow.getInt("qty_on_hand"), null));
            tblLowStock.setItems(lowStockList);

            ResultSet rsExp = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Medicine WHERE expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)");
            while (rsExp.next()) expiringList.add(new MedicineDto(rsExp.getString("medicine_code"), rsExp.getString("name"), null, null, rsExp.getDate("expiry_date").toLocalDate(), null, null));
            tblExpiring.setItems(expiringList);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadAnalytics() {
        try {
            ResultSet rs1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) FROM Medicine");
            if (rs1.next()) lblTotalMedicines.setText(String.valueOf(rs1.getInt(1)));

            ResultSet rs2 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) FROM Supplier");
            if (rs2.next()) lblTotalSuppliers.setText(String.valueOf(rs2.getInt(1)));

            ResultSet rs3 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT SUM(total_amount) FROM Orders");
            if (rs3.next()) lblTotalSales.setText(String.format("%.2f", rs3.getDouble(1)));
        } catch (Exception e) { e.printStackTrace(); }
    }
}