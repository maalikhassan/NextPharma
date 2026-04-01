package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MedicineController {

    @FXML
    private DatePicker dateExpiry;

    @FXML
    private TextField txtBrand;
    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtQty;
    @FXML
    private TextField txtSupplierId;

    @FXML
    private TableView<?> tblMedicines;
    @FXML
    private TableColumn<?, ?> colBrand;
    @FXML
    private TableColumn<?, ?> colCode;
    @FXML
    private TableColumn<?, ?> colExpiry;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colSupplier;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtCode.clear();
        txtName.clear();
        txtBrand.clear();
        txtSupplierId.clear();
        dateExpiry.setValue(null);
        txtQty.clear();
        txtPrice.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        System.out.println("Delete Clicked");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        System.out.println("Save Clicked");
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        System.out.println("Update Clicked");
    }
}