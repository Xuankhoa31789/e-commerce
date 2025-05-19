package com.xuka;

import com.xuka.model.Customer;
import com.xuka.model.Order;
import com.xuka.model.Product;
import com.xuka.model.User;
import com.xuka.op.*;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class IOInterface {
    private static IOInterface instance = null;

    private IOInterface() {
    }

    /**
     * Returns the single instance of IOInterface.
     * @return IOInterface instance
     */
    public static IOInterface getInstance() {
        if (instance == null) {
            instance = new IOInterface();
        }
        return instance;
    }

    /**
     * Accept user input.
     * @param message The message to display for input prompt
     * @param numOfArgs The number of arguments expected
     * @return An array of strings containing the arguments
     */
    public String[] getUserInput(String message, int numOfArgs) {
        // Display the input prompt message

        // Split the input into arguments
        String[] inputArgs = message.split(" ");

        // Create an array to store the result with the exact number of arguments
        String[] result = new String[numOfArgs];

        // Fill the result array with input arguments or empty strings
        for (int i = 0; i < numOfArgs; i++) {
            if (i < inputArgs.length) {
                result[i] = inputArgs[i];
            } else {
                result[i] = ""; // Fill with empty strings if fewer arguments are provided
            }
        }
        return result;
    }

    /**
     * Display the login menu with options: (1) Login, (2) Register, (3) Quit.
     * The admin account cannot be registered.
     */
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("====== E-COMMERCE ======:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Quit");
            System.out.println("========================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Login selected.");
                    System.out.print("Enter username & password: ");
                    String[] credentials = getUserInput(scanner.nextLine(), 2);
                    String username = credentials[0];
                    String password = credentials[1];
                    // Call the login method with the provided username and password
                    UserOperation userOperation = UserOperation.getInstance();
                    User user = userOperation.login(username, password);
                    if (user != null) {
                        System.out.println("Login successful. Welcome, " + user.getUserName() + "!");
                        if (user.getRole().equalsIgnoreCase("admin")) {
                            adminMenu(); // Call the admin menu
                        } else {
                            System.out.println("Customer menu not implemented yet.");
                            // Call the customer menu here
                        }
                    } else {
                        System.out.println("Login failed. Invalid username or password.");
                    }
                    break;
                case "2":
                    System.out.println("Register selected.");
                    System.out.print("Enter role (customer/admin): ");
                    String role = scanner.nextLine();
                    if (role.equalsIgnoreCase("admin")) {
                        System.out.println("Admin account cannot be registered.");
                    } else if (role.equalsIgnoreCase("customer")) {
                        // Call the register method for customer here
                    } else {
                        System.out.println("Invalid role. Please try again.");
                    }
                    break;
                case "3":
                    System.out.println("Exiting the application. Goodbye!");
                    return; // Exit the method to quit the application
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Display the admin menu with options:
     * (1) Show products
     * (2) Add customers
     * (3) Show customers
     * (4) Show orders
     * (5) Generate test data
     * (6) Generate all statistical figures
     * (7) Delete all data
     * (8) Logout
     */
    public void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("====== ADMIN MENU ======");
            System.out.println("1. Show products");
            System.out.println("2. Add customers");
            System.out.println("3. Show customers");
            System.out.println("4. Show orders");
            System.out.println("5. Generate test data");
            System.out.println("6. Generate all statistical figures");
            System.out.println("7. Delete all data");
            System.out.println("8. Logout");
            System.out.println("========================");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Showing products...");
                    showProductList();
                    break;
                case "2":
                    System.out.println("Adding customers...");
                    System.out.println("(Enter customer details in the format: userName userPassword userEmail userMobile)");
                    String[] customerDetails = getUserInput(scanner.nextLine(), 4);
                    String customerName = customerDetails[0];
                    String customerPassword = customerDetails[1];
                    String customerEmail = customerDetails[2];
                    String customerMobile = customerDetails[3];
                    CustomerOperation customerOperation = CustomerOperation.getInstance();
                    boolean status = customerOperation.registerCustomer(customerName, customerPassword, customerEmail, customerMobile);
                    if (status) {
                        System.out.println("Customer added successfully.");
                    } else {
                        System.out.println("Failed to add customer. Please try again.");
                    }
                    break;
                case "3":
                    System.out.println("Showing customers...");
                    showCustomerList();
                    break;
                case "4":
                    System.out.print("Enter customer ID to show orders: ");
                    String customerId = scanner.nextLine();
                    System.out.println("Showing orders...");
                    showOrderList(customerId);
                    break;
                case "5":
                    System.out.println("Generating test data...");
                    CompletableFuture.runAsync(() -> {
                        OrderOperation orderOperation = OrderOperation.getInstance();
                        orderOperation.generateTestOrderData();
                    }).thenRun(() -> {
                        System.out.println("Test data generated successfully.");
                    }).join();
                    break;
                case "6":
                    System.out.println("Generating all statistical figures...");
                    CompletableFuture.runAsync(() -> {
                        ProductOperation productOperation = ProductOperation.getInstance();
                        productOperation.generateDiscountFigure();
                    }).thenRun(() -> {
                        System.out.println("Discount figure generated successfully.");
                    }).join();
                    CompletableFuture.runAsync(() -> {
                        ProductOperation productOperation = ProductOperation.getInstance();
                        productOperation.generateCategoryFigure();
                    }).thenRun(() -> {
                        System.out.println("Category figure generated successfully.");
                    }).join();
                    CompletableFuture.runAsync(() -> {
                        OrderOperation orderOperation = OrderOperation.getInstance();
                        orderOperation.generateAllCustomersConsumptionFigure();
                    }).thenRun(() -> {
                        System.out.println("Customer consumption figure generated successfully.");
                    }).join();
                    break;
                case "7":
                    System.out.println("Deleting all data...");
                    CompletableFuture.runAsync(() -> {
                        OrderOperation orderOperation = OrderOperation.getInstance();
                        orderOperation.deleteAllOrders();
                    }).thenRun(() -> {
                        System.out.println("Order data deleted.");
                    }).join();
                    break;
                case "8":
                    System.out.println("Logging out...");
                    return; // Exit the admin menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Display the product list with pagination.
     * The user can navigate through the pages using 'n' for next, 'p' for previous, and 'b' to go back.
     */
    public void showProductList() {
        ProductOperation productOperation = ProductOperation.getInstance();
        Scanner productScanner = new Scanner(System.in);

        int currentPage = 1; // Initial page
        while (true) {
            try {
                ProductListResult productListResult = productOperation.getProductList(currentPage);
                currentPage = productListResult.getCurrentPage(); // Update currentPage from the result
                int totalPages = productListResult.getTotalPages();

                // Display the product list
                System.out.println("====== Product List (Page " + currentPage + " / " + totalPages + ") ======");
                int n = 1;
                for (Product product : productListResult.getProducts()) {
                    System.out.println(n + ". " + product.toString());
                    n++;
                }
                System.out.println("==============================================");
                System.out.println("Page " + currentPage + " of " + totalPages);
                System.out.print("Enter 'n' for next page, 'p' for the previous page or 'b' to go back\nEnter choice: ");

                String choice = productScanner.nextLine().trim().toLowerCase();

                if (choice.equals("n")) {
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("You are already on the last page.");
                    }
                } else if (choice.equals("p")) {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("You are already on the first page.");
                    }
                } else if (choice.equals("b")) {
                    break; // Exit the product list and return to the admin menu
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Display the customer list with pagination.
     * The user can navigate through the pages using 'n' for next, 'p' for previous, and 'b' to go back.
     */
    public void showCustomerList() {
        // Implement the method to show customer list
        CustomerOperation customerOperation = CustomerOperation.getInstance();
        Scanner customerScanner = new Scanner(System.in);

        int currentPage = 1; // Initial page
        while (true) {
            try {
                CustomerListResult customerListResult = customerOperation.getCustomerList(currentPage);
                currentPage = customerListResult.getCurrentPage(); // Update currentPage from the result
                int totalPages = customerListResult.getTotalPages();

                // Display the product list
                System.out.println("====== Customer List (Page " + currentPage + " / " + totalPages + ") ======");
                int n = 1;
                for (Customer customer : customerListResult.getCustomers()) {
                    System.out.println(n + ". " + customer.toString());
                    n++;
                }
                System.out.println("==============================================");
                System.out.println("Page " + currentPage + " of " + totalPages);
                System.out.print("Enter 'n' for next page, 'p' for the previous page or 'b' to go back\nEnter choice: ");

                String choice = customerScanner.nextLine().trim().toLowerCase();

                if (choice.equals("n")) {
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("You are already on the last page.");
                    }
                } else if (choice.equals("p")) {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("You are already on the first page.");
                    }
                } else if (choice.equals("b")) {
                    break; // Exit the product list and return to the admin menu
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Display the order list for a specific customer with pagination.
     * The user can navigate through the pages using 'n' for next, 'p' for previous, and 'b' to go back.
     * @param customerId The ID of the customer whose orders are to be displayed
     */
    public void showOrderList(String customerId) {
        OrderOperation orderOperation = OrderOperation.getInstance();
        Scanner orderScanner = new Scanner(System.in);

        int currentPage = 1; // Initial page
        while (true) {
            try {
                OrderListResult orderListResult = orderOperation.getOrderList(customerId, currentPage);
                currentPage = orderListResult.getCurrentPage(); // Update currentPage from the result
                int totalPages = orderListResult.getTotalPages();

                // Display the product list
                System.out.println("====== Customer"+ customerId +" Order List (Page " + currentPage + " / " + totalPages + ") ======");
                int n = 1;
                for (Order order : orderListResult.getOrders()) {
                    System.out.println(n + ". " + order.toString());
                    n++;
                }
                System.out.println("==============================================");
                System.out.println("Page " + currentPage + " of " + totalPages);
                System.out.print("Enter 'n' for next page, 'p' for the previous page or 'b' to go back\nEnter choice: ");

                String choice = orderScanner.nextLine().trim().toLowerCase();

                if (choice.equals("n")) {
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("You are already on the last page.");
                    }
                } else if (choice.equals("p")) {
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("You are already on the first page.");
                    }
                } else if (choice.equals("b")) {
                    break; // Exit the product list and return to the admin menu
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }



}