package com.inventory.controller;

import com.inventory.App;
import com.inventory.model.Client;
import com.inventory.service.ClientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ClientController {

    private ClientService clientService = new ClientService();

    @FXML
    private ComboBox<String> clientCombo;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    private ObservableList<String> clientEmails = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loadClients();
    }

    private void loadClients() {
        List<Client> clients = clientService.getAllClients();
        clientEmails.clear();
        for (Client c : clients) {
            clientEmails.add(c.getemail());
        }
        clientCombo.setItems(clientEmails);
    }

    @FXML
    private void handleClientSelection() {
        String selectedEmail = clientCombo.getValue();
        if (selectedEmail != null) {
            List<Client> clients = clientService.getAllClients();
            for (Client c : clients) {
                if (c.getemail().equals(selectedEmail)) {
                    nameField.setText(c.getname());
                    emailField.setText(c.getemail());
                    addressField.setText(c.getaddress());
                    break;
                }
            }
        }
    }

    @FXML
    private void handleSaveClient() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }

        String selectedEmail = clientCombo.getValue();
        if (selectedEmail != null && selectedEmail.equals(email)) {
            // Edit existing client
            Client updatedClient = new Client(name, email, address, "", "", ""); // phone and regDate not updated
            boolean success = clientService.editClient(updatedClient);
            if (success) {
                System.out.println("Client updated successfully.");
                loadClients(); // Refresh combo
                clearFields();
            } else {
                System.out.println("Failed to update client.");
            }
        } else {
            // Register new client
            String clientId = UUID.randomUUID().toString();
            String regDate = LocalDate.now().toString();
            Client newClient = new Client(name, email, address, "", clientId, regDate);
            clientService.registerClient(newClient);
            System.out.println("Client registered successfully.");
            loadClients(); // Refresh combo
            clearFields();
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        addressField.clear();
        clientCombo.setValue(null);
    }

    @FXML
    private void handleCreateOrder() {
        // Call clientService.createPurchaseOrder()
    }

    @FXML
    private void handleGenerateInvoice() {
        // Call clientService.generateInvoice()
    }

    @FXML
    private void switchToMain() throws IOException {
        App.setRoot("view/main_menu");
    }
}
