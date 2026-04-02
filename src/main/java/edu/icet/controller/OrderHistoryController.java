package edu.icet.controller;

import edu.icet.dto.OrderDetailDto;
import edu.icet.dto.OrderDto;
import edu.icet.service.OrderServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {

    @FXML private TextField txtSearch;

    // Master Table
    @FXML private TableView<OrderDto> tblOrders;
    @FXML private TableColumn<OrderDto, String> colOrderId;
    @FXML private TableColumn<OrderDto, LocalDate> colDate;
    @FXML private TableColumn<OrderDto, Double> colTotal;

    // Detail Table
    @FXML private TableView<OrderDetailDto> tblOrderDetails;
    @FXML private TableColumn<OrderDetailDto, String> colMedCode;
    @FXML private TableColumn<OrderDetailDto, Integer> colQty;
    @FXML private TableColumn<OrderDetailDto, Double> colUnitPrice;

    private final OrderServiceImpl service = new OrderServiceImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup Master Columns
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        // Setup Detail Columns
        colMedCode.setCellValueFactory(new PropertyValueFactory<>("medicineCode"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        loadTableData();

        // The Click Listener: When an order is clicked, fetch its details!
        tblOrders.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadOrderDetails(newVal.getOrderId());
            }
        });
    }

    private void loadTableData() {
        try {
            ObservableList<OrderDto> observableList = FXCollections.observableArrayList(service.getAllOrders());
            FilteredList<OrderDto> filteredData = new FilteredList<>(observableList, b -> true);

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(order -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lowerCaseFilter = newValue.toLowerCase();
                    return order.getOrderId().toLowerCase().contains(lowerCaseFilter) || order.getOrderDate().toString().contains(lowerCaseFilter);
                });
            });

            SortedList<OrderDto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblOrders.comparatorProperty());
            tblOrders.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadOrderDetails(String orderId) {
        try {
            // Fetch items for the clicked order and put them in the bottom table
            List<OrderDetailDto> details = service.getOrderDetails(orderId);
            tblOrderDetails.setItems(FXCollections.observableArrayList(details));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}