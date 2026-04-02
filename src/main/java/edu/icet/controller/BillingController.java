package edu.icet.controller;

import edu.icet.dto.CartTm;
import edu.icet.dto.MedicineDto;
import edu.icet.service.MedicineServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BillingController implements Initializable {

    @FXML private ComboBox<String> cmbMedicineCode;
    @FXML private Label lblNetTotal;
    @FXML private Label lblOrderId;
    @FXML private TextField txtBuyingQty;
    @FXML private TextField txtMedicineName;
    @FXML private TextField txtQtyOnHand;
    @FXML private TextField txtUnitPrice;

    @FXML private TableView<CartTm> tblCart;
    @FXML private TableColumn<CartTm, String> colCode;
    @FXML private TableColumn<CartTm, String> colName;
    @FXML private TableColumn<CartTm, Double> colUnitPrice;
    @FXML private TableColumn<CartTm, Integer> colQty;
    @FXML private TableColumn<CartTm, Double> colTotal;

    // Services & Local Memory
    private final MedicineServiceImpl medicineService = new MedicineServiceImpl();
    private ObservableList<CartTm> cartList = FXCollections.observableArrayList();
    private double netTotal = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Setup Table Columns
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // 2. Load Dropdown
        loadMedicineCodes();

        // 3. Add Listener to Dropdown to Auto-Fill details
        cmbMedicineCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillMedicineDetails(newValue);
            }
        });
    }

    private void loadMedicineCodes() {
        try {
            List<MedicineDto> allMedicines = medicineService.getAll();
            for (MedicineDto dto : allMedicines) {
                cmbMedicineCode.getItems().add(dto.getMedicineCode());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillMedicineDetails(String code) {
        try {
            MedicineDto medicine = medicineService.search(code);
            if (medicine != null) {
                txtMedicineName.setText(medicine.getName());
                txtUnitPrice.setText(String.valueOf(medicine.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(medicine.getQtyOnHand()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        if (cmbMedicineCode.getValue() == null || txtBuyingQty.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an item and enter quantity!").show();
            return;
        }

        try {
            String code = cmbMedicineCode.getValue();
            String name = txtMedicineName.getText();
            int qty = Integer.parseInt(txtBuyingQty.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int stock = Integer.parseInt(txtQtyOnHand.getText());

            // Stock Check
            if (qty > stock) {
                new Alert(Alert.AlertType.ERROR, "Not enough stock available!").show();
                return;
            }

            double total = qty * unitPrice;

            // Check if item is already in cart, if so, update it
            for (CartTm tm : cartList) {
                if (tm.getCode().equals(code)) {
                    if(tm.getQty() + qty > stock){
                        new Alert(Alert.AlertType.ERROR, "Cannot exceed available stock!").show();
                        return;
                    }
                    tm.setQty(tm.getQty() + qty);
                    tm.setTotal(tm.getTotal() + total);
                    tblCart.refresh();
                    calculateNetTotal();
                    txtBuyingQty.clear();
                    return;
                }
            }

            // If not in cart, add new row
            cartList.add(new CartTm(code, name, unitPrice, qty, total));
            tblCart.setItems(cartList);
            calculateNetTotal();
            txtBuyingQty.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity!").show();
        }
    }

    private void calculateNetTotal() {
        netTotal = 0.0;
        for (CartTm tm : cartList) {
            netTotal += tm.getTotal();
        }
        lblNetTotal.setText(String.format("%.2f", netTotal));
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        System.out.println("Order Placement Logic coming next!");
    }
}