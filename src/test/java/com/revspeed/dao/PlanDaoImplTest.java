package com.revspeed.dao;

import com.revspeed.models.Plan;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PlanDaoImplTest {

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    @InjectMocks
    private PlanDao_Impl planDao;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        // Mock the behavior of connection.prepareStatement to return the preparedStatementMock
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        // Mock the behavior of preparedStatement.executeQuery to return the resultSetMock
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
    }


    @Test
    public void testUpdatePlan() throws SQLException {
        // Arrange
        int customerId = 1;
        int planId = 101;
        when(preparedStatementMock.executeUpdate()).thenReturn(1); // Simulate successful update

        // Act
        planDao.updatePlan(customerId, planId);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(planId));
        verify(preparedStatementMock).setInt(eq(2), eq(customerId));
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testUpdatePlanFailure() throws SQLException {
        // Arrange
        int customerId = 1;
        int planId = 101;
        when(preparedStatementMock.executeUpdate()).thenReturn(0); // Simulate failed update

        // Act
        planDao.updatePlan(customerId, planId);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(planId));
        verify(preparedStatementMock).setInt(eq(2), eq(customerId));
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testAddPlan() throws SQLException {
        // Arrange
        int customerId = 1;
        int planId = 101;
        when(preparedStatementMock.executeUpdate()).thenReturn(1); // Simulate successful insertion

        // Act
        planDao.addPlan(customerId, planId);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(planId));
        verify(preparedStatementMock).setInt(eq(2), eq(customerId));
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testAddPlanFailure() throws SQLException {
        // Arrange
        int customerId = 1;
        int planId = 101;
        when(preparedStatementMock.executeUpdate()).thenReturn(0); // Simulate failed insertion

        // Act
        planDao.addPlan(customerId, planId);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(planId));
        verify(preparedStatementMock).setInt(eq(2), eq(customerId));
        verify(preparedStatementMock).executeUpdate();
    }


    @Test
    public void testDeletePlan() throws SQLException {
        // Arrange
        int planId = 101;
        when(preparedStatementMock.executeUpdate()).thenReturn(1); // Simulate successful deletion

        // Act
        planDao.deletePlan(planId);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(planId));
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeletePlanFailure() throws SQLException {
        // Arrange
        int planId = 101;
        when(preparedStatementMock.executeUpdate()).thenReturn(0); // Simulate failed deletion

        // Act
        planDao.deletePlan(planId);

        // Verify that the SQL query and setInt were called with the expected parameters
        verify(connectionMock).prepareStatement(anyString());
        verify(preparedStatementMock).setInt(eq(1), eq(planId));
        verify(preparedStatementMock).executeUpdate();
    }

}
