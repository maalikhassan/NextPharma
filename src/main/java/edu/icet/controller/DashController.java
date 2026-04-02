package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class DashController {

    @FXML
    private AnchorPane mainContent;

    public void initialize() {
        btnHomeOnAction(null); // Loads the home screen automatically on startup
    }

    @FXML
    void btnHomeOnAction(ActionEvent event) {
        try {
            AnchorPane homePane = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/HomeForm.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(homePane);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
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
        try {
            AnchorPane posPane = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/BillingForm.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(posPane);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        System.out.println("Reports Clicked");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        try {
            AnchorPane supplierPane = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/SupplierForm.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(supplierPane);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}