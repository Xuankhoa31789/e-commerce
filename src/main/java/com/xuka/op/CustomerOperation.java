package com.xuka.op;

import com.xuka.model.Customer;

public class CustomerOperation {
    private static CustomerOperation instance = null;

    private CustomerOperation() {
        // Private constructor to prevent instantiation
    }

    public static CustomerOperation getInstance() {
        if (instance == null) {
            instance = new CustomerOperation();
        }
        return instance;
    }

    public boolean validateEmail(String userEmail) {
        if (userEmail == null || userEmail.isEmpty()) {
            return false; // Email cannot be null or empty
        }

        // Regular expression to validate email format
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return userEmail.matches(emailRegex);
    }

    public boolean validateMobile(String userMobile) {
        if (userMobile == null || userMobile.isEmpty()) {
            return false; // Mobile number cannot be null or empty
        }

        // Regular expression to validate mobile number format
        return userMobile.matches("^(04|03)\\d{8}$");
    }

    public boolean registerCustomer(String userName, String userPassword, String userEmail, String userMobile) {
        // Validate inputs
        UserOperation userOp = UserOperation.getInstance();
        CustomerOperation customerOp = CustomerOperation.getInstance();

        if (!userOp.validateUsername(userName) || !userOp.validatePassword(userPassword) ||
                !customerOp.validateEmail(userEmail) || !customerOp.validateMobile(userMobile)) {
            System.out.println("Invalid input");
            return false; // Invalid input
        }

        // Check if username already exists
        if (userOp.checkUsernameExist(userName)) {
            System.out.println("Username already exists");
            return false; // Username already exists
        }

        // Generate unique user ID
        String userId = userOp.generateUniqueUserId();

        // Get current system time as register time
        String registerTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss"));

        // Encrypt the password
        String encryptedPassword = userOp.encryptPassword(userPassword);

        // Prepare the user data to write into the file
        String userData = String.format(
                "{\"user_id\":\"%s\",\"user_name\":\"%s\",\"user_password\":\"%s\",\"user_register_time\":\"%s\",\"user_role\":\"customer\",\"user_email\":\"%s\",\"user_mobile\":\"%s\"}",
                userId, userName, encryptedPassword, registerTime, userEmail, userMobile
        );

        // Write the user data to the file
        String filePath = "src/main/data/users.txt";
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath, true)) {
            writer.write(System.lineSeparator() + userData);
            return true; // Registration successful
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

    public boolean updateProfile(String attributeName, String value, Customer customerObject) {
        if (attributeName == null || value == null || customerObject == null) {
            System.out.println("Invalid input: attributeName, value, or customerObject is null");
            return false;
        }

        switch (attributeName.toLowerCase()) {
            case "username":
                if (UserOperation.getInstance().validateUsername(value)) {
                    customerObject.setUserName(value);
                    return true;
                } else {
                    System.out.println("Invalid username format");
                    return false;
                }

            case "password":
                if (UserOperation.getInstance().validatePassword(value)) {
                    customerObject.setUserPassword(value);
                    return true;
                } else {
                    System.out.println("Invalid password format");
                    return false;
                }

            case "email":
                if (CustomerOperation.getInstance().validateEmail(value)) {
                    customerObject.setUserEmail(value);
                    return true;
                } else {
                    System.out.println("Invalid email format");
                    return false;
                }

            case "mobile":
                if (CustomerOperation.getInstance().validateMobile(value)) {
                    customerObject.setUserMobile(value);
                    return true;
                } else {
                    System.out.println("Invalid mobile number format");
                    return false;
                }
            case "role":
                if (value.equalsIgnoreCase("customer") || value.equalsIgnoreCase("admin")) {
                    customerObject.setRole(value);
                    return true;
                } else {
                    System.out.println("Invalid role");
                    return false;
                }
            default:
                System.out.println("Invalid attribute name");
                return false;
        }
    }


}
