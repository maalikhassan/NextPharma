package edu.icet.controller;

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

public class MedicineController implements Initializable {

    private final MedicineServiceImpl service = new MedicineServiceImpl();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Map columns to MedicineDto variable names EXACTLY as they are typed in the DTO class
        colCode.setCellValueFactory(new PropertyValueFactory<>("medicineCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colExpiry.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        // 2. Load the data
        loadTableData();

        tblMedicines.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // 1. Cast the generic newValue to our specific MedicineDto
                MedicineDto selectedMedicine = (MedicineDto) newValue;

                // 2. Use the strongly-typed selectedMedicine object
                txtCode.setText(selectedMedicine.getMedicineCode());
                txtName.setText(selectedMedicine.getName());
                txtBrand.setText(selectedMedicine.getBrand());
                txtSupplierId.setText(selectedMedicine.getSupplierId());
                dateExpiry.setValue(selectedMedicine.getExpiryDate());
                txtQty.setText(String.valueOf(selectedMedicine.getQtyOnHand()));
                txtPrice.setText(String.valueOf(selectedMedicine.getUnitPrice()));
            }
        });
    }

    private void loadTableData() {
        try {
            List<MedicineDto> allMedicines = service.getAll();
            ObservableList<MedicineDto> observableList = FXCollections.observableArrayList(allMedicines);

            // This cast is needed because we used wildcards <?, ?> in the FXML generation earlier
            ((TableView<MedicineDto>) tblMedicines).setItems(observableList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load medicines: " + e.getMessage()).show();
        }
    }

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
        try {
            if (service.delete(txtCode.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                loadTableData();
                btnClearOnAction(null);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot delete! It might be tied to an order.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            // 1. Gather data from UI
            MedicineDto dto = new MedicineDto(
                    txtCode.getText(),
                    txtName.getText(),
                    txtBrand.getText(),
                    txtSupplierId.getText(),
                    dateExpiry.getValue(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtPrice.getText())
            );

            // 2. Pass to Service Layer
            boolean isSaved = service.save(dto);

            // 3. Handle Result
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Medicine Saved Successfully!").show();
                btnClearOnAction(null); // Clears the form after saving
                loadTableData();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Please enter valid numbers for Qty and Price!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An unexpected error occurred.").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            MedicineDto dto = new MedicineDto(
                    txtCode.getText(), txtName.getText(), txtBrand.getText(),
                    txtSupplierId.getText(), dateExpiry.getValue(),
                    Integer.parseInt(txtQty.getText()), Double.parseDouble(txtPrice.getText())
            );
            if (service.update(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                loadTableData();
                btnClearOnAction(null);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error!").show();
        }
    }

}