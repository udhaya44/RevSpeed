package com.revspeed.dao;

import com.revspeed.models.Customer;
import com.revspeed.service.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CustomerDao_Impl implements CustomerDao {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDao_Impl.class);
    private final Connection connection;

    public CustomerDao_Impl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Customer customer)  {
            String query = "INSERT INTO Users (customerName, email, phoneNumber, address, password) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, customer.getCustomerName());
                preparedStatement.setString(2, customer.getEmail());
                preparedStatement.setString(3, customer.getPhoneNumber());
                preparedStatement.setString(4, customer.getAddress());
                preparedStatement.setString(5, customer.getPassword());
                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    System.out.println( "Registration successful" );
                    logger.info("new user got registered ");
                }
            } catch (SQLException e) {
                System.out.println("Registration failed: " + e.getMessage());
                logger.error("SQL Error happened in add method during registration");
            }
        }

    @Override
    public int authenticateCustomer(String email, String password) {
        String query = "SELECT CustomerID, email, password FROM Users WHERE email = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the user's email and password from the database
                    String dbEmail = resultSet.getString("email");
                    String dbPassword = resultSet.getString("password");

                    // Perform a case-sensitive comparison
                    if (email.equals(dbEmail) && password.equals(dbPassword)) {
                        return resultSet.getInt("CustomerID");
                    }
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace(); // Consider logging the exception instead
            logger.error("SQL Error happened in authenticate method during registration");
        }
        return -1; // Credentials are not valid
    }


    @Override
    public void updateCustomer(Customer customer){

        String query = "Update Users set CustomerName=?, PhoneNumber=?,  Address=? where CustomerID=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getPhoneNumber());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setInt(4, customer.getCustomerID());
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println( "Update successful" );
                logger.info("User got Updated");
            }
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            logger.error("SQL Error happened in update method in customerDAO");

        }
    }

    @Override
    public boolean updatePassword( String newPassword , Customer customer){
        String query = "Update Users set Password = ? where CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, customer.getCustomerID());
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                System.out.println( "Update password successful" );
                logger.info("password got Updated");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Update password failed: " + e.getMessage());
        }
        return false;
    }

    public static boolean validatePassword( String oldPassword, Customer customer, Connection connection){
        String query = "SELECT CustomerID FROM Users WHERE Password = ? AND CustomerID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, oldPassword);
            preparedStatement.setInt(2, customer.getCustomerID());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace(); // Consider logging the exception instead
        }
        return false;
    }


    public Customer getCustomer(int CustomerID){
        String query ="Select * from users where CustomerID =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,CustomerID);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next() ){
                Customer customer = new Customer();
                customer.setCustomerID(result.getInt("CustomerID"));
                customer.setCustomerName(result.getString("CustomerName") );
                customer.setEmail(result.getString("email"));
                customer.setPhoneNumber(result.getString("phoneNumber"));
                customer.setAddress(result.getString("address"));
                //logging
                logger.info("returning records of the customer");

                return customer;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String validatePhoneNumber(String phoneNumber ){
        if (Pattern.matches("\\d{10}", phoneNumber)) {
            return phoneNumber;
        }else {
            System.out.println("Invalid Phone Number.\t Please enter a 10-digit integer ");
            return null;
        }
    }

    public static String getValidEmail(Scanner sc, Connection connection) {
        String validatedEmail = null;
        boolean isvalidInput = false;
        while (!isvalidInput) {
            try {
                System.out.println("Enter a valid email address:");
                String email = sc.next();

                if (validateEmail(email, connection)) {
                    validatedEmail = email;
                    isvalidInput = true;
                } else {
                    // The validateEmail method will print error messages.
                    // You don't need to print anything specific here.
                }
            }catch (InputMismatchException e){
                System.err.println("Invalid input! Please enter a valid email address.");
                sc.nextLine();
            }
        }
        return validatedEmail;
    }

    private static boolean validateEmail(String email, Connection connection) {
        // Regular expression for a simple email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regular expression
        Pattern pattern = Pattern.compile(emailRegex);

        // Check if the provided email matches the pattern
        if (!pattern.matcher(email).matches()) {
            System.out.println("Invalid email format. Please enter a valid email address.");
            return false;
        }

        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count == 0) {
                        return true; // Email is valid and available
                    } else {
                        System.out.println("Email already exists. Please choose a different one.");
                        return false; // Email is valid, but already exists
                    }
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        }

        return false; // Default to false in case of exceptions
    }

}
