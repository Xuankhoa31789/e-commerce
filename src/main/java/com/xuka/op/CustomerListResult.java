package com.xuka.op;

import com.xuka.model.Customer;

import java.util.List;

public class CustomerListResult {
    private List<Customer> customers;
    private int currentPage;
    private int totalPages;

    public CustomerListResult(List<Customer> customers, int currentPage, int totalPages) {
        this.customers = customers;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "CustomerListResult{" +
                "customers=" + customers +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                '}';
    }
}