package com.xuka.op;

import com.xuka.model.Product;

import java.util.List;

public class ProductListResult {
    private List<Product> products;
    private int currentPage;
    private int totalPages;

    public ProductListResult(List<Product> products, int currentPage, int totalPages) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "ProductListResult{" +
                "products=" + products +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                '}';
    }
}