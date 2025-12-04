package com.inventory.service;

import java.util.ArrayList;
import java.util.List;

import com.inventory.model.Client;

public class ClientService {

    // FileService in your workspace expects a path fragment; pass with leading slash
    private static final String CLIENT_FILE = "/clients.txt";

    private final FileService fileService = new FileService();

    /**
     * Register a new client by appending a CSV line to the clients file.
     */
    public void registerClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client cannot be null");
        }

        String line = toCsvLine(client);
        fileService.appendToFile(CLIENT_FILE, line);
        System.out.println("Client registered: " + summary(client));
    }

    /**
     * Edit an existing client. Matches by clientId if available; falls back to email.
     * Reads all lines, replaces the matching line, then overwrites the file.
     */
    public boolean editClient(Client updatedClient) {
        if (updatedClient == null) {
            throw new IllegalArgumentException("updatedClient cannot be null");
        }

        List<String> all = fileService.readAllLines(CLIENT_FILE);
        if (all == null) all = new ArrayList<>();

        String targetId = safeGetClientId(updatedClient);
        String targetEmail = safeGetEmail(updatedClient);

        boolean changed = false;
        List<String> out = new ArrayList<>(all.size());

        for (String line : all) {
            String[] parts = line.split(",");
            String existingId = parts.length > 4 ? parts[4].trim() : "";
            String existingEmail = parts.length > 1 ? parts[1].trim() : "";

            boolean match = false;
            if (!targetId.isEmpty() && !existingId.isEmpty()) {
                match = existingId.equals(targetId);
            } else if (!targetEmail.isEmpty() && !existingEmail.isEmpty()) {
                match = existingEmail.equalsIgnoreCase(targetEmail);
            }

            if (match) {
                out.add(toCsvLine(updatedClient));
                changed = true;
            } else {
                out.add(line);
            }
        }

        if (changed) {
            fileService.overwriteFile(CLIENT_FILE, out);
            System.out.println("Client updated: " + summary(updatedClient));
        } else {
            System.out.println("Client to update not found.");
        }

        return changed;
    }

    // Build CSV line expected by other parts of the app (name,email,address,phone,clientId)
    private String toCsvLine(Client c) {
        String name = safeCall(() -> c.getname());
        String email = safeCall(() -> c.getemail());
        String address = safeCall(() -> c.getaddress());
        String phone = safeCall(() -> c.getphoneNumber());
        String id = safeCall(() -> c.getclientId());

        return String.join(",", name, email, address, phone, id);
    }

    // Small helpers to avoid NPEs
    private String safeGetClientId(Client c) {
        String v = safeCall(() -> c.getclientId());
        return v == null ? "" : v.trim();
    }
    private String safeGetEmail(Client c) {
        String v = safeCall(() -> c.getemail());
        return v == null ? "" : v.trim();
    }
    private String summary(Client c) {
        return safeCall(() -> c.getname()) + " <" + safeCall(() -> c.getemail()) + ">";
    }

    // Functional wrapper to call client getters that may be present in your model.
    private interface SupplierEx { String get() throws Exception; }
    private String safeCall(SupplierEx s) {
        try {
            String r = s.get();
            return r == null ? "" : r;
        } catch (Throwable t) {
            return "";
        }
    }
}