package com.xuka.op;

import com.xuka.model.Product;
import com.xuka.utils.CategoryChartApp;
import com.xuka.utils.PieChartApp;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductOperation{
    private static ProductOperation instance = null;
    private List<Map.Entry<String, Integer>> sortedCategories; // Class-level variable


    private ProductOperation() {
    }

    /**
     * Returns the single instance of ProductOperation.
     * @return ProductOperation instance
     */
    public static ProductOperation getInstance() {
        if (instance == null) {
            instance = new ProductOperation();
        }
        return instance;
    }
    /**
     * Extracts product information from the given product data files.
     * The data is saved into the data/products.txt file.
     */
    public List<Product> extractProductsFromFiles() {
        List<Product> products = new ArrayList<>();
        String filePath = "src/main/data/products.txt";

        try {
            // Read the entire file content as a string
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Split the content into individual JSON objects (one per line)
            String[] lines = content.split("\n");

            // Parse each line as a JSON object and create Product instances
            for (String line : lines) {
                JSONObject jsonObject = new JSONObject(line);

                Product product = new Product(
                        jsonObject.getString("pro_id"),
                        jsonObject.getString("pro_model"),
                        jsonObject.getString("pro_category"),
                        jsonObject.getString("pro_name"),
                        jsonObject.getDouble("pro_current_price"),
                        jsonObject.getDouble("pro_raw_price"),
                        jsonObject.getDouble("pro_discount"),
                        jsonObject.getInt("pro_likes_count")
                );

                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Retrieves one page of products from the database.
     * One page contains a maximum of 10 items.
     * @param pageNumber The page number to retrieve
     * @return A list of Product objects, current page number, and total pages
     */
    public ProductListResult getProductList(int pageNumber) {
        final int PAGE_SIZE = 10; // Maximum items per page
        List<Product> allProducts = extractProductsFromFiles(); // Retrieve all products
        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / PAGE_SIZE);

        // Validate page number
        if (pageNumber < 1 || pageNumber > totalPages) {
            throw new IllegalArgumentException("Invalid page number: " + pageNumber);
        }

        // Calculate start and end indices for the current page
        int startIndex = (pageNumber - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalProducts);

        // Get the sublist for the current page
        List<Product> currentPageProducts = allProducts.subList(startIndex, endIndex);

        // Return the result
        return new ProductListResult(currentPageProducts, pageNumber, totalPages);
    }

    /**
     * Deletes the product from the system based on the provided product_id.
     * @param productId The ID of the product to delete
     * @return true if successful, false otherwise
     */

    public boolean deleteProduct(String productId) {
        String filePath = "src/main/data/products.txt";
        boolean isDeleted = false;

        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<String> updatedLines = new ArrayList<>();

            // Iterate through each line and filter out the product with the given ID
            for (String line : lines) {
                JSONObject jsonObject = new JSONObject(line);
                if (!jsonObject.getString("pro_id").equals(productId)) {
                    updatedLines.add(line);
                } else {
                    isDeleted = true; // Mark as deleted if the product is found
                }
            }

            // Write the updated lines back to the file
            Files.write(Paths.get(filePath), updatedLines);

        } catch (IOException e) {
            e.printStackTrace();
            return false; // Return false if an error occurs
        }

        return isDeleted;
    }
    /**
     * Retrieves all products whose name contains the keyword (case insensitive).
     * @param keyword The search keyword
     * @return A list of Product objects matching the keyword
     */
    public List<Product> getProductListByKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        List<Product> allProducts = extractProductsFromFiles(); // Retrieve all products
        List<Product> matchingProducts = new ArrayList<>();

        // Filter products by keyword (case insensitive)
        for (Product product : allProducts) {
            if (product.getProName().toLowerCase().contains(keyword.toLowerCase())) {
                matchingProducts.add(product);
            }
        }

        return matchingProducts;
    }

    /**
     * Returns one product object based on the given product_id.
     * @param productId The ID of the product to retrieve
     * @return A Product object or null if not found
     */
    public Product getProductById(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }

        String filePath = "src/main/data/products.txt";

        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Iterate through each line to find the matching product
            for (String line : lines) {
                JSONObject jsonObject = new JSONObject(line);

                if (jsonObject.getString("pro_id").equals(productId)) {
                    // Create and return the matching Product object
                    return new Product(
                            jsonObject.getString("pro_id"),
                            jsonObject.getString("pro_model"),
                            jsonObject.getString("pro_category"),
                            jsonObject.getString("pro_name"),
                            jsonObject.getDouble("pro_current_price"),
                            jsonObject.getDouble("pro_raw_price"),
                            jsonObject.getDouble("pro_discount"),
                            jsonObject.getInt("pro_likes_count")
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Return null if no matching product is found
    }

    /**
     * Generates a bar chart showing the total number of products
     * using a JavaFX BarChart/Application.
     * for each category in descending order.
     * Saves the figure into the data/figure folder.
     */
    public void generateCategoryFigure() {
        List<Product> products = extractProductsFromFiles();

        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Product product : products) {
            categoryCounts.put(product.getProCategory(),
                    categoryCounts.getOrDefault(product.getProCategory(), 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedCategories = categoryCounts.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());

        // Pass the sorted categories to the JavaFX application
        CategoryChartApp.setSortedCategories(sortedCategories);

        // Launch the JavaFX application
        CategoryChartApp.launch(CategoryChartApp.class);
    }

    /**
     * Generates a chart displaying the sum of products' likes_count
     * for each category in ascending order.
     * Saves the figure into the data/figure folder.
     */
    public void generateDiscountFigure() {
        List<Product> products = extractProductsFromFiles();

        // Categorize products based on discount values
        int lessThan30 = 0;
        int between30And60 = 0;
        int greaterThan60 = 0;

        for (Product product : products) {
            double discount = product.getProDiscount();
            if (discount < 30) {
                lessThan30++;
            } else if (discount <= 60) {
                between30And60++;
            } else {
                greaterThan60++;
            }
        }
        System.out.println(lessThan30 + " " + between30And60 + " " + greaterThan60);
        // Set the values in the PieChartApp
        PieChartApp.setLessThan30(lessThan30);
        PieChartApp.setBetween30And60(between30And60);
        PieChartApp.setGreaterThan60(greaterThan60);
        // Launch the JavaFX application
        PieChartApp.launch(PieChartApp.class);
    }

    /**
     * Removes all product data in the `data/products.txt` file.
     */
    public void deleteAllProducts() {
        String filePath = "src/main/data/products.txt";

        try {
            // Overwrite the file with empty content
            Files.write(Paths.get(filePath), new byte[0]);
            System.out.println("All products have been deleted.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to delete all products.");
        }
    }
}
