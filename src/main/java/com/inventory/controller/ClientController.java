package com.inventory.controller;

import com.inventory.App;
import com.inventory.service.ClientService;
import javafx.fxml.FXML;
import java.io.IOException;

public class ClientController {

    private ClientService clientService = new ClientService();

    @FXML
    private void handleRegisterClient() {
        // Call clientService.registerClient()
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
