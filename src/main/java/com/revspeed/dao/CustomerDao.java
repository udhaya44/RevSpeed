package com.revspeed.dao;

import com.revspeed.models.Customer;

import java.sql.SQLException;

public interface CustomerDao {

    // Insert a single customer
    void add(Customer customer) throws SQLException;

    int authenticateCustomer(String email, String password);

    // Retrieve a single customer by ID
     Customer getCustomer(int customerID) throws SQLException;

    // Retrieve all customers
    // List<Customer> getCustomers() throws SQLException;

    // Update customer details
     void updateCustomer(Customer customer) throws SQLException;

     boolean updatePassword( String newPassword, Customer customer) throws SQLException;

    // Delete a customer by ID
    // void delete(int customerID) throws SQLException;
}
