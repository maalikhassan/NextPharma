package edu.icet.controller;

import edu.icet.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.SQLException;

public class ReportsController {

    @FXML void btnPrintInventoryOnAction(ActionEvent event) { generateReport("/reports/InventorySummary.jrxml"); }
    @FXML void btnPrintSalesReportOnAction(ActionEvent event) { generateReport("/reports/SalesReport.jrxml"); }
    @FXML void btnPrintSuppliersOnAction(ActionEvent event) { generateReport("/reports/SupplierReport.jrxml"); }

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