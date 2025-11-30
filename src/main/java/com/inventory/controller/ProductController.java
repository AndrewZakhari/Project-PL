package com.inventory.controller;

import com.inventory.App;
import com.inventory.service.ProductService;
import javafx.fxml.FXML;
import java.io.IOException;

public class ProductController {

    private ProductService productService = new ProductService();

    @FXML
    private void handleAddProduct() {
        // Call productService.addProduct()
    }

    @FXML
    private void handleUpdateProduct() {
        // Call productService.updateProduct()
    }

    @FXML
    private void handleDeleteProduct() {
        // Call productService.deleteProduct()
    }

    @FXML
    private void handleSearch() {
        // Call productService.searchByName/Date/Category
    }

    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("view/main_menu");
    }
}
