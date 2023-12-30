package com.revspeed.dao;

import com.revspeed.models.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

class CustomerDaoImplTest {

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private CustomerDao_Impl customerDao;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        customerDao = new CustomerDao_Impl(connectionMock);
    }

    @Test
    void testAddSuccess() throws SQLException {
        // Arrange
        Customer customer = new Customer(/* Set customer details */);

        // Mock behavior
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // Act
        customerDao.add(customer);

        // Assert
        // You can assert the expected behavior, e.g., print statement or log messages
        // Verify that the SQL query and setString were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setString(eq(1), eq(customer.getCustomerName()));
        verify(preparedStatementMock).setString(eq(2), eq(customer.getEmail()));
        verify(preparedStatementMock).setString(eq(3), eq(customer.getPhoneNumber()));
        verify(preparedStatementMock).setString(eq(4), eq(customer.getAddress()));
        verify(preparedStatementMock).setString(eq(5), eq(customer.getPassword()));
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    void testUpdateCustomerSuccess() throws SQLException {
        // Arrange
        Customer customer = new Customer(/* Set customer details */);

        // Mock behavior
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        // Act
        customerDao.updateCustomer(customer);

        // Assert
        // Verify that the SQL query and setString were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setString(eq(1), eq(customer.getCustomerName()));
        verify(preparedStatementMock).setString(eq(2), eq(customer.getPhoneNumber()));
        verify(preparedStatementMock).setString(eq(3), eq(customer.getAddress()));
        verify(preparedStatementMock).setInt(eq(4), eq(customer.getCustomerID()));
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    void testGetCustomerSuccess() throws SQLException {
        // Arrange
        int customerId = 1;

        // Mock behavior
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("CustomerID")).thenReturn(customerId);
        when(resultSetMock.getString("CustomerName")).thenReturn("John Doe");
        when(resultSetMock.getString("email")).thenReturn("john.doe@example.com");
        when(resultSetMock.getString("phoneNumber")).thenReturn("1234567890");
        when(resultSetMock.getString("address")).thenReturn("123 Main St");

        // Act
        Customer result = customerDao.getCustomer(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerID());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
        assertEquals("123 Main St", result.getAddress());

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(customerId));
        verify(preparedStatementMock).executeQuery();
    }

    @Test
    void testGetCustomerFailure() throws SQLException {
        // Arrange
        int customerId = 1;

        // Mock behavior
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(false);

        // Act
        Customer result = customerDao.getCustomer(customerId);

        // Assert
        assertNull(result);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(customerId));
        verify(preparedStatementMock).executeQuery();
    }
}
