package com.inventory.pl_project.controllers;

import com.inventory.pl_project.models.Client;
import com.inventory.pl_project.models.Order;
import com.inventory.pl_project.models.Product;
import com.inventory.pl_project.services.ClientService;
import com.inventory.pl_project.services.OrderService;
import com.inventory.pl_project.services.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;
import java.util.*;

public class ClientController {

    // Client Registration Section
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> clientIdColumn;
    @FXML private TableColumn<Client, String> clientNameColumn;
    @FXML private TableColumn<Client, String> clientEmailColumn;
    @FXML private TableColumn<Client, String> clientPhoneColumn;
    @FXML private TextField clientNameField;
    @FXML private TextField clientEmailField;
    @FXML private TextField clientPhoneField;
    @FXML private TextField clientAddressField;

    // Order Section
    @FXML private ComboBox<String> orderClientComboBox;
    @FXML private ComboBox<String> orderProductComboBox;
    @FXML private TextField orderQuantityField;
    @FXML private ListView<String> orderItemsList;
    @FXML private Label totalAmountLabel;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> orderIdColumn;
    @FXML private TableColumn<Order, String> orderClientColumn;
    @FXML private TableColumn<Order, Double> orderAmountColumn;
    @FXML private TableColumn<Order, LocalDateTime> orderDateColumn;
    @FXML private TableColumn<Order, String> orderStatusColumn;

    // Order Report Section
    @FXML private ComboBox<String> reportClientComboBox;
    @FXML private TextArea orderReportArea;

    private ClientService clientService;
    private OrderService orderService;
    private ProductService productService;
    private ObservableList<Client> clientList;
    private ObservableList<Order> orderList;
    private Client selectedClient;
    private Order selectedOrder;
    private List<Order.OrderItem> currentOrderItems;
    private Map<String, Product> productMap;

    @FXML
    private void initialize() {
        clientService = new ClientService();
        orderService = new OrderService();
        productService = new ProductService();
        currentOrderItems = new ArrayList<>();
        productMap = new HashMap<>();

        setupClientTable();
        setupOrderTable();
        loadClients();
        loadOrders();
        loadProductsForOrders();
        loadClientsForOrders();
    }

    // ============ CLIENT METHODS ============

    private void setupClientTable() {
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        clientTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selectedClient = newValue;
                        populateClientFields(newValue);
                    }
                });
    }

    private void populateClientFields(Client client) {
        clientNameField.setText(client.getName());
        clientEmailField.setText(client.getEmail());
        clientPhoneField.setText(client.getPhone());
        clientAddressField.setText(client.getAddress());
    }

    private void loadClients() {
        List<Client> clients = clientService.getAllClients();
        clientList = FXCollections.observableArrayList(clients);
        clientTable.setItems(clientList);
    }

    @FXML
    private void handleRegisterClient() {
        if (!validateClientInput()) {
            return;
        }

        try {
            Client client = new Client(
                    UUID.randomUUID().toString(),
                    clientNameField.getText(),
                    clientEmailField.getText(),
                    clientPhoneField.getText(),
                    clientAddressField.getText()
            );

            clientService.registerClient(client);
            loadClients();
            loadClientsForOrders();
            clearClientFields();
            showAlert("Success", "Client registered successfully! Welcome email sent.",
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to register client: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdateClient() {
        if (selectedClient == null) {
            showAlert("Warning", "Please select a client to update!", Alert.AlertType.WARNING);
            return;
        }

        if (!validateClientInput()) {
            return;
        }

        selectedClient.setName(clientNameField.getText());
        selectedClient.setEmail(clientEmailField.getText());
        selectedClient.setPhone(clientPhoneField.getText());
        selectedClient.setAddress(clientAddressField.getText());

        clientService.updateClient(selectedClient);
        loadClients();
        loadClientsForOrders();
        clearClientFields();
        showAlert("Success", "Client updated successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleDeleteClient() {
        if (selectedClient == null) {
            showAlert("Warning", "Please select a client to delete!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Client");
        confirmAlert.setContentText("Are you sure you want to delete this client?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            clientService.deleteClient(selectedClient.getId());
            loadClients();
            loadClientsForOrders();
            clearClientFields();
            selectedClient = null;
            showAlert("Success", "Client deleted successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void clearClientFields() {
        clientNameField.clear();
        clientEmailField.clear();
        clientPhoneField.clear();
        clientAddressField.clear();
        selectedClient = null;
    }

    private boolean validateClientInput() {
        if (clientNameField.getText().isEmpty() || clientEmailField.getText().isEmpty()) {
            showAlert("Validation Error", "Name and Email are required!", Alert.AlertType.ERROR);
            return false;
        }

        if (!clientEmailField.getText().contains("@")) {
            showAlert("Validation Error", "Please enter a valid email address!",
                    Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // ============ ORDER METHODS ============

    private void setupOrderTable() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderClientColumn.setCellValueFactory(cellData -> {
            Client client = clientService.getClientById(cellData.getValue().getClientId());
            return new javafx.beans.property.SimpleStringProperty(
                    client != null ? client.getName() : "Unknown");
        });
        orderAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        orderTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selectedOrder = newValue;
                });
    }

    private void loadOrders() {
        List<Order> orders = orderService.getAllOrders();
        orderList = FXCollections.observableArrayList(orders);
        orderTable.setItems(orderList);
    }

    private void loadProductsForOrders() {
        List<Product> products = productService.getAllProducts();
        ObservableList<String> productNames = FXCollections.observableArrayList();

        for (Product product : products) {
            productNames.add(product.getName());
            productMap.put(product.getName(), product);
        }

        orderProductComboBox.setItems(productNames);
    }

    private void loadClientsForOrders() {
        List<Client> clients = clientService.getAllClients();
        ObservableList<String> clientNames = FXCollections.observableArrayList();

        for (Client client : clients) {
            clientNames.add(client.getName());
        }

        orderClientComboBox.setItems(clientNames);
        reportClientComboBox.setItems(clientNames);
    }

    @FXML
    private void handleAddItemToOrder() {
        if (orderProductComboBox.getValue() == null || orderQuantityField.getText().isEmpty()) {
            showAlert("Validation Error", "Please select a product and enter quantity!",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            String productName = orderProductComboBox.getValue();
            int quantity = Integer.parseInt(orderQuantityField.getText());

            Product product = productMap.get(productName);
            if (product == null) {
                showAlert("Error", "Product not found!", Alert.AlertType.ERROR);
                return;
            }

            if (quantity > product.getQuantity()) {
                showAlert("Error", "Insufficient stock! Available: " + product.getQuantity(),
                        Alert.AlertType.ERROR);
                return;
            }

            Order.OrderItem item = new Order.OrderItem(
                    product.getId(),
                    quantity,
                    product.getPrice()
            );

            currentOrderItems.add(item);

            String itemDisplay = String.format("%s x%d @ $%.2f = $%.2f",
                    productName, quantity, product.getPrice(), item.getSubtotal());
            orderItemsList.getItems().add(itemDisplay);

            updateTotalAmount();
            orderProductComboBox.setValue(null);
            orderQuantityField.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid quantity format!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRemoveItemFromOrder() {
        int selectedIndex = orderItemsList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            currentOrderItems.remove(selectedIndex);
            orderItemsList.getItems().remove(selectedIndex);
            updateTotalAmount();
        } else {
            showAlert("Warning", "Please select an item to remove!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleCreateOrder() {
        if (orderClientComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select a client!", Alert.AlertType.ERROR);
            return;
        }

        if (currentOrderItems.isEmpty()) {
            showAlert("Validation Error", "Please add items to the order!", Alert.AlertType.ERROR);
            return;
        }

        try {
            String clientName = orderClientComboBox.getValue();
            Client client = clientService.getAllClients().stream()
                    .filter(c -> c.getName().equals(clientName))
                    .findFirst()
                    .orElse(null);

            if (client == null) {
                showAlert("Error", "Client not found!", Alert.AlertType.ERROR);
                return;
            }

            Order order = orderService.createOrder(client.getId(), currentOrderItems);

            loadOrders();
            clearOrderFields();

            showAlert("Success",
                    "Order created successfully!\nOrder ID: " + order.getId() +
                            "\nNotification email sent to client.",
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Error", "Failed to create order: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleGenerateInvoice() {
        if (selectedOrder == null) {
            showAlert("Warning", "Please select an order to generate invoice!",
                    Alert.AlertType.WARNING);
            return;
        }

        String invoice = orderService.generateInvoice(selectedOrder.getId());

        Alert invoiceAlert = new Alert(Alert.AlertType.INFORMATION);
        invoiceAlert.setTitle("Order Invoice");
        invoiceAlert.setHeaderText("Invoice for Order: " + selectedOrder.getId());

        TextArea textArea = new TextArea(invoice);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(600, 400);

        invoiceAlert.getDialogPane().setContent(textArea);
        invoiceAlert.showAndWait();
    }

    private void updateTotalAmount() {
        double total = currentOrderItems.stream()
                .mapToDouble(Order.OrderItem::getSubtotal)
                .sum();
        totalAmountLabel.setText(String.format("Total: $%.2f", total));
    }

    private void clearOrderFields() {
        orderClientComboBox.setValue(null);
        orderProductComboBox.setValue(null);
        orderQuantityField.clear();
        orderItemsList.getItems().clear();
        currentOrderItems.clear();
        totalAmountLabel.setText("Total: $0.00");
    }

    // ============ ORDER REPORT METHODS ============

    @FXML
    private void handleGenerateOrderReport() {
        if (reportClientComboBox.getValue() == null) {
            showAlert("Warning", "Please select a client!", Alert.AlertType.WARNING);
            return;
        }

        String clientName = reportClientComboBox.getValue();
        Client client = clientService.getAllClients().stream()
                .filter(c -> c.getName().equals(clientName))
                .findFirst()
                .orElse(null);

        if (client == null) {
            showAlert("Error", "Client not found!", Alert.AlertType.ERROR);
            return;
        }

        List<Order> clientOrders = orderService.getOrdersByClient(client.getId());

        StringBuilder report = new StringBuilder();
        report.append("=== ORDER REPORT ===\n\n");
        report.append("Client: ").append(client.getName()).append("\n");
        report.append("Email: ").append(client.getEmail()).append("\n");
        report.append("Phone: ").append(client.getPhone()).append("\n\n");
        report.append("Total Orders: ").append(clientOrders.size()).append("\n");

        double totalSpent = clientOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        report.append("Total Spent: $").append(String.format("%.2f", totalSpent)).append("\n\n");

        report.append("--- ORDER HISTORY ---\n\n");

        for (Order order : clientOrders) {
            report.append("Order ID: ").append(order.getId()).append("\n");
            report.append("Date: ").append(order.getOrderDate()).append("\n");
            report.append("Status: ").append(order.getStatus()).append("\n");
            report.append("Amount: $").append(String.format("%.2f", order.getTotalAmount())).append("\n");

            report.append("Items:\n");
            for (Order.OrderItem item : order.getItems()) {
                Product product = productService.getProductById(item.getProductId());
                String productName = product != null ? product.getName() : "Unknown";
                report.append("  - ").append(productName)
                        .append(" x").append(item.getQuantity())
                        .append(" @ $").append(String.format("%.2f", item.getPricePerUnit()))
                        .append(" = $").append(String.format("%.2f", item.getSubtotal()))
                        .append("\n");
            }
            report.append("\n");
        }

        orderReportArea.setText(report.toString());
    }

    @FXML
    private void handleClearOrderReport() {
        orderReportArea.clear();
        reportClientComboBox.setValue(null);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
