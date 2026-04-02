package edu.icet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Starter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Points to the Dash.fxml we just created inside resources/view
        URL resource = getClass().getResource("/view/Dash.fxml");

        if (resource == null) {
            throw new IllegalStateException("Cannot find Dash.fxml. Make sure it is inside src/main/resources/view/");
        }

        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"))));
        primaryStage.setTitle("NextPharma - Login");
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false); // Prevents the layout from breaking if they try to resize the window
        primaryStage.show();
    }
}