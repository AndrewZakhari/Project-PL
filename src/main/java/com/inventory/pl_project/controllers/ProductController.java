package com.inventory.pl_project.controllers;

import com.inventory.pl_project.models.Product;
import com.inventory.pl_project.models.Category;
import com.inventory.pl_project.services.ProductService;
import com.inventory.pl_project.services.CategoryService;
import com.inventory.pl_project.services.NotificationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, LocalDate> expirationColumn;

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private DatePicker expirationDatePicker;
    @FXML private TextField supplierField;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;
    @FXML private DatePicker searchStartDate;
    @FXML private DatePicker searchEndDate;
    @FXML private Button checkExpirationBtn;

    private ProductService productService;
    private CategoryService categoryService;
    private NotificationService notificationService;
    private ObservableList<Product> productList;
    private Product selectedProduct;

    @FXML
    private void initialize() {
        productService = new ProductService();
        categoryService = new CategoryService();
        notificationService = new NotificationService();

        setupTableColumns();
        loadCategories();
        loadProducts();
        setupSearchTypes();
        setupTableSelection();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(cellData -> {
            Category category = categoryService.getCategoryById(cellData.getValue().getCategoryId());
            return new javafx.beans.property.SimpleStringProperty(
                    category != null ? category.getName() : "Unknown");
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
    }

    private void setupTableSelection() {
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedProduct = newValue;
                        populateFields(newValue);
                    }
                });
    }

    private void populateFields(Product product) {
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());

        Category category = categoryService.getCategoryById(product.getCategoryId());
        if (category != null) {
            categoryComboBox.setValue(category.getName());
        }

        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        expirationDatePicker.setValue(product.getExpirationDate());
        supplierField.setText(product.getSupplierId());
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        categoryComboBox.setItems(categoryNames);
    }

    private void loadProducts() {
        List<Product> products = productService.getAllProducts();
        productList = FXCollections.observableArrayList(products);
        productTable.setItems(productList);
    }

    private void setupSearchTypes() {
        ObservableList<String> searchTypes = FXCollections.observableArrayList(
                "Name", "Category", "Date Range");
        searchTypeComboBox.setItems(searchTypes);
        searchTypeComboBox.setValue("Name");

        searchTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Date Range".equals(newVal)) {
                searchStartDate.setVisible(true);
                searchEndDate.setVisible(true);
                searchField.setVisible(false);
            } else {
                searchStartDate.setVisible(false);
                searchEndDate.setVisible(false);
                searchField.setVisible(true);
            }
        });
    }

    @FXML
    private void handleAddProduct() {
        try {
            if (!validateInput()) {
                return;
            }

            String categoryId = getCategoryIdByName(categoryComboBox.getValue());

            Product product = new Product(
                    UUID.randomUUID().toString(),
                    nameField.getText(),
                    descriptionField.getText(),
                    categoryId,
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(quantityField.getText()),
                    expirationDatePicker.getValue(),
                    supplierField.getText()
            );

            productService.addProduct(product);
            loadProducts();
            clearFields();
            showAlert("Success", "Product added successfully!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price or quantity format!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add product: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateProduct() {
        if (selectedProduct == null) {
            showAlert("Warning", "Please select a product to update!", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (!validateInput()) {
                return;
            }

            String categoryId = getCategoryIdByName(categoryComboBox.getValue());

            selectedProduct.setName(nameField.getText());
            selectedProduct.setDescription(descriptionField.getText());
            selectedProduct.setCategoryId(categoryId);
            selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
            selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedProduct.setExpirationDate(expirationDatePicker.getValue());
            selectedProduct.setSupplierId(supplierField.getText());

            productService.updateProduct(selectedProduct);
            loadProducts();
            clearFields();
            showAlert("Success", "Product updated successfully!", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price or quantity format!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update product: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteProduct() {
        if (selectedProduct == null) {
            showAlert("Warning", "Please select a product to delete!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Product");
        confirmAlert.setContentText("Are you sure you want to delete this product?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            productService.deleteProduct(selectedProduct.getId());
            loadProducts();
            clearFields();
            selectedProduct = null;
            showAlert("Success", "Product deleted successfully!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void handleSearch() {
        String searchType = searchTypeComboBox.getValue();
        List<Product> results;

        switch (searchType) {
            case "Name":
                String name = searchField.getText();
                results = productService.searchByName(name);
                break;

            case "Category":
                String categoryId = getCategoryIdByName(searchField.getText());
                results = productService.searchByCategory(categoryId);
                break;

            case "Date Range":
                LocalDate start = searchStartDate.getValue();
                LocalDate end = searchEndDate.getValue();
                if (start == null || end == null) {
                    showAlert("Warning", "Please select both start and end dates!", Alert.AlertType.WARNING);
                    return;
                }
                results = productService.searchByDateRange(start, end);
                break;

            default:
                results = productService.getAllProducts();
        }

        productList = FXCollections.observableArrayList(results);
        productTable.setItems(productList);
    }

    @FXML
    private void handleCheckExpiration() {
        List<Product> expiringProducts = productService.getProductsNearingExpiration(7);

        if (expiringProducts.isEmpty()) {
            showAlert("Information", "No products are expiring within 7 days.", Alert.AlertType.INFORMATION);
        } else {
            StringBuilder message = new StringBuilder("Products expiring soon:\n\n");
            for (Product product : expiringProducts) {
                long daysUntilExpiration = java.time.temporal.ChronoUnit.DAYS
                        .between(LocalDate.now(), product.getExpirationDate());
                message.append("- ").append(product.getName())
                        .append(" (").append(daysUntilExpiration).append(" days)\n");
            }
            showAlert("Expiration Warning", message.toString(), Alert.AlertType.WARNING);
        }

        // Trigger notification service
        notificationService.checkAndNotify();
    }

    @FXML
    private void handleReset() {
        loadProducts();
        searchField.clear();
    }

    private void clearFields() {
        nameField.clear();
        descriptionField.clear();
        categoryComboBox.setValue(null);
        priceField.clear();
        quantityField.clear();
        expirationDatePicker.setValue(null);
        supplierField.clear();
        selectedProduct = null;
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || categoryComboBox.getValue() == null ||
                priceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields!", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private String getCategoryIdByName(String categoryName) {
        List<Category> categories = categoryService.getAllCategories();
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category.getId();
            }
        }
        return null;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
