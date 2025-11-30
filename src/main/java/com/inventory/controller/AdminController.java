package com.inventory.controller;

import com.inventory.App;
import com.inventory.service.AdminService;
import javafx.fxml.FXML;
import java.io.IOException;

public class AdminController {

    private AdminService adminService = new AdminService();

    @FXML
    private void handleAddCategory() {
        // Call adminService.addCategory()
    }

    @FXML
    private void handleAddSupplier() {
        // Call adminService.addSupplier()
    }

    @FXML
    private void handleGenerateReports() {
        // Call adminService.generateProductReport()
    }

    @FXML
    private void handleAddOffer() {
        // Call adminService.addOffer()
    }

    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("view/main_menu");
    }
}
