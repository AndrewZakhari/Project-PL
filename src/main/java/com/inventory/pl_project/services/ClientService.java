package com.inventory.pl_project.services;

import com.google.gson.reflect.TypeToken;
import com.inventory.pl_project.models.Client;
import com.inventory.pl_project.utils.EmailService;
import com.inventory.pl_project.utils.FileManager;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ClientService {
    private static final String CLIENTS_FILE = "clients.json";
    private static final Type CLIENT_LIST_TYPE = new TypeToken<List<Client>>(){}.getType();

    public void registerClient(Client client) {
        FileManager.appendToFile(CLIENTS_FILE, client, CLIENT_LIST_TYPE);

        // Send welcome email
        String subject = "Welcome to Inventory Management System";
        String body = "Dear " + client.getName() + ",\n\n" +
                "Thank you for registering with our Inventory Management System.\n" +
                "Your account has been successfully created.\n\n" +
                "Best regards,\nIMS Team";
        EmailService.sendEmail(client.getEmail(), subject, body);
    }

    public void updateClient(Client updatedClient) {
        List<Client> clients = getAllClients();
        clients = clients.stream()
                .map(c -> c.getId().equals(updatedClient.getId()) ? updatedClient : c)
                .collect(Collectors.toList());
        FileManager.updateInFile(CLIENTS_FILE, clients);
    }

    public void deleteClient(String clientId) {
        FileManager.deleteFromFile(CLIENTS_FILE, clientId, CLIENT_LIST_TYPE);
    }

    public List<Client> getAllClients() {
        return FileManager.readFromFile(CLIENTS_FILE, CLIENT_LIST_TYPE);
    }

    public Client getClientById(String id) {
        return getAllClients().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Client getClientByEmail(String email) {
        return getAllClients().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
