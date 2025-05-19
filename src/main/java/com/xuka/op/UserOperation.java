package com.xuka.op;

import com.xuka.model.Admin;
import com.xuka.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

public class UserOperation {
    private static UserOperation instance = null;

    private UserOperation() {
        // Private constructor to prevent instantiation
    }

    public static UserOperation getInstance() {
        if (instance == null) {
            instance = new UserOperation();
        }
        return instance;
    }
    public String generateUniqueUserId() {
        // Path to the file containing user data
        String filePath = "src/main/data/users.txt";
        // Initialize the highest user ID to a default value
        String highestUserId = "u_0000000000";

        // Read the file to find the highest user ID
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains a user ID
                if (line.contains("\"user_id\"")) {
                    // Extract the user ID value from the line
                    int startIndex = line.indexOf("\"user_id\":\"") + 11;
                    int endIndex = line.indexOf("\"", startIndex);
                    String currentUserId = line.substring(startIndex, endIndex);

                    // Compare and update the highest user ID
                    if (currentUserId.compareTo(highestUserId) > 0) {
                        highestUserId = currentUserId;
                    }
                }
            }
        } catch (IOException e) {
            // Handle any file reading errors
            e.printStackTrace();
        }

        // Extract the numeric part of the highest ID, increment it, and format it back
        int nextId = Integer.parseInt(highestUserId.substring(2)) + 1;
        return String.format("u_%010d", nextId); // Return the new unique user ID
    }

    public String encryptPassword(String userPassword) {
        if (userPassword == null || userPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Step 1: Generate a random string with a length equal to two times the length of the user-provided password
        int randomStringLength = userPassword.length() * 2;
        StringBuilder randomString = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < randomStringLength; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        // Step 2: Combine the random string and the input password
        StringBuilder encryptedPassword = new StringBuilder("^^");// Add "^^" at the end
        for (int i = 0; i < userPassword.length(); i++) {
            encryptedPassword.append(randomString.substring(i * 2, i * 2 + 2)); // Add two characters from the random string
            encryptedPassword.append(userPassword.charAt(i)); // Add one character from the user password
        }
        encryptedPassword.append("$$"); // Add "$$" at the end

        return encryptedPassword.toString();
    }

    public String decryptPassword(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalArgumentException("Encrypted password cannot be null or empty");
        }

        // Ensure the encrypted password starts with "^^" and ends with "$$"
        if (!encryptedPassword.startsWith("^^") || !encryptedPassword.endsWith("$$")) {
            throw new IllegalArgumentException("Invalid encrypted password format");
        }

        // Remove the "^^" prefix and "$$" suffix
        String trimmedPassword = encryptedPassword.substring(2, encryptedPassword.length() - 2);

        // Extract the original password by skipping every two random characters
        StringBuilder originalPassword = new StringBuilder();
        for (int i = 2; i < trimmedPassword.length(); i += 3) {
            originalPassword.append(trimmedPassword.charAt(i));
        }

        return originalPassword.toString();
    }

    public boolean checkUsernameExist(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        // Path to the file containing user data
        String filePath = "src/main/data/users.txt";

        // Read the file to check if the username exists
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains the username
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    return true; // Username exists
                }
            }
        } catch (IOException e) {
            // Handle any file reading errors
            e.printStackTrace();
        }

        return false; // Username does not exist
    }

    public boolean validateUsername(String userName) {
        if (userName == null || userName.isEmpty()) {
            System.out.println("Username cannot be null or empty");
            return false; // Username cannot be null or empty
        }

        // Check if the username matches the criteria
        return userName.matches("^[a-zA-Z0-9_]{5,}$");
    }

    public boolean validatePassword(String userPassword) {
        if (userPassword == null || userPassword.isEmpty()) {
            return false; // Password cannot be null or empty
        }

        // Check if the password matches the criteria
        return userPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d)[^\\s]{5,}$");
    }

    public User login(String userName, String userPassword) {
        if (userName == null || userPassword == null || userName.isEmpty() || userPassword.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }

        String filePath = "src/main/data/users.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_name\":\"" + userName + "\"")) {
                    int passwordStartIndex = line.indexOf("\"user_password\":\"") + 17;
                    int passwordEndIndex = line.indexOf("\"", passwordStartIndex);
                    String storedPassword = line.substring(passwordStartIndex, passwordEndIndex);

                    int roleStartIndex = line.indexOf("\"user_role\":\"") + 13;
                    int roleEndIndex = line.indexOf("\"", roleStartIndex);
                    String role = line.substring(roleStartIndex, roleEndIndex);

                    int idStartIndex = line.indexOf("\"user_id\":\"") + 11;
                    int idEndIndex = line.indexOf("\"", idStartIndex);
                    String userId = line.substring(idStartIndex, idEndIndex);

                    int registerTimeStartIndex = line.indexOf("\"user_register_time\":\"") + 23;
                    int registerTimeEndIndex = line.indexOf("\"", registerTimeStartIndex);
                    String registerTime = line.substring(registerTimeStartIndex, registerTimeEndIndex);

                    String decryptedPassword = decryptPassword(storedPassword);
                    if (decryptedPassword.equals(userPassword)) {
                        if (role.equalsIgnoreCase("admin")) {
                            return new Admin(userId, userName, userPassword, registerTime, role);
                        } else {
                            return new User(userId, userName, userPassword, registerTime, role);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Login failed: Invalid username or password");
        return null;
    }

}
