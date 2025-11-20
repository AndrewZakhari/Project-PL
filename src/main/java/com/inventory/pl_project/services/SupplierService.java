package com.inventory.pl_project.services;

import com.google.gson.reflect.TypeToken;
import com.inventory.pl_project.models.Supplier;
import com.inventory.pl_project.utils.FileManager;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class SupplierService {
    private static final String SUPPLIERS_FILE = "suppliers.json";
    private static final Type SUPPLIER_LIST_TYPE = new TypeToken<List<Supplier>>(){}.getType();

    public void addSupplier(Supplier supplier) {
        FileManager.appendToFile(SUPPLIERS_FILE, supplier, SUPPLIER_LIST_TYPE);
    }

    public void updateSupplier(Supplier updatedSupplier) {
        List<Supplier> suppliers = getAllSuppliers();
        suppliers = suppliers.stream()
                .map(s -> s.getId().equals(updatedSupplier.getId()) ? updatedSupplier : s)
                .collect(Collectors.toList());
        FileManager.updateInFile(SUPPLIERS_FILE, suppliers);
    }

    public void deleteSupplier(String supplierId) {
        FileManager.deleteFromFile(SUPPLIERS_FILE, supplierId, SUPPLIER_LIST_TYPE);
    }

    public List<Supplier> getAllSuppliers() {
        return FileManager.readFromFile(SUPPLIERS_FILE, SUPPLIER_LIST_TYPE);
    }

    public Supplier getSupplierById(String id) {
        return getAllSuppliers().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
