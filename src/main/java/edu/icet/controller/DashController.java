package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class DashController {

    @FXML
    private AnchorPane mainContent;

    // Low Stock Table
    @FXML
    private TableView<?> tblLowStock;
    @FXML
    private TableColumn<?, ?> colLowStockCode;
    @FXML
    private TableColumn<?, ?> colLowStockName;
    @FXML
    private TableColumn<?, ?> colLowStockQty;

    // Expiring Table
    @FXML
    private TableView<?> tblExpiring;
    @FXML
    private TableColumn<?, ?> colExpCode;
    @FXML
    private TableColumn<?, ?> colExpName;
    @FXML
    private TableColumn<?, ?> colExpDate;

    // Navigation Button Actions
    @FXML
    void btnHomeOnAction(ActionEvent event) {
        System.out.println("Home Clicked");
        // Logic to clear/reset the mainContent area
    }

    @FXML
    void btnMedicinesOnAction(ActionEvent event) {
        try {
            // Load the MedicineForm FXML
            AnchorPane medicinePane = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/MedicineForm.fxml"));

            // Clear the current content in the main area
            mainContent.getChildren().clear();

            // Add the new form to the main area
            mainContent.getChildren().add(medicinePane);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        System.out.println("POS Clicked");
        // Logic to load POSForm.fxml into mainContent
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        System.out.println("Reports Clicked");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        System.out.println("Suppliers Clicked");
    }
}