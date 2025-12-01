package com.inventory.controller;

import com.inventory.App;
import com.inventory.model.Category;
import com.inventory.model.Offer;
import com.inventory.model.Supplier;
import com.inventory.service.AdminService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;

public class AdminController {

    private final AdminService adminService = new AdminService();

    @FXML private TextField categoryNameField;
    @FXML private TextField supplierNameField;
    @FXML private TextField supplierContactField;
    @FXML private TextField offerDetailsField;
    @FXML private TextArea reportArea;
    
    @FXML private TabPane adminTabPane;
    @FXML private Tab reportsTab;
    
    @FXML private ListView<Category> categoryList;
    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, String> colSupName;
    @FXML private TableColumn<Supplier, String> colSupContact;

    @FXML
    public void initialize() {
        colSupName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupContact.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        
        refreshData();
        
        categoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) categoryNameField.setText(newVal.getName());
        });
        
        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) {
                supplierNameField.setText(newVal.getName());
                supplierContactField.setText(newVal.getContactInfo());
            }
        });
    }
    
    private void refreshData() {
        List<Category> categories = adminService.getAllCategories();
        categoryList.setItems(FXCollections.observableArrayList(categories));
        
        List<Supplier> suppliers = adminService.getAllSuppliers();
        supplierTable.setItems(FXCollections.observableArrayList(suppliers));
    }

    @FXML
    private void handleAddCategory() {
        String name = categoryNameField.getText();
        if (!name.isEmpty()) {
            adminService.addCategory(new Category(name));
            showAlert("Success", "Category added.");
            categoryNameField.clear();
            refreshData();
        }
    }

    @FXML
    private void handleAddSupplier() {
        String name = supplierNameField.getText();
        String contact = supplierContactField.getText();
        if (!name.isEmpty()) {
            adminService.addSupplier(new Supplier(name, contact));
            showAlert("Success", "Supplier added.");
            supplierNameField.clear();
            supplierContactField.clear();
            refreshData();
        }
    }

    @FXML
    private void handleGenerateReports() {
        String prodReport = adminService.generateProductReport();
        String profitReport = adminService.generateProfitReport();
        String catReport = adminService.generateCategoryReport();
        
        reportArea.setText(prodReport + "\n\n" + catReport + "\n\n" + profitReport);
        
        // Automatically switch to the reports tab
        adminTabPane.getSelectionModel().select(reportsTab);
    }

    @FXML
    private void handleAddOffer() {
        String details = offerDetailsField.getText();
        if(!details.isEmpty()){
             showAlert("Info", "Offer feature requires Offer model update to match UI inputs.");
        }
    }

    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("view/main_menu");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
