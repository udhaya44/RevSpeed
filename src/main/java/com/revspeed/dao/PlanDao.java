package com.revspeed.dao;

import com.revspeed.models.Plan;

import java.sql.SQLException;
import java.util.List;

public interface PlanDao {

    List<Plan> getSubscribedPlansForCustomer(int customerID) throws SQLException;

    // Retrieve all plans
     List<Plan> getAllPlans() throws SQLException;

     void addPlan(int customerID,int planID) throws SQLException;
    // Update customer details
     void updatePlan(int  customerID, int planID) throws SQLException;

    // Delete a plan by ID
     void deletePlan(int planID) throws SQLException;




}
