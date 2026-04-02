package edu.icet.controller;

import edu.icet.db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        try {
            // 1. Secure Database Check
            String sql = "SELECT * FROM Users WHERE username=? AND password=?";
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1, txtUsername.getText());
            pstm.setString(2, txtPassword.getText());
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                // 2. Success! Load the Main Dashboard
                Stage dashboardStage = new Stage();
                dashboardStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Dash.fxml"))));
                dashboardStage.setTitle("NextPharma POS - Dashboard");
                dashboardStage.setResizable(false);
                dashboardStage.show();

                // 3. Close the Login Window
                Stage currentStage = (Stage) txtUsername.getScene().getWindow();
                currentStage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database Connection Error!").show();
        }
    }
}