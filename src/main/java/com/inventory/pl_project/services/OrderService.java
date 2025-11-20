package com.inventory.pl_project.services;

import com.google.gson.reflect.TypeToken;
import com.inventory.pl_project.models.Order;
import com.inventory.pl_project.models.Product;
import com.inventory.pl_project.models.Client;
import com.inventory.pl_project.utils.EmailService;
import com.inventory.pl_project.utils.FileManager;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private static final String ORDERS_FILE = "orders.json";
    private static final Type ORDER_LIST_TYPE = new TypeToken<List<Order>>(){}.getType();

    private ProductService productService;
    private ClientService clientService;

    public OrderService() {
        this.productService = new ProductService();
        this.clientService = new ClientService();
    }

    public Order createOrder(String clientId, List<Order.OrderItem> items) {
        Order order = new Order(generateOrderId(), clientId);
        order.setItems(items);

        double total = 0;
        for (Order.OrderItem item : items) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                // Update product quantity
                product.setQuantity(product.getQuantity() - item.getQuantity());
                productService.updateProduct(product);

                item.setPricePerUnit(product.getPrice());
                item.setSubtotal(item.getQuantity() * product.getPrice());
                total += item.getSubtotal();
            }
        }

        order.setTotalAmount(total);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("COMPLETED");

        FileManager.appendToFile(ORDERS_FILE, order, ORDER_LIST_TYPE);

        // Send notification email
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            EmailService.sendOrderNotification(client.getEmail(), order.getId(), total);
        }

        return order;
    }

    public void updateOrder(Order updatedOrder) {
        List<Order> orders = getAllOrders();
        orders = orders.stream()
                .map(o -> o.getId().equals(updatedOrder.getId()) ? updatedOrder : o)
                .collect(Collectors.toList());
        FileManager.updateInFile(ORDERS_FILE, orders);
    }

    public void deleteOrder(String orderId) {
        FileManager.deleteFromFile(ORDERS_FILE, orderId, ORDER_LIST_TYPE);
    }

    public List<Order> getAllOrders() {
        return FileManager.readFromFile(ORDERS_FILE, ORDER_LIST_TYPE);
    }

    public Order getOrderById(String id) {
        return getAllOrders().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersByClient(String clientId) {
        return getAllOrders().stream()
                .filter(o -> o.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        return getAllOrders().stream()
                .filter(o -> !o.getOrderDate().isBefore(start) && !o.getOrderDate().isAfter(end))
                .collect(Collectors.toList());
    }

    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }

    public String generateInvoice(String orderId) {
        Order order = getOrderById(orderId);
        if (order == null) return "Order not found";

        Client client = clientService.getClientById(order.getClientId());

        StringBuilder invoice = new StringBuilder();
        invoice.append("=== ORDER INVOICE ===\n\n");
        invoice.append("Order ID: ").append(order.getId()).append("\n");
        invoice.append("Date: ").append(order.getOrderDate()).append("\n");
        invoice.append("Client: ").append(client != null ? client.getName() : "Unknown").append("\n");
        invoice.append("\n--- ITEMS ---\n");

        for (Order.OrderItem item : order.getItems()) {
            Product product = productService.getProductById(item.getProductId());
            invoice.append(product != null ? product.getName() : "Unknown Product")
                    .append(" x").append(item.getQuantity())
                    .append(" @ $").append(item.getPricePerUnit())
                    .append(" = $").append(item.getSubtotal())
                    .append("\n");
        }

        invoice.append("\n--- TOTAL ---\n");
        invoice.append("Total Amount: $").append(order.getTotalAmount()).append("\n");
        invoice.append("Status: ").append(order.getStatus()).append("\n");

        return invoice.toString();
    }
}
