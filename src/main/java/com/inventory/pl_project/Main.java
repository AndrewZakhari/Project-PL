package com.inventory.pl_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Try to load FXML
        URL fxmlLocation = Main.class.getResource("/fxml/main.fxml");

        if (fxmlLocation == null) {
            System.err.println("Cannot find main.fxml!");
            System.err.println("Looking in: " + Main.class.getResource("/"));

            // Create a simple UI without FXML as fallback
            createSimpleUI(primaryStage);
            return;
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();
    }

    private void createSimpleUI(Stage primaryStage) {
        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(20);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setStyle("-fx-padding: 50;");

        javafx.scene.control.Label label = new javafx.scene.control.Label("Inventory Management System");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        javafx.scene.control.Button productBtn = new javafx.scene.control.Button("Product Module");
        javafx.scene.control.Button adminBtn = new javafx.scene.control.Button("Admin Module");
        javafx.scene.control.Button clientBtn = new javafx.scene.control.Button("Client Module");

        productBtn.setPrefWidth(200);
        adminBtn.setPrefWidth(200);
        clientBtn.setPrefWidth(200);

        productBtn.setOnAction(e -> openProductModule());
        adminBtn.setOnAction(e -> openAdminModule());
        clientBtn.setOnAction(e -> openClientModule());

        root.getChildren().addAll(label, productBtn, adminBtn, clientBtn);

        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private void openProductModule() {
        try {
            Stage stage = new Stage();
            URL fxmlLocation = Main.class.getResource("/fxml/product.fxml");
            if (fxmlLocation != null) {
                Parent root = FXMLLoader.load(fxmlLocation);
                stage.setTitle("Product Module");
                stage.setScene(new Scene(root, 1000, 600));
                stage.show();
            } else {
                showAlert("Error", "Cannot find product.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Product Module: " + e.getMessage());
        }
    }

    private void openAdminModule() {
        try {
            Stage stage = new Stage();
            URL fxmlLocation = Main.class.getResource("/fxml/admin.fxml");
            if (fxmlLocation != null) {
                Parent root = FXMLLoader.load(fxmlLocation);
                stage.setTitle("Admin Module");
                stage.setScene(new Scene(root, 1000, 600));
                stage.show();
            } else {
                showAlert("Error", "Cannot find admin.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Admin Module: " + e.getMessage());
        }
    }

    private void openClientModule() {
        try {
            Stage stage = new Stage();
            URL fxmlLocation = Main.class.getResource("/fxml/client.fxml");
            if (fxmlLocation != null) {
                Parent root = FXMLLoader.load(fxmlLocation);
                stage.setTitle("Client Module");
                stage.setScene(new Scene(root, 1000, 600));
                stage.show();
            } else {
                showAlert("Error", "Cannot find client.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Client Module: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}