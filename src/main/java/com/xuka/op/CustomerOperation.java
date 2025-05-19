package com.xuka.op;

import com.xuka.model.Customer;

import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomerOperation {
    private static CustomerOperation instance = null;

    private CustomerOperation() {
    }

    /**
     * Returns the single instance of CustomerOperation.
     *
     * @return CustomerOperation instance
     */
    public static CustomerOperation getInstance() {
        if (instance == null) {
            instance = new CustomerOperation();
        }
        return instance;
    }

    /**
     * Validate the provided email address format. An email address
     * consists of username@domain.extension format.
     *
     * @param userEmail The email to validate
     * @return true if valid, false otherwise
     */
    public boolean validateEmail(String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            return false; // Email cannot be null or empty
        }

        // Regular expression to validate email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return userEmail.matches(emailRegex);
    }

    /**
     * Validate the provided mobile number format. The mobile number
     * should be exactly 10 digits long, consisting only of numbers,
     * and starting with either '04' or '03'.
     *
     * @param userMobile The mobile number to validate
     * @return true if valid, false otherwise
     */
    public boolean validateMobile(String userMobile) {
        if (userMobile == null || userMobile.isEmpty()) {
            return false; // Mobile number cannot be null or empty
        }

        // Regular expression to validate mobile number format
        return userMobile.matches("^(04|03)\\d{8}$");
    }

    /**
     * Save the information of the new customer into the data/users.txt file.
     *
     * @param userName     Customer's username
     * @param userPassword Customer's password
     * @param userEmail    Customer's email
     * @param userMobile   Customer's mobile number
     * @return true if success, false if failure
     */
    public boolean registerCustomer(String userName, String userPassword, String userEmail, String userMobile) {
        // Validate inputs
        if (!UserOperation.getInstance().validateUsername(userName)) {
            throw new IllegalArgumentException("Invalid username format");
        }
        if (!UserOperation.getInstance().validatePassword(userPassword)) {
            throw new IllegalArgumentException("Invalid password format");
        }
        if (!validateEmail(userEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!validateMobile(userMobile)) {
            throw new IllegalArgumentException("Invalid mobile number format");
        }

        // Check if username already exists
        if (UserOperation.getInstance().checkUsernameExist(userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Generate unique user ID
        String userId = UserOperation.getInstance().generateUniqueUserId();

        // Get current system time as register time
        String registerTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));

        // Encrypt the password
        String encryptedPassword = UserOperation.getInstance().encryptPassword(userPassword);

        JSONObject newCustomer = new JSONObject();
        newCustomer.put("user_id", userId);
        newCustomer.put("user_name", userName);
        newCustomer.put("user_password", encryptedPassword);
        newCustomer.put("user_email", userEmail);
        newCustomer.put("user_mobile", userMobile);
        newCustomer.put("user_register_time", registerTime);
        newCustomer.put("user_role", "customer");

        String filePath = "src/main/data/users.txt";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(System.lineSeparator());
            writer.write(newCustomer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Registration failed
        }

        return true; // Registration successful
    }

    /**
     * Update the given customer object's attribute value. According to
     * different attributes, perform appropriate validations.
     *
     * @param attributeName  The attribute to update
     * @param value          The new value
     * @param customerObject The customer object to update
     * @return true if updated, false if failed
     */
    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        if (attributeName == null || value == null || customerObject == null) {
            throw new IllegalArgumentException("Attribute name, value, and customer object cannot be null");
        }

        switch (attributeName.toLowerCase()) {
            case "username":
                if (UserOperation.getInstance().validateUsername(value)) {
                    customerObject.setUserName(value);
                    return true;
                } else {
                    throw new IllegalArgumentException("Invalid username format");
                }

            case "password":
                if (UserOperation.getInstance().validatePassword(value)) {
                    customerObject.setUserPassword(value);
                    return true;
                } else {
                    throw new IllegalArgumentException("Invalid password format");
                }

            case "email":
                if (CustomerOperation.getInstance().validateEmail(value)) {
                    customerObject.setUserEmail(value);
                    return true;
                } else {
                    throw new IllegalArgumentException("Invalid email format");
                }

            case "mobile":
                if (CustomerOperation.getInstance().validateMobile(value)) {
                    customerObject.setUserMobile(value);
                    return true;
                } else {
                    throw new IllegalArgumentException("Invalid mobile number format");
                }
            case "role":
                if (value.equalsIgnoreCase("customer") || value.equalsIgnoreCase("admin")) {
                    customerObject.setRole(value);
                    return true;
                } else {
                    throw new IllegalArgumentException("Invalid role format");
                }
            default:
                throw new IllegalArgumentException("Invalid attribute name");
        }
    }

    /**
     * Delete the customer from the data/users.txt file based on the
     * provided customer_id.
     *
     * @param customerId The ID of the customer to delete
     * @return true if deleted, false if failed
     */
    public boolean deleteCustomer(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        String filePath = "src/main/data/users.txt";
        try {
            // Read the file content
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Split the content into lines
            String[] lines = content.split(System.lineSeparator());
            JSONArray jsonArray = new JSONArray();

            // Parse each line as JSON and add to JSONArray
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    jsonArray.put(jsonObject);
                }
            }

            // Find and remove the customer with the given ID
            boolean isDeleted = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("user_id").equals(customerId)) {
                    jsonArray.remove(i);
                    isDeleted = true;
                    break;
                }
            }

            if (!isDeleted) {
                throw new IllegalArgumentException("Customer ID not found");
            }

            try (FileWriter writer = new FileWriter(new File(filePath))) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    writer.write(jsonArray.getJSONObject(i).toString());
                    if (i < jsonArray.length() - 1) {
                        writer.write(System.lineSeparator());
                    }
                }
            }

            return true; // Deletion successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Deletion failed
        }
    }

    /**
     * Retrieve one page of customers from the data/users.txt.
     * One page contains a maximum of 10 customers.
     * @param pageNumber The page number to retrieve
     * @return A List of Customer objects, the current page number, and total
    pages (This use a helper class CustomerListResult)
     */
    public CustomerListResult getCustomerList(int pageNumber) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }

        String filePath = "src/main/data/users.txt";
        List<Customer> customers = new ArrayList<>();
        int totalCustomers = 0;

        try {
            // Read the entire file content
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Parse the content as a JSON array
            String[] lines = content.split(System.lineSeparator());
            JSONArray jsonArray = new JSONArray();
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    jsonArray.put(new JSONObject(line));
                }
            }

            // Filter customers and add them to the list
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("user_role").equalsIgnoreCase("customer")) {
                    Customer customer = new Customer(
                            jsonObject.getString("user_id"),
                            jsonObject.getString("user_name"),
                            jsonObject.getString("user_password"),
                            jsonObject.getString("user_register_time"),
                            jsonObject.getString("user_role"),
                            jsonObject.getString("user_email"),
                            jsonObject.getString("user_mobile")
                    );
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        totalCustomers = customers.size();
        int totalPages = (int) Math.ceil((double) totalCustomers / 10);

        if (pageNumber > totalPages) {
            throw new IllegalArgumentException("Page number exceeds total pages");
        }

        int startIndex = (pageNumber - 1) * 10;
        int endIndex = Math.min(startIndex + 10, totalCustomers);
        List<Customer> pageCustomers = customers.subList(startIndex, endIndex);

        return new CustomerListResult(pageCustomers, pageNumber, totalPages);
    }

    /**
     * Removes all the customers from the data/users.txt file.
     */
    public void deleteAllCustomers() {
        String filePath = "src/main/data/users.txt";

        try {
            // Read the entire file content
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Split the content into lines
            String[] lines = content.split(System.lineSeparator());
            JSONArray jsonArray = new JSONArray();

            // Parse each line as JSON and add only non-customer entries to JSONArray
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    if (!jsonObject.getString("user_role").equalsIgnoreCase("customer")) {
                        jsonArray.put(jsonObject);
                    }
                }
            }

            // Write the updated JSON array back to the file
            try (FileWriter writer = new FileWriter(filePath)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    writer.write(jsonArray.getJSONObject(i).toString());
                    if (i < jsonArray.length() - 1) {
                        writer.write(System.lineSeparator());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


