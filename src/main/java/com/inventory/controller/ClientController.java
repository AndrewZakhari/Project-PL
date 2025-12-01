package com.inventory.controller;

import com.inventory.App;
import com.inventory.model.Client;
import com.inventory.model.Order;
import com.inventory.service.ClientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ClientController {

    private final ClientService clientService = new ClientService();

    @FXML private TextField clientNameField;
    @FXML private TextField clientEmailField;
    @FXML private TextField clientAddressField;
    
    @FXML private TextField orderProductField;
    @FXML private TextField orderQuantityField;
    
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> colClientName;
    @FXML private TableColumn<Client, String> colClientEmail;
    @FXML private TableColumn<Client, String> colClientAddress;

    @FXML
    public void initialize() {
        colClientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colClientEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colClientAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        refreshTable();

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clientNameField.setText(newSelection.getName());
                clientEmailField.setText(newSelection.getEmail());
                clientAddressField.setText(newSelection.getAddress());
            }
        });
    }

    private void refreshTable() {
        List<Client> clients = clientService.getAllClients();
        clientTable.setItems(FXCollections.observableArrayList(clients));
    }

    @FXML
    private void handleRegisterClient() {
        try {
            String name = clientNameField.getText();
            String email = clientEmailField.getText();
            String address = clientAddressField.getText();

            // Check if updating or new (simple check by email existence could be done in service, 
            // but here we just overwrite/append based on service logic)
            Client client = new Client(name, email, address);
            
            // If email exists, we might want to update. The service currently appends. 
            // For a better UX, we should check if it exists, but for now we stick to the service.
            // Let's assume register = add or update if we had an update method.
            // Since we only have register (append) and edit (update), let's try to use edit if selected.
            
            Client selected = clientTable.getSelectionModel().getSelectedItem();
            if (selected != null && selected.getEmail().equals(email)) {
                 clientService.editClient(email, client);
                 showAlert("Success", "Client updated successfully!");
            } else {
                 clientService.registerClient(client);
                 showAlert("Success", "Client registered successfully!");
            }
            refreshTable();
        } catch (Exception e) {
            showAlert("Error", "Invalid input.");
        }
    }

    @FXML
    private void handleCreateOrder() {
        try {
            String email = clientEmailField.getText();
            String product = orderProductField.getText();
            int quantity = Integer.parseInt(orderQuantityField.getText());
            String orderId = UUID.randomUUID().toString().substring(0, 8);

            Order order = new Order(orderId, email, product, LocalDate.now(), quantity);
            clientService.createPurchaseOrder(order);
            showAlert("Success", "Order created successfully! ID: " + orderId);
        } catch (Exception e) {
            showAlert("Error", "Could not create order: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateInvoice() {
        String email = clientEmailField.getText();
        if(email.isEmpty()) {
            showAlert("Error", "Enter client email to view orders.");
            return;
        }
        Client dummy = new Client("", email, "");
        String report = clientService.generateOrderReport(dummy);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invoice / History");
        alert.setHeaderText("Order History for " + email);
        TextArea area = new TextArea(report);
        area.setEditable(false);
        area.setWrapText(true);
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
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