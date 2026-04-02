package edu.icet.controller;

import edu.icet.dto.CartTm;
import edu.icet.dto.MedicineDto;
import edu.icet.service.MedicineServiceImpl;
import edu.icet.service.OrderServiceImpl;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;
import java.util.HashMap;
import java.util.Map;

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
    private final OrderServiceImpl orderService = new OrderServiceImpl();

    private void generateOrderId() {
        try {
            String nextId = orderService.generateNextOrderId();
            lblOrderId.setText(nextId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Table Columns
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        generateOrderId();
        // Dropdown
        loadMedicineCodes();

        //  Listener to Dropdown to Auto-Fill details
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
        if (cartList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty!").show();
            return;
        }

        try {
            String orderId = lblOrderId.getText();
            java.time.LocalDate orderDate = java.time.LocalDate.now();

            // 1. Create a list of OrderDetailDto from our Cart table
            java.util.List<edu.icet.dto.OrderDetailDto> orderDetails = new java.util.ArrayList<>();
            for (CartTm tm : cartList) {
                orderDetails.add(new edu.icet.dto.OrderDetailDto(
                        orderId, tm.getCode(), tm.getQty(), tm.getUnitPrice()
                ));
            }

            // 2. Package everything into a single OrderDto
            edu.icet.dto.OrderDto orderDto = new edu.icet.dto.OrderDto(
                    orderId, orderDate, netTotal, orderDetails
            );

            // 3. Pass to the Service layer to handle the transaction
            boolean isPlaced = orderService.placeOrder(orderDto);

            if (isPlaced) {
                // --- JASPER REPORT GENERATION START ---
                try {
                    // 1. Load the JRXML file you just created
                    JasperDesign design = net.sf.jasperreports.engine.xml.JRXmlLoader.load(getClass().getResourceAsStream("/reports/Invoice.jrxml"));
                    JasperReport jasperReport = JasperCompileManager.compileReport(design);

                    // 2. Set the Parameters (Order ID and Total)
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("orderId", orderId);
                    parameters.put("netTotal", String.format("%.2f", netTotal));

                    // 3. Pass the JavaFX Cart directly to the Report!
                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cartList);

                    // 4. Fill and View the Report
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
                    JasperViewer.viewReport(jasperPrint, false); // 'false' prevents the app from closing when you close the report window

                } catch (JRException e) {
                    new Alert(Alert.AlertType.ERROR, "Failed to load receipt: " + e.getMessage()).show();
                }
                // --- JASPER REPORT GENERATION END ---
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!").show();
                cartList.clear();
                tblCart.refresh();
                calculateNetTotal();
                generateOrderId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order Placement Failed!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        }
    }
}