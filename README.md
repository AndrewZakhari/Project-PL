# Inventory Management System - Developer Guide

Welcome to the Inventory Management System project! This guide outlines the tasks assigned to each developer and provides instructions on where to start and how to implement the required features.

## Getting Started

1.  **Prerequisites**: Ensure you have Java 17+ and Maven installed.
2.  **Run the App**: Use the command `mvn clean javafx:run` to start the application.
3.  **Project Structure**:
    *   `src/main/java/com/inventory/model`: Data classes (POJOs).
    *   `src/main/java/com/inventory/service`: Business logic and file handling.
    *   `src/main/java/com/inventory/controller`: JavaFX controllers for UI interaction.
    *   `src/main/resources/com/inventory/view`: FXML files for UI layout.

---

## 👥 Client Module
**Developers:** George, Sama

**Focus:** Client registration, order management, and invoicing.

### 1. Register & Edit Clients (George)
*   **Goal**: Allow new clients to register and existing ones to update their details.
*   **Files to Modify**:
    *   `src/main/java/com/inventory/service/ClientService.java`: Implement `registerClient(Client client)` and `editClient(Client client)`.
    *   `src/main/java/com/inventory/controller/ClientController.java`: Update `handleRegisterClient()` to read input from text fields and call the service.
    *   `src/main/resources/com/inventory/view/client_view.fxml`: Add `TextField`s for Name, Email, Address and a "Save" button.
*   **Implementation Tip**: Use `FileService` to append client data to `clients.txt` (e.g., CSV format: `name,email,address`).

### 2. Purchase Orders & Invoices (Sama)
*   **Goal**: Create orders for products, generate invoices, and send email notifications.
*   **Files to Modify**:
    *   `src/main/java/com/inventory/service/ClientService.java`: Implement `createPurchaseOrder(Order order)`, `generateInvoice(Order order)`, and `sendOrderNotification(Order order)`.
    *   `src/main/java/com/inventory/controller/ClientController.java`: Update `handleCreateOrder()` and `handleGenerateInvoice()`.
    *   `src/main/resources/com/inventory/view/client_view.fxml`: Add a way to select products (e.g., `ComboBox` or `TableView`) and enter quantities.
*   **Implementation Tip**:
    *   Save orders to `orders.txt`.
    *   Use `EmailService` (JavaMail API) to send the invoice details to the client's email.

---

## 🛠️ Admin Module
**Developers:** Jana, Badr

**Focus:** Managing categories, suppliers, and generating reports.

### 1. Categories & Suppliers (Jana)
*   **Goal**: Add, update, and delete product categories and suppliers.
*   **Files to Modify**:
    *   `src/main/java/com/inventory/service/AdminService.java`: Implement methods for `Category` and `Supplier` (add/update/delete).
    *   `src/main/java/com/inventory/controller/AdminController.java`: Update `handleAddCategory()` and `handleAddSupplier()`.
    *   `src/main/resources/com/inventory/view/admin_view.fxml`: Add forms for entering Category/Supplier details.
*   **Implementation Tip**: Maintain separate files `categories.txt` and `suppliers.txt`. Ensure you handle unique IDs or names to avoid duplicates.

### 2. Reports & Offers (Badr)
*   **Goal**: Generate statistics (sales, expired products, profits) and manage special offers.
*   **Files to Modify**:
    *   `src/main/java/com/inventory/service/AdminService.java`: Implement `generateProductReport()`, `generateProfitReport()`, and `addOffer(Offer offer)`.
    *   `src/main/java/com/inventory/controller/AdminController.java`: Update `handleGenerateReports()` and `handleAddOffer()`.
    *   `src/main/resources/com/inventory/view/admin_view.fxml`: Add a `TextArea` or `TableView` to display the generated reports.
*   **Implementation Tip**:
    *   Read from `orders.txt` to calculate sales/profits.
    *   Read from `products.txt` to find expired items.
    *   Save active offers to `offers.txt` with start/end dates.

---

## 📦 Product Module
**Developers:** Zeinab, Dija

**Focus:** Product CRUD operations, searching, and expiration alerts.

### 1. Product CRUD (Zeinab)
*   **Goal**: Add, update, and delete products from the inventory.
*   **Files to Modify**:
    *   `src/main/java/com/inventory/service/ProductService.java`: Implement `addProduct`, `updateProduct`, and `deleteProduct`.
    *   `src/main/java/com/inventory/controller/ProductController.java`: Update handlers to collect product info (Name, Category, Price, Quantity, Expiration Date).
    *   `src/main/resources/com/inventory/view/product_view.fxml`: Add input fields and a `DatePicker` for the expiration date.
*   **Implementation Tip**: Store products in `products.txt`. When updating/deleting, you'll need to read the whole file, modify the list in memory, and write it back.

### 2. Search & Notifications (Dija)
*   **Goal**: Search products by various criteria and notify about expiring items.
*   **Files to Modify**:
    *   `src/main/java/com/inventory/service/ProductService.java`: Implement `searchByName`, `searchByDate`, `searchByCategory`, and `checkExpiration()`.
    *   `src/main/java/com/inventory/controller/ProductController.java`: Update `handleSearch()` to display results (e.g., in a `ListView` or `TableView`).
    *   **Notifications**: Implement a check that runs on startup or via a button to alert if `expirationDate` is within 7 days.
*   **Implementation Tip**:
    *   Parse the text file line-by-line to find matches for search.
    *   Use `java.time.LocalDate` to compare today's date with the product's expiration date.

---

## 🔧 Shared Utilities (All Developers)

### FileService (`src/main/java/com/inventory/service/FileService.java`)
*   **Task**: Collaborate to create generic methods like `readFile(String filename)`, `appendToFile(String filename, String data)`, and `overwriteFile(String filename, List<String> data)`.
*   **Why**: This avoids code duplication. Everyone should use this service for file I/O.

### EmailService (`src/main/java/com/inventory/service/EmailService.java`)
*   **Task**: Implement a method `sendEmail(String to, String subject, String body)`.
*   **Why**: Required for Client Module notifications.
