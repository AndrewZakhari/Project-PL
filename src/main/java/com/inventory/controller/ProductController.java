package com.inventory.controller;

import com.inventory.App;
import com.inventory.model.Product;
import com.inventory.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ProductController {

    private final ProductService productService = new ProductService();

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private DatePicker expirationDatePicker;
    
    @FXML private TextField searchField; // New Search Field
    
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, LocalDate> colExp;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colExp.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        refreshTable();

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                categoryField.setText(newSelection.getCategory());
                quantityField.setText(String.valueOf(newSelection.getQuantity()));
                priceField.setText(String.valueOf(newSelection.getPrice()));
                expirationDatePicker.setValue(newSelection.getExpirationDate());
            }
        });
    }

    private void refreshTable() {
        List<Product> products = productService.getAllProducts();
        ObservableList<Product> data = FXCollections.observableArrayList(products);
        productTable.setItems(data);
    }

    @FXML
    private void handleAddProduct() {
        try {
            String name = nameField.getText();
            String category = categoryField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            LocalDate expirationDate = expirationDatePicker.getValue();

            Product product = new Product(name, category, expirationDate, quantity, price);
            productService.addProduct(product);
            showAlert("Success", "Product " + name + " added successfully");
            clearFields();
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateProduct() {
        try {
            String name = nameField.getText();
            String category = categoryField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            LocalDate expirationDate = expirationDatePicker.getValue();

            Product newProduct = new Product(name, category, expirationDate, quantity, price);
            productService.updateProduct(name, newProduct);
            showAlert("Success", "Product updated successfully!");
            clearFields();
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", "Could not update product: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProduct() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            showAlert("Error", "Please enter a product name to delete.");
            return;
        }
        productService.deleteProduct(name);
        showAlert("Success", "Product deleted successfully!");
        clearFields();
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            refreshTable();
            return;
        }
        List<Product> results = productService.searchByName(query);
        productTable.setItems(FXCollections.observableArrayList(results));
    }
    
    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("view/main_menu");
    }

    private void clearFields() {
        nameField.clear();
        categoryField.clear();
        quantityField.clear();
        priceField.clear();
        expirationDatePicker.setValue(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
