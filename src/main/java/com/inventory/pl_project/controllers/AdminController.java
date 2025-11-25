package com.inventory.pl_project.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.inventory.pl_project.models.Category;
import com.inventory.pl_project.models.Offer;
import com.inventory.pl_project.models.Product;
import com.inventory.pl_project.models.Supplier;
import com.inventory.pl_project.services.CategoryService;
import com.inventory.pl_project.services.ProductService;
import com.inventory.pl_project.services.ReportService;
import com.inventory.pl_project.services.SupplierService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {

    // Category Section
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, String> catIdColumn;
    @FXML private TableColumn<Category, String> catNameColumn;
    @FXML private TableColumn<Category, String> catDescColumn;
    @FXML private TextField categoryNameField;
    @FXML private TextArea categoryDescField;

    // Supplier Section
    @FXML private TableView<Supplier> supplierTable;
    @FXML private TableColumn<Supplier, String> suppIdColumn;
    @FXML private TableColumn<Supplier, String> suppNameColumn;
    @FXML private TableColumn<Supplier, String> suppEmailColumn;
    @FXML private TableColumn<Supplier, String> suppPhoneColumn;
    @FXML private TextField supplierNameField;
    @FXML private TextField supplierContactField;
    @FXML private TextField supplierEmailField;
    @FXML private TextField supplierPhoneField;
    @FXML private TextArea supplierAddressField;

    // Offer Section
    @FXML private ComboBox<String> offerProductComboBox;
    @FXML private TextField discountField;
    @FXML private DatePicker offerStartDate;
    @FXML private DatePicker offerEndDate;

    // Reports Section
    @FXML private TextArea reportTextArea;
    @FXML private ComboBox<String> reportTypeComboBox;
    @FXML private DatePicker reportStartDate;
    @FXML private DatePicker reportEndDate;

    private CategoryService categoryService;
    private SupplierService supplierService;
    private ProductService productService;
    private ReportService reportService;
    private ObservableList<Category> categoryList;
    private ObservableList<Supplier> supplierList;
    private Category selectedCategory;
    private Supplier selectedSupplier;

    @FXML
    private void initialize() {
        categoryService = new CategoryService();
        supplierService = new SupplierService();
        productService = new ProductService();
        reportService = new ReportService();

        setupCategoryTable();
        setupSupplierTable();
        loadCategories();
        loadSuppliers();
        loadProductsForOffers();
        setupReportTypes();
    }

    // ============ CATEGORY METHODS ============

    private void setupCategoryTable() {
        catIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        catDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        categoryTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedCategory = newValue;
                        categoryNameField.setText(newValue.getName());
                        categoryDescField.setText(newValue.getDescription());
                    }
                });
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryList = FXCollections.observableArrayList(categories);
        categoryTable.setItems(categoryList);
    }

    @FXML
    private void handleAddCategory() {
        if (categoryNameField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter category name!", Alert.AlertType.ERROR);
            return;
        }

        Category category = new Category(
                UUID.randomUUID().toString(),
                categoryNameField.getText(),
                categoryDescField.getText()
        );

        categoryService.addCategory(category);
        loadCategories();
        clearCategoryFields();
        showAlert("Success", "Category added successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleUpdateCategory() {
        if (selectedCategory == null) {
            showAlert("Warning", "Please select a category to update!", Alert.AlertType.WARNING);
            return;
        }

        selectedCategory.setName(categoryNameField.getText());
        selectedCategory.setDescription(categoryDescField.getText());

        categoryService.updateCategory(selectedCategory);
        categoryTable.refresh();
        clearCategoryFields();
        showAlert("Success", "Category updated successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleDeleteCategory() {
        if (selectedCategory == null) {
            showAlert("Warning", "Please select a category to delete!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Category");
        confirmAlert.setContentText("Are you sure you want to delete this category?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            categoryService.deleteCategory(selectedCategory.getId());
            loadCategories();
            clearCategoryFields();
            selectedCategory = null;
            showAlert("Success", "Category deleted successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void clearCategoryFields() {
        categoryNameField.clear();
        categoryDescField.clear();
        selectedCategory = null;
    }

    // ============ SUPPLIER METHODS ============

    private void setupSupplierTable() {
        suppIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        suppNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        suppEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        suppPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        supplierTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedSupplier = newValue;
                        supplierNameField.setText(newValue.getName());
                        supplierContactField.setText(newValue.getContactPerson());
                        supplierEmailField.setText(newValue.getEmail());
                        supplierPhoneField.setText(newValue.getPhone());
                        supplierAddressField.setText(newValue.getAddress());
                    }
                });
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        supplierList = FXCollections.observableArrayList(suppliers);
        supplierTable.setItems(supplierList);
    }

    @FXML
    private void handleAddSupplier() {
        if (supplierNameField.getText().isEmpty() || supplierEmailField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in required fields!", Alert.AlertType.ERROR);
            return;
        }

        Supplier supplier = new Supplier(
                UUID.randomUUID().toString(),
                supplierNameField.getText(),
                supplierContactField.getText(),
                supplierEmailField.getText(),
                supplierPhoneField.getText(),
                supplierAddressField.getText()
        );

        supplierService.addSupplier(supplier);
        loadSuppliers();
        clearSupplierFields();
        showAlert("Success", "Supplier added successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleUpdateSupplier() {
        if (selectedSupplier == null) {
            showAlert("Warning", "Please select a supplier to update!", Alert.AlertType.WARNING);
            return;
        }

        selectedSupplier.setName(supplierNameField.getText());
        selectedSupplier.setContactPerson(supplierContactField.getText());
        selectedSupplier.setEmail(supplierEmailField.getText());
        selectedSupplier.setPhone(supplierPhoneField.getText());
        selectedSupplier.setAddress(supplierAddressField.getText());

        supplierService.updateSupplier(selectedSupplier);
        supplierTable.refresh();
        clearSupplierFields();
        showAlert("Success", "Supplier updated successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleDeleteSupplier() {
        if (selectedSupplier == null) {
            showAlert("Warning", "Please select a supplier to delete!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Supplier");
        confirmAlert.setContentText("Are you sure you want to delete this supplier?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            supplierService.deleteSupplier(selectedSupplier.getId());
            loadSuppliers();
            clearSupplierFields();
            selectedSupplier = null;
            showAlert("Success", "Supplier deleted successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void clearSupplierFields() {
        supplierNameField.clear();
        supplierContactField.clear();
        supplierEmailField.clear();
        supplierPhoneField.clear();
        supplierAddressField.clear();
        selectedSupplier = null;
    }

    // ============ OFFER METHODS ============

    private void loadProductsForOffers() {
        List<Product> products = productService.getAllProducts();
        ObservableList<String> productNames = FXCollections.observableArrayList();
        for (Product product : products) {
            productNames.add(product.getName());
        }
        offerProductComboBox.setItems(productNames);
    }

    @FXML
    private void handleAddOffer() {
        if (offerProductComboBox.getValue() == null || discountField.getText().isEmpty() ||
                offerStartDate.getValue() == null || offerEndDate.getValue() == null) {
            showAlert("Validation Error", "Please fill in all offer fields!", Alert.AlertType.ERROR);
            return;
        }

        try {
            String productName = offerProductComboBox.getValue();
            Product product = productService.searchByName(productName).stream()
                    .filter(p -> p.getName().equals(productName))
                    .findFirst()
                    .orElse(null);

            if (product == null) {
                showAlert("Error", "Product not found!", Alert.AlertType.ERROR);
                return;
            }

            double discount = Double.parseDouble(discountField.getText());
            if (discount < 0 || discount > 100) {
                showAlert("Error", "Discount must be between 0 and 100!", Alert.AlertType.ERROR);
                return;
            }

            Offer offer = new Offer(
                    UUID.randomUUID().toString(),
                    product.getId(),
                    discount,
                    offerStartDate.getValue(),
                    offerEndDate.getValue()
            );

            product.getOffers().add(offer);
            productService.updateProduct(product);

            clearOfferFields();
            showAlert("Success", "Offer added successfully!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid discount format!", Alert.AlertType.ERROR);
        }
    }

    private void clearOfferFields() {
        offerProductComboBox.setValue(null);
        discountField.clear();
        offerStartDate.setValue(null);
        offerEndDate.setValue(null);
    }

    // ============ REPORT METHODS ============

    private void setupReportTypes() {
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
                "Product Report", "Category Report", "Sales Report", "Profit Report");
        reportTypeComboBox.setItems(reportTypes);
        reportTypeComboBox.setValue("Product Report");
    }

    @FXML
    private void handleGenerateReport() {
        String reportType = reportTypeComboBox.getValue();
        StringBuilder report = new StringBuilder();

        switch (reportType) {
            case "Product Report":
                Map<String, Object> productReport = reportService.generateProductReport();
                report.append("=== PRODUCT REPORT ===\n\n");
                report.append("Total Products: ").append(productReport.get("totalProducts")).append("\n");
                report.append("Total Inventory Value: $").append(
                        String.format("%.2f", productReport.get("totalValue"))).append("\n");
                report.append("Low Stock Products: ").append(productReport.get("lowStockProducts")).append("\n");
                report.append("Expired Products: ").append(productReport.get("expiredProducts")).append("\n\n");

                Map<String, Integer> categoryDist = reportService.getProductsByCategory();
                report.append("--- Products by Category ---\n");
                categoryDist.forEach((cat, count) ->
                        report.append(cat).append(": ").append(count).append("\n"));
                break;

            case "Category Report":
                Map<String, Object> categoryReport = reportService.generateCategoryReport();
                report.append("=== CATEGORY REPORT ===\n\n");
                report.append("Total Categories: ").append(categoryReport.get("totalCategories")).append("\n\n");

                @SuppressWarnings("unchecked")
                Map<String, Integer> distribution = (Map<String, Integer>) categoryReport.get("productDistribution");
                report.append("--- Product Distribution ---\n");
                distribution.forEach((cat, count) ->
                        report.append(cat).append(": ").append(count).append(" products\n"));
                break;

            case "Sales Report":
                LocalDateTime startSales = reportStartDate.getValue() != null ?
                        reportStartDate.getValue().atStartOfDay() : LocalDateTime.now().minusMonths(1);
                LocalDateTime endSales = reportEndDate.getValue() != null ?
                        reportEndDate.getValue().atTime(23, 59) : LocalDateTime.now();

                Map<String, Object> salesReport = reportService.generateSalesReport(startSales, endSales);
                report.append("=== SALES REPORT ===\n\n");
                report.append("Period: ").append(startSales.toLocalDate()).append(" to ")
                        .append(endSales.toLocalDate()).append("\n\n");
                report.append("Total Orders: ").append(salesReport.get("totalOrders")).append("\n");
                report.append("Total Revenue: $").append(
                        String.format("%.2f", salesReport.get("totalRevenue"))).append("\n");
                report.append("Average Order Value: $").append(
                        String.format("%.2f", salesReport.get("averageOrderValue"))).append("\n");
                report.append("Completed Orders: ").append(salesReport.get("completedOrders")).append("\n");
                break;

            case "Profit Report":
                LocalDateTime startProfit = reportStartDate.getValue() != null ?
                        reportStartDate.getValue().atStartOfDay() : LocalDateTime.now().minusMonths(1);
                LocalDateTime endProfit = reportEndDate.getValue() != null ?
                        reportEndDate.getValue().atTime(23, 59) : LocalDateTime.now();

                Map<String, Object> profitReport = reportService.generateProfitReport(startProfit, endProfit);
                report.append("=== PROFIT REPORT ===\n\n");
                report.append("Period: ").append(startProfit.toLocalDate()).append(" to ")
                        .append(endProfit.toLocalDate()).append("\n\n");
                report.append("Total Revenue: $").append(
                        String.format("%.2f", profitReport.get("totalRevenue"))).append("\n");
                report.append("Total Cost: $").append(
                        String.format("%.2f", profitReport.get("totalCost"))).append("\n");
                report.append("Gross Profit: $").append(
                        String.format("%.2f", profitReport.get("grossProfit"))).append("\n");
                report.append("Profit Margin: ").append(
                        String.format("%.2f", profitReport.get("profitMargin"))).append("%\n");
                break;
        }

        reportTextArea.setText(report.toString());
    }

    @FXML
    private void handleClearReport() {
        reportTextArea.clear();
        reportStartDate.setValue(null);
        reportEndDate.setValue(null);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}