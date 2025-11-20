package com.inventory.pl_project.services;

import com.inventory.pl_project.models.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    private ProductService productService;
    private CategoryService categoryService;
    private OrderService orderService;
    private ClientService clientService;

    public ReportService() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
        this.orderService = new OrderService();
        this.clientService = new ClientService();
    }

    // Product Reports
    public Map<String, Object> generateProductReport() {
        Map<String, Object> report = new HashMap<>();
        List<Product> products = productService.getAllProducts();

        report.put("totalProducts", products.size());
        report.put("totalValue", products.stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum());
        report.put("lowStockProducts", products.stream()
                .filter(p -> p.getQuantity() <= 10)
                .count());
        report.put("expiredProducts", products.stream()
                .filter(p -> p.getExpirationDate() != null &&
                        p.getExpirationDate().isBefore(LocalDate.now()))
                .count());

        return report;
    }

    public Map<String, Integer> getProductsByCategory() {
        List<Product> products = productService.getAllProducts();
        Map<String, Integer> categoryCounts = new HashMap<>();

        for (Product product : products) {
            Category category = categoryService.getCategoryById(product.getCategoryId());
            String categoryName = category != null ? category.getName() : "Unknown";
            categoryCounts.put(categoryName, categoryCounts.getOrDefault(categoryName, 0) + 1);
        }

        return categoryCounts;
    }

    public List<Product> getExpiredProducts() {
        return productService.getAllProducts().stream()
                .filter(p -> p.getExpirationDate() != null &&
                        p.getExpirationDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    // Category Reports
    public Map<String, Object> generateCategoryReport() {
        Map<String, Object> report = new HashMap<>();
        List<Category> categories = categoryService.getAllCategories();
        Map<String, Integer> productCounts = getProductsByCategory();

        report.put("totalCategories", categories.size());
        report.put("productDistribution", productCounts);

        return report;
    }

    // Sales and Profit Reports
    public Map<String, Object> generateSalesReport(LocalDateTime start, LocalDateTime end) {
        List<Order> orders = orderService.getOrdersByDateRange(start, end);

        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders", orders.size());
        report.put("totalRevenue", orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum());
        report.put("averageOrderValue", orders.isEmpty() ? 0 :
                orders.stream().mapToDouble(Order::getTotalAmount).average().orElse(0));
        report.put("completedOrders", orders.stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .count());

        return report;
    }

    public Map<String, Object> generateProfitReport(LocalDateTime start, LocalDateTime end) {
        List<Order> orders = orderService.getOrdersByDateRange(start, end);

        double totalRevenue = 0;
        double totalCost = 0;

        for (Order order : orders) {
            totalRevenue += order.getTotalAmount();

            for (Order.OrderItem item : order.getItems()) {
                Product product = productService.getProductById(item.getProductId());
                if (product != null) {
                    // Assuming cost is 60% of price (adjust as needed)
                    totalCost += item.getQuantity() * (product.getPrice() * 0.6);
                }
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("totalRevenue", totalRevenue);
        report.put("totalCost", totalCost);
        report.put("grossProfit", totalRevenue - totalCost);
        report.put("profitMargin", totalRevenue > 0 ?
                ((totalRevenue - totalCost) / totalRevenue) * 100 : 0);

        return report;
    }

    // Client Reports
    public Map<String, Object> generateClientReport(String clientId) {
        List<Order> clientOrders = orderService.getOrdersByClient(clientId);
        Client client = clientService.getClientById(clientId);

        Map<String, Object> report = new HashMap<>();
        report.put("clientName", client != null ? client.getName() : "Unknown");
        report.put("totalOrders", clientOrders.size());
        report.put("totalSpent", clientOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum());
        report.put("averageOrderValue", clientOrders.isEmpty() ? 0 :
                clientOrders.stream().mapToDouble(Order::getTotalAmount).average().orElse(0));

        return report;
    }

    public List<Map<String, Object>> getTopClients(int limit) {
        List<Client> clients = clientService.getAllClients();

        return clients.stream()
                .map(client -> {
                    List<Order> orders = orderService.getOrdersByClient(client.getId());
                    double totalSpent = orders.stream()
                            .mapToDouble(Order::getTotalAmount)
                            .sum();

                    Map<String, Object> clientData = new HashMap<>();
                    clientData.put("clientName", client.getName());
                    clientData.put("totalOrders", orders.size());
                    clientData.put("totalSpent", totalSpent);
                    return clientData;
                })
                .sorted((a, b) -> Double.compare(
                        (Double) b.get("totalSpent"),
                        (Double) a.get("totalSpent")))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Generate text report
    public String generateTextReport(String reportType) {
        StringBuilder report = new StringBuilder();
        report.append("=== ").append(reportType.toUpperCase()).append(" REPORT ===\n\n");

        switch (reportType.toLowerCase()) {
            case "products":
                Map<String, Object> productReport = generateProductReport();
                report.append("Total Products: ").append(productReport.get("totalProducts")).append("\n");
                report.append("Total Inventory Value: $").append(productReport.get("totalValue")).append("\n");
                report.append("Low Stock Products: ").append(productReport.get("lowStockProducts")).append("\n");
                report.append("Expired Products: ").append(productReport.get("expiredProducts")).append("\n");
                break;

            case "sales":
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime monthAgo = now.minusMonths(1);
                Map<String, Object> salesReport = generateSalesReport(monthAgo, now);
                report.append("Total Orders: ").append(salesReport.get("totalOrders")).append("\n");
                report.append("Total Revenue: $").append(salesReport.get("totalRevenue")).append("\n");
                report.append("Average Order Value: $").append(salesReport.get("averageOrderValue")).append("\n");
                report.append("Completed Orders: ").append(salesReport.get("completedOrders")).append("\n");
                break;

            case "profit":
                LocalDateTime now2 = LocalDateTime.now();
                LocalDateTime monthAgo2 = now2.minusMonths(1);
                Map<String, Object> profitReport = generateProfitReport(monthAgo2, now2);
                report.append("Total Revenue: $").append(profitReport.get("totalRevenue")).append("\n");
                report.append("Total Cost: $").append(profitReport.get("totalCost")).append("\n");
                report.append("Gross Profit: $").append(profitReport.get("grossProfit")).append("\n");
                report.append("Profit Margin: ").append(profitReport.get("profitMargin")).append("%\n");
                break;
        }

        return report.toString();
    }
}
