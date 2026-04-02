package edu.icet.controller;

import edu.icet.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML private Label lblTotalMedicines, lblTotalSales, lblTotalSuppliers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAnalytics();
    }

    private void loadAnalytics() {
        try {
            // 1. Total Medicines
            ResultSet rs1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) FROM Medicine");
            if (rs1.next()) lblTotalMedicines.setText(String.valueOf(rs1.getInt(1)));

            // 2. Total Suppliers
            ResultSet rs2 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) FROM Supplier");
            if (rs2.next()) lblTotalSuppliers.setText(String.valueOf(rs2.getInt(1)));

            // 3. Total Sales Revenue
            ResultSet rs3 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT SUM(total_amount) FROM Orders");
            if (rs3.next()) lblTotalSales.setText(String.format("%.2f", rs3.getDouble(1)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPrintInventoryOnAction(ActionEvent event) {
        generateReport("/reports/InventorySummary.jrxml");
    }

    @FXML
    void btnPrintSalesReportOnAction(ActionEvent event) {
        generateReport("/reports/SalesReport.jrxml");
    }

    @FXML
    void btnPrintSuppliersOnAction(ActionEvent event) {
        generateReport("/reports/SupplierReport.jrxml");
    }

    private void generateReport(String path) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(path));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }
}