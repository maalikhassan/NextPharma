package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BillingController {

    @FXML
    private ComboBox<String> cmbMedicineCode;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblOrderId;

    @FXML
    private TextField txtBuyingQty;

    @FXML
    private TextField txtMedicineName;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtUnitPrice;

    // Cart Table
    @FXML
    private TableView<?> tblCart;
    @FXML
    private TableColumn<?, ?> colCode;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        System.out.println("Added to cart!");
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        System.out.println("Order Placed!");
    }
}