package com.xuka.op;

import com.xuka.model.Order;
import com.xuka.model.Product;
import com.xuka.utils.AllCustomerConsumptionApp;
import com.xuka.utils.CustomerConsumptionApp;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class OrderOperation {
    private static OrderOperation instance = null;

    private OrderOperation() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the single instance of OrderOperation.
     * @return OrderOperation instance
     */
    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }

    /**
     * Generates and returns a 5-digit unique order id starting with "o_".
     * @return A string such as o_12345
     */
    public String generateUniqueOrderId() {
        String filePath = "src/main/data/orders.txt";
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    String orderId = jsonObject.getString("order_id");
                    if (orderId.startsWith("o_")) {
                        int numericId = Integer.parseInt(orderId.substring(2));
                        maxId = Math.max(maxId, numericId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Increment the max ID to generate a new unique ID
        int newId = maxId + 1;
        return String.format("o_%05d", newId);
    }

    /**
     * Creates a new order with a unique order id and saves it to the
     * data/orders.txt file.
     * @param customerId The ID of the customer making the order
     * @param productId The ID of the product being ordered
     * @param createTime The time of order creation (null for current time)
     * @return true if successful, false otherwise
     */
    public boolean createAnOrder(String customerId, String productId, String createTime) {
        String filePath = "src/main/data/orders.txt";
        String orderId = generateUniqueOrderId();

        // Use current time if createTime is null
        if (createTime == null || createTime.trim().isEmpty()) {
            createTime = new java.text.SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new java.util.Date());
        }

        // Create a JSON object for the new order
        JSONObject newOrder = new JSONObject();
        newOrder.put("order_id", orderId);
        newOrder.put("user_id", customerId);
        newOrder.put("pro_id", productId);
        newOrder.put("order_time", createTime);

        // Append the new order to the file
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath, true)) {
            writer.write(newOrder.toString() + "\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes the order from the data/orders.txt file based on order_id.
     * @param orderId The ID of the order to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteAnOrder(String orderId) {
        String filePath = "src/main/data/orders.txt";
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    if (!jsonObject.getString("order_id").equals(orderId)) {
                        fileContent.append(line).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Write the updated content back to the file
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath)) {
            writer.write(fileContent.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves one page of orders from the database belonging to the
     * given customer. One page contains a maximum of 10 items.
     * @param customerId The ID of the customer
     * @param pageNumber The page number to retrieve
     * @return A list of Order objects, current page number, and total pages
     */
    public OrderListResult getOrderList(String customerId, int pageNumber) {
        String filePath = "src/main/data/orders.txt";
        List<Order> customerOrders = new ArrayList<>();
        int itemsPerPage = 10;

        // Read and filter orders by customer ID
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    if (jsonObject.getString("user_id").equals(customerId)) {
                        Order order = new Order(
                                jsonObject.getString("order_id"),
                                jsonObject.getString("user_id"),
                                jsonObject.getString("pro_id"),
                                jsonObject.getString("order_time")
                        );
                        customerOrders.add(order);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate pagination details
        int totalOrders = customerOrders.size();
        int totalPages = (int) Math.ceil((double) totalOrders / itemsPerPage);
        pageNumber = Math.max(1, Math.min(pageNumber, totalPages)); // Ensure valid page number

        int startIndex = (pageNumber - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalOrders);

        // Get the orders for the current page
        List<Order> pageOrders = customerOrders.subList(startIndex, endIndex);

        // Return the result
        return new OrderListResult(pageOrders, pageNumber, totalPages);
    }

    /**
     * Automatically generates test data including customers and orders.
     * Creates 10 customers and randomly generates 50-200 orders for each.
     * Order times should be scattered across different months of the year.
     */
    public void generateTestOrderData() {
        String filePath = "src/main/data/orders.txt";
        Set<String> generatedOrderIds = new HashSet<>();
        Random random = new Random();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

        try (FileWriter writer = new FileWriter(filePath, false)) {
            // Generate 10 customers
            for (int customerIndex = 1; customerIndex <= 10; customerIndex++) {
                String customerId = String.format("u_%010d", customerIndex);

                // Generate 50-200 orders for each customer
                int orderCount = 50 + random.nextInt(151); // Random number between 50 and 200
                for (int i = 0; i < orderCount; i++) {
                    String orderId;
                    do {
                        orderId = String.format("o_%05d", random.nextInt(100000));
                    } while (generatedOrderIds.contains(orderId)); // Ensure unique order ID

                    generatedOrderIds.add(orderId);

                    // Generate random product ID
                    String productId = String.format("p%03d", 1 + random.nextInt(20)); // Random product ID between p001 and p020

                    // Generate random order time scattered across the year
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, 2025);
                    calendar.set(Calendar.MONTH, random.nextInt(12)); // Random month
                    calendar.set(Calendar.DAY_OF_MONTH, 1 + random.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
                    calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24));
                    calendar.set(Calendar.MINUTE, random.nextInt(60));
                    calendar.set(Calendar.SECOND, random.nextInt(60));
                    String orderTime = dateFormat.format(calendar.getTime());

                    // Create JSON object for the order
                    JSONObject order = new JSONObject();
                    order.put("order_id", orderId);
                    order.put("user_id", customerId);
                    order.put("pro_id", productId);
                    order.put("order_time", orderTime);

                    // Write the order to the file
                    writer.write(order.toString() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a chart showing the consumption (sum of order prices)
     * across 12 different months for the given customer.
     * @param customerId The ID of the customer
     */
    public void generateSingleCustomerConsumptionFigure(String customerId) {
        String filePath = "src/main/data/orders.txt";
        Map<String, Double> monthlyConsumption = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    if (jsonObject.getString("user_id").equals(customerId)) {
                        String orderTime = jsonObject.getString("order_time");
                        String month = orderTime.substring(3, 10); // Extract "MM-yyyy" from "dd-MM-yyyy_HH:mm:ss"

                        // Retrieve product price
                        String productId = jsonObject.getString("pro_id");
                        Product product = ProductOperation.getInstance().getProductById(productId);
                        if (product != null) {
                            double price = product.getProCurrentPrice();
                            monthlyConsumption.put(month, monthlyConsumption.getOrDefault(month, 0.0) + price);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort the months in chronological order
        List<Map.Entry<String, Double>> sortedConsumption = monthlyConsumption.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        // Pass the sorted data to the JavaFX application
        CustomerConsumptionApp.setMonthlyConsumption(sortedConsumption);

        // Launch the JavaFX application
        CustomerConsumptionApp.launch(CustomerConsumptionApp.class);
    }

    /**
     * Generates a chart showing the consumption (sum of order prices)
     * across 12 different months for all customers.
     */
    public void generateAllCustomersConsumptionFigure() {
        String filePath = "src/main/data/orders.txt";
        Map<String, Double> customerConsumption = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    String customerId = jsonObject.getString("user_id");

                    // Retrieve product price
                    String productId = jsonObject.getString("pro_id");
                    Product product = ProductOperation.getInstance().getProductById(productId);
                    if (product != null) {
                        double price = product.getProCurrentPrice();
                        customerConsumption.put(customerId, customerConsumption.getOrDefault(customerId, 0.0) + price);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort customers alphabetically by ID
        List<Map.Entry<String, Double>> sortedConsumption = customerConsumption.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        // Pass the sorted data to the JavaFX application
        AllCustomerConsumptionApp.setCustomerConsumption(sortedConsumption);

        // Launch the JavaFX application
        AllCustomerConsumptionApp.launch(AllCustomerConsumptionApp.class);
    }

    /**
     * Removes all data in the data/orders.txt file.
     */
    public void deleteAllOrders() {
        String filePath = "src/main/data/orders.txt";

        try {
            Files.write(Paths.get(filePath), new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
