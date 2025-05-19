package com.xuka.op;

import com.xuka.model.Admin;
import com.xuka.model.User;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    /**
     * Generates and returns a 10-digit unique user id starting with 'u_'
     * every time when a new user is registered.
     * @return A string value in the format 'u_10digits', e.g., 'u_1234567890'
     */
    public String generateUniqueUserId() {
        String filePath = "src/main/data/users.txt";
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    String userId = jsonObject.getString("user_id");
                    if (userId.startsWith("u_")) {
                        int numericId = Integer.parseInt(userId.substring(2));
                        maxId = Math.max(maxId, numericId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Increment the max ID to generate a new unique ID
        int newId = maxId + 1;
        return String.format("u_%010d", newId);
    }
    /**
     * Encode a user-provided password.
     * Encryption steps:
     * 1. Generate a random string with a length equal to two times
     * the length of the user-provided password. The random string
     * should consist of characters chosen from a-zA-Z0-9.
     * 2. Combine the random string and the input password text to
     * create an encrypted password, following the rule of selecting
     * two letters sequentially from the random string and
     * appending one letter from the input password. Repeat until all
     * characters in the password are encrypted. Finally, add "^^" at
     * the beginning and "$$" at the end of the encrypted password.
     *
     * @param userPassword The password to encrypt
     * @return Encrypted password
     */
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
    /**
     * Decode the encrypted password with a similar rule as the encryption
     method.
     * @param encryptedPassword The encrypted password to decrypt
     * @return Original user-provided password
     */
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
    /**
     * Verify whether a user is already registered or exists in the system.
     * @param userName The username to check
     * @return true if exists, false otherwise
     */
    public boolean checkUsernameExist(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        String filePath = "src/main/data/users.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);
                    if (jsonObject.has("user_name") && jsonObject.getString("user_name").equals(userName)) {
                        return true; // Username exists
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Username does not exist
    }
    /**
     * Validate the user's name. The name should only contain letters or
     * underscores, and its length should be at least 5 characters.
     * @param userName The username to validate
     * @return true if valid, false otherwise
     */
    public boolean validateUsername(String userName) {
        if (userName == null || userName.isEmpty()) {
            return false; // Username cannot be null or empty
        }

        // Check if the username matches the criteria
        return userName.matches("^[a-zA-Z0-9_]{5,}$");
    }
    /**
     * Validate the user's password. The password should contain at least
     * one letter (uppercase or lowercase) and one number. The length
     * must be greater than or equal to 5 characters.
     * @param userPassword The password to validate
     * @return true if valid, false otherwise
     */
    public boolean validatePassword(String userPassword) {
        if (userPassword == null || userPassword.isEmpty()) {
            return false; // Password cannot be null or empty
        }

        // Check if the password matches the criteria
        return userPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d)[^\\s]{5,}$");
    }
    /**
     * Verify the provided user's name and password combination against
     * stored user data to determine the authorization status.
     * @param userName The username for login
     * @param userPassword The password for login
     * @return A User object (Customer or Admin) if successful, null otherwise
     */
    public User login(String userName, String userPassword) {
        if (userName == null || userPassword == null || userName.isEmpty() || userPassword.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty");
        }

        String filePath = "src/main/data/users.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    JSONObject jsonObject = new JSONObject(line);

                    // Check if the username matches
                    if (jsonObject.has("user_name") && jsonObject.getString("user_name").equals(userName)) {
                        String storedPassword = jsonObject.getString("user_password");
                        String decryptedPassword = decryptPassword(storedPassword);

                        // Check if the password matches
                        if (decryptedPassword.equals(userPassword)) {
                            String userId = jsonObject.getString("user_id");
                            String registerTime = jsonObject.getString("user_register_time");
                            String role = jsonObject.getString("user_role");

                            // Return the appropriate user object based on the role
                            if (role.equalsIgnoreCase("admin")) {
                                return new Admin(userId, userName, userPassword, registerTime, role);
                            } else {
                                return new User(userId, userName, userPassword, registerTime, role);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Invalid username or password");
    }
}
