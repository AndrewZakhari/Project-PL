package com.inventory.controller;

import com.inventory.App;
import javafx.fxml.FXML;
import java.io.IOException;

public class MainController {

    @FXML
    private void switchToProduct() throws IOException {
        App.setRoot("view/product_view");
    }

    @FXML
    private void switchToAdmin() throws IOException {
        App.setRoot("view/admin_view");
    }

    @FXML
    private void switchToClient() throws IOException {
        App.setRoot("view/client_view");
    }
}
