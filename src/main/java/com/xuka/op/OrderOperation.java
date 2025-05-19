package com.xuka.op;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
}
