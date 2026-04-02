package edu.icet.controller;

import edu.icet.dto.SupplierDto;
import edu.icet.service.SupplierServiceImpl;
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

public class SupplierController implements Initializable {

    private final SupplierServiceImpl service = new SupplierServiceImpl();

    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtContact;

    @FXML
    private TableView<SupplierDto> tblSuppliers;
    @FXML
    private TableColumn<SupplierDto, String> colId;
    @FXML
    private TableColumn<SupplierDto, String> colName;
    @FXML
    private TableColumn<SupplierDto, String> colContact;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        loadTableData();

        tblSuppliers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SupplierDto selectedSupplier = newValue;
                txtId.setText(selectedSupplier.getSupplierId());
                txtName.setText(selectedSupplier.getName());
                txtContact.setText(selectedSupplier.getContactNumber());
            }
        });
    }

    private void loadTableData() {
        try {
            List<SupplierDto> allSuppliers = service.getAll();
            ObservableList<SupplierDto> observableList = FXCollections.observableArrayList(allSuppliers);

            javafx.collections.transformation.FilteredList<SupplierDto> filteredData =
                    new javafx.collections.transformation.FilteredList<>(observableList, b -> true);

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(supplier -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (supplier.getSupplierId().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (supplier.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            javafx.collections.transformation.SortedList<SupplierDto> sortedData =
                    new javafx.collections.transformation.SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblSuppliers.comparatorProperty());
            tblSuppliers.setItems(sortedData);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load suppliers: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtContact.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            if (service.delete(txtId.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                loadTableData();
                btnClearOnAction(null);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot delete supplier!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            SupplierDto dto = new SupplierDto(
                    txtId.getText(),
                    txtName.getText(),
                    txtContact.getText()
            );

            boolean isSaved = service.save(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Saved Successfully!").show();
                btnClearOnAction(null);
                loadTableData();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            SupplierDto dto = new SupplierDto(
                    txtId.getText(),
                    txtName.getText(),
                    txtContact.getText()
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
