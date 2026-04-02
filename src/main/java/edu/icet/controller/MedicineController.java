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
    private TextField txtSearch;
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
    private TableView<MedicineDto> tblMedicines;

    @FXML
    private TableColumn<MedicineDto, String> colCode;

    @FXML
    private TableColumn<MedicineDto, String> colName;

    @FXML
    private TableColumn<MedicineDto, String> colBrand;

    @FXML
    private TableColumn<MedicineDto, String> colSupplier;

    @FXML
    private TableColumn<MedicineDto, java.time.LocalDate> colExpiry;

    @FXML
    private TableColumn<MedicineDto, Integer> colQty;

    @FXML
    private TableColumn<MedicineDto, Double> colPrice;

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

            // --- INSTANT SEARCH LOGIC START ---
            // 1. Wrap the ObservableList in a FilteredList
            javafx.collections.transformation.FilteredList<MedicineDto> filteredData =
                    new javafx.collections.transformation.FilteredList<>(observableList, b -> true);

            // 2. Set the filter Predicate whenever the filter changes
            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(medicine -> {
                    // If filter text is empty, display all medicines
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    // Search by Code or Name!
                    if (medicine.getMedicineCode().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches code
                    } else if (medicine.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches name
                    }
                    return false; // Does not match
                });
            });

            // 3. Wrap the FilteredList in a SortedList (to keep table sorting functional)
            javafx.collections.transformation.SortedList<MedicineDto> sortedData =
                    new javafx.collections.transformation.SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator
            sortedData.comparatorProperty().bind(tblMedicines.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table
            tblMedicines.setItems(sortedData);
            // --- INSTANT SEARCH LOGIC END ---

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