package com.inventory.pl_project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button productModuleBtn;

    @FXML
    private Button adminModuleBtn;

    @FXML
    private Button clientModuleBtn;

    @FXML
    private void initialize() {
        // Initialization code if needed
    }

    @FXML
    private void openProductModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/product.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Product Module");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Admin Module");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openClientModule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Client Module");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
