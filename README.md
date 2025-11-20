# Inventory Management System - Developer Guide

This document provides a guide for developers working on the Inventory Management System. It outlines the project structure, key modules, and specific tasks for each developer.

## Project Overview

The Inventory Management System is a JavaFX application designed to manage products, clients, and administrative tasks. The system is divided into three main modules:

*   **Product Module:** Manages the inventory of products, including adding, updating, deleting, and searching for products. It also handles notifications for expiring products.
*   **Admin Module:** Handles administrative tasks such as managing product categories and suppliers, generating reports, and creating special offers.
*   **Client Module:** Manages client information, processes purchase orders, and generates client-specific reports.

## Project Structure

The project follows a standard Maven project structure. The core application logic is located in `src/main/java/com/inventory/pl_project`.

*   `controllers/`: Contains the JavaFX controllers for each view (`.fxml` file). These classes handle user interactions and connect the UI to the application's backend logic.
*   `models/`: Defines the data models for the application (e.g., `Product`, `Client`, `Order`).
*   `services/`: Contains the business logic of the application. Services are responsible for operations like data manipulation, calculations, and interactions with the file system.
*   `utils/`: Provides utility classes for common tasks such as file management (`FileManager`) and sending emails (`EmailService`).
*   `src/main/resources/data/`: Stores the application's data in JSON files.
*   `src/main/resources/fxml/`: Contains the FXML files that define the user interface.

## Running the Project Locally on Windows

To run the project on your local machine, you first need to set up your development environment.

### 1. Install Java Development Kit (JDK)

This project requires JDK 17 or later.

1.  **Download:** Go to the [Oracle JDK download page](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or use an alternative like [OpenJDK](https://adoptium.net/).
2.  **Install:** Run the installer and follow the on-screen instructions.
3.  **Set up `JAVA_HOME` environment variable:**
    *   Find the JDK installation path (e.g., `C:\Program Files\Java\jdk-17`).
    *   Search for "Edit the system environment variables" in the Start Menu.
    *   Click on "Environment Variables...".
    *   Under "System variables", click "New...".
    *   For "Variable name", enter `JAVA_HOME`.
    *   For "Variable value", enter your JDK installation path.
4.  **Update the `Path` variable:**
    *   In the same "System variables" section, find the `Path` variable and click "Edit...".
    *   Click "New" and add `%JAVA_HOME%\bin`.
    *   Click "OK" to save.
5.  **Verify installation:** Open a new command prompt and run `java -version`. You should see the installed JDK version.

### 2. Install Apache Maven

Maven is used to build and manage the project.

1.  **Download:** Go to the [Maven download page](https://maven.apache.org/download.cgi) and download the "Binary zip archive".
2.  **Extract:** Unzip the file to a location on your computer (e.g., `C:\Program Files\apache-maven-3.9.6`).
3.  **Set up `MAVEN_HOME` environment variable:**
    *   Follow the same steps as for `JAVA_HOME`.
    *   For "Variable name", enter `MAVEN_HOME`.
    *   For "Variable value", enter the path where you extracted Maven.
4.  **Update the `Path` variable:**
    *   Add `%MAVEN_HOME%\bin` to your `Path` system variable, just as you did for Java.
5.  **Verify installation:** Open a new command prompt and run `mvn -version`. You should see the installed Maven version.

### 3. Clone and Run the Project

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/AndrewZakhari/Project-PL
    cd Pl_project
    ```

2.  **Build and run the project:**
    Open a terminal or command prompt in the root directory of the project and run the following command:
    ```bash
    mvn clean javafx:run
    ```
    This command will compile the project, download the necessary dependencies, and start the application.

## Developer Tasks

This section details the tasks assigned to each developer.

---

### Client Module

#### 1. George: Client Registration and Data Management

You will be responsible for implementing the functionality to register new clients and edit their data.

*   **Files to work on:**
    *   `src/main/java/com/inventory/pl_project/controllers/ClientController.java`: Implement the `handleRegisterClient` and `handleUpdateClient` methods.
    *   `src/main/java/com/inventory/pl_project/services/ClientService.java`: Implement the `registerClient` and `updateClient` methods. You will use the `FileManager` to save and update client data in `clients.json`.
    *   `src/main/java/com/inventory/pl_project/utils/EmailService.java`: Use this service to send a confirmation email upon successful client registration. You will need to configure your email credentials in this file.

*   **Specific Tasks:**
    1.  In `ClientController`, get the client data from the input fields.
    2.  Call the `registerClient` or `updateClient` method in `ClientService`.
    3.  In `ClientService`, use `FileManager.appendToFile` to add a new client and `FileManager.updateInFile` to modify an existing one.
    4.  After a new client is registered, call `EmailService.sendEmail` to send a welcome email.

#### 2. Sama: Purchase Orders and Client Reports

You will implement the functionality for clients to create purchase orders and for the system to generate reports on client orders.

*   **Files to work on:**
    *   `src/main/java/com/inventory/pl_project/controllers/ClientController.java`: Implement `handleCreateOrder`, `handleGenerateInvoice`, and `handleGenerateOrderReport`.
    *   `src/main/java/com/inventory/pl_project/services/OrderService.java`: Implement `createOrder`, `generateInvoice`, and `getOrdersByClient`.
    *   `src/main/java/com/inventory/pl_project/services/ClientService.java`: Use `getClientById` to fetch client details for reports.
    *   `src/main/java/com/inventory/pl_project/utils/EmailService.java`: Use `sendOrderNotification` to email clients after an order is placed.

*   **Specific Tasks:**
    1.  In `ClientController`, manage the process of adding items to an order and creating the final order.
    2.  In `OrderService`, when an order is created, update the product quantities using `ProductService` and save the order to `orders.json`.
    3.  Implement the logic to generate a text-based invoice for a selected order.
    4.  Implement the `handleGenerateOrderReport` method in `ClientController` to display the order history for a selected client.

---

### Admin Module

#### 1. Jana: Category and Supplier Management

You are responsible for the management of product categories and suppliers.

*   **Files to work on:**
    *   `src/main/java/com/inventory/pl_project/controllers/AdminController.java`: Implement `handleAddCategory`, `handleUpdateCategory`, `handleDeleteCategory`, `handleAddSupplier`, `handleUpdateSupplier`, and `handleDeleteSupplier`.
    *   `src/main/java/com/inventory/pl_project/services/CategoryService.java`: Implement the CRUD (Create, Read, Update, Delete) operations for categories.
    *   `src/main/java/com/inventory/pl_project/services/SupplierService.java`: Implement the CRUD operations for suppliers.

*   **Specific Tasks:**
    1.  In `AdminController`, link the UI buttons to the corresponding methods in `CategoryService` and `SupplierService`.
    2.  In `CategoryService`, use `FileManager` to manage the `categories.json` file.
    3.  In `SupplierService`, use `FileManager` to manage the `suppliers.json` file.
    4.  Ensure the UI tables in the admin view are updated after any changes.

#### 2. Badr: Reports, Statistics, and Offers

You will work on generating reports, calculating statistics, and managing special offers for products.

*   **Files to work on:**
    *   `src/main/java/com/inventory/pl_project/controllers/AdminController.java`: Implement `handleGenerateReport` and `handleAddOffer`.
    *   `src/main/java/com/inventory/pl_project/services/ReportService.java`: Implement the logic for generating product, category, sales, and profit reports.
    *   `src/main/java/com/inventory/pl_project/services/ProductService.java`: You will need to interact with this service to add offers to products.
    *   `src/main/java/com/inventory/pl_project/models/Offer.java`: This is the model for an offer.

*   **Specific Tasks:**
    1.  In `ReportService`, implement methods to calculate statistics like total inventory value, sales revenue, and profit.
    2.  In `AdminController`, display the generated reports in the `reportTextArea`.
    3.  Implement the `handleAddOffer` method to create a new offer with a discount and a time interval.
    4.  In `ProductService`, update the `Product` object to include the new offer and save the changes.

---

### Product Module

#### 1. Zeinab: Product Management

Your task is to implement the core functionalities for managing products: adding, updating, and deleting them.

*   **Files to work on:**
    *   `src/main/java/com/inventory/pl_project/controllers/ProductController.java`: Implement `handleAddProduct`, `handleUpdateProduct`, and `handleDeleteProduct`.
    *   `src/main/java/com/inventory/pl_project/services/ProductService.java`: Implement the `addProduct`, `updateProduct`, and `deleteProduct` methods.
    *   `src/main/java/com/inventory/pl_project/utils/FileManager.java`: Your services will use this utility to interact with `products.json`.

*   **Specific Tasks:**
    1.  In `ProductController`, retrieve product information from the UI fields.
    2.  Call the appropriate methods in `ProductService` to perform the CRUD operations.
    3.  In `ProductService`, use `FileManager` to persist the changes to the `products.json` file.
    4.  After each operation, ensure the product table in the UI is refreshed to show the latest data.

#### 2. Dija: Product Search and Expiration Notifications

You will be responsible for implementing the product search functionality and the notification system for expiring products.

*   **Files to work on:**
    *   `src/main/java/com/inventory/pl_project/controllers/ProductController.java`: Implement the `handleSearch` and `handleCheckExpiration` methods.
    *   `src/main/java/com/inventory/pl_project/services/ProductService.java`: Implement `searchByName`, `searchByCategory`, `searchByDateRange`, and `getProductsNearingExpiration`.
    *   `src/main/java/com/inventory/pl_project/services/NotificationService.java`: Implement the logic to check for expiring products and send notifications.

*   **Specific Tasks:**
    1.  In `ProductService`, implement the search methods to filter products from `products.json` based on different criteria.
    2.  In `ProductController`, use the search methods from `ProductService` to display the search results in the table.
    3.  In `NotificationService`, create a method that runs periodically (e.g., daily) to check for products nearing their expiration date.
    4.  When an expiring product is found, use `EmailService` to send a notification to a designated admin email address. The `handleCheckExpiration` button in `ProductController` should trigger this check manually.
