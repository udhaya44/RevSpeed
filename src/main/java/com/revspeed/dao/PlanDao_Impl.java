package com.revspeed.dao;

import com.revspeed.models.Customer;
import com.revspeed.models.Plan;
import com.revspeed.service.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanDao_Impl implements PlanDao{

    private static final Logger logger = LoggerFactory.getLogger(PlanDao_Impl.class);
    private final Connection connection;

    public PlanDao_Impl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Plan> getSubscribedPlansForCustomer(int customerId) {
        List<Plan> subscribedPlans = new ArrayList<>();

        String query = "SELECT bpd.* " +
                "FROM broadband_plan_details bpd " +
                "JOIN broadband_service bs ON bpd.Broadband_Plan_ID = bs.Broadband_Plan_ID " +
                "WHERE bs.CustomerID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Create a Plan object with details from the result set
                    Plan plan = new Plan();
                    plan.setPlanId(resultSet.getInt("Broadband_Plan_ID"));
                    plan.setPlanName(resultSet.getString("PlanName"));
                    plan.setSpeed(resultSet.getInt("Speed"));
                    plan.setDataLimit(resultSet.getString("DataLimit"));
                    plan.setPrice(resultSet.getDouble("Price"));
                    logger.info("returning plans records of the customer");
                    subscribedPlans.add(plan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            logger.error("SQL Error happened in getSubscribedPlansForCustomer in planDAO");
        }

        return subscribedPlans;
    }

    @Override
    public List<Plan> getAllPlans(){
        List<Plan> allPlans = new ArrayList<>();

        String query = "Select * from broadband_plan_details";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Create a Plan object with details from the result set
                    Plan plan = new Plan();
                    plan.setPlanId(resultSet.getInt("Broadband_Plan_ID"));
                    plan.setPlanName(resultSet.getString("PlanName"));
                    plan.setSpeed(resultSet.getInt("Speed"));
                    plan.setDataLimit(resultSet.getString("DataLimit"));
                    plan.setPrice(resultSet.getDouble("Price"));
                    //logging
                    logger.info("returning all plan records of the customer");
                    allPlans.add(plan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            logger.error("SQL Error happened in getAllPlans in planDAO");
        }
        return allPlans;
    }

    @Override
    public void updatePlan(int customerID, int planID)  {
        String query = "Update broadband_service set Broadband_Plan_id =?, CustomerID = ? Where CustomerID =? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,planID);
            preparedStatement.setInt(2,customerID);
            preparedStatement.setInt(3,customerID);
            int result = preparedStatement.executeUpdate();
            if(result > 0){
                logger.info("Update plan successful");
                System.out.println("Update Successful");
            }else {
                System.out.println("Update Failed");
            }
        } catch (SQLException e) {
            logger.error("SQL Error happened in updatePlan in planDAO");
            throw new RuntimeException(e);

        }
    }

    @Override
    public void addPlan(int customerID, int planID){
        String query = "Insert into  broadband_service(Broadband_Plan_ID, CustomerID) value(?,?) ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,planID);
            preparedStatement.setInt(2,customerID);
            int result = preparedStatement.executeUpdate();
            if(result > 0){
                logger.info("plan added successfully");
                System.out.println("Update Successful");
            }else {
                System.out.println("Update Failed");
            }
        } catch (SQLException e) {
            logger.error("SQL Error happened in addPlan in planDAO");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePlan(int planID){

        String query = "Delete from broadband_service where Broadband_Plan_ID =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,planID);
            int result = preparedStatement.executeUpdate();
            if(result > 0){
                logger.info("Deletion successfully");
                System.out.println("Deletion Successful");
            }else {
                System.out.println("Deletion Failed");
            }
        } catch (SQLException e) {
            logger.error("SQL Error happened in deletePlan in planDAO");
            throw new RuntimeException(e);
        }
    }


    public static void displayPlans(List<Plan> plans) {
        if (!plans.isEmpty()) {
            System.out.println("| Plan ID | Plan Name            | Plan Speed | Plan Data Limit | Plan Price per month |");
            System.out.println("|=========|======================|============|=================|======================|");

            for (Plan p : plans) {
                System.out.printf("| %-7d | %-20s | %-10d | %-15s | %-20.2f |\n",
                        p.getPlanId(), p.getPlanName(), p.getSpeed(), p.getDataLimit(), p.getPrice());
            }
        } else {
            System.out.println("Currently No Available Plans");
        }
    }

    public static List<Plan> displaySubscribedPlans(int customerID, PlanDao planDao, boolean detailed) {
        try {
            List<Plan> subscribedPlans = planDao.getSubscribedPlansForCustomer(customerID);

        if (!subscribedPlans.isEmpty()) {
            System.out.println("| Plan ID | Plan Name            | Plan Speed | Plan Data Limit | Plan Price per month |");
            System.out.println("|=========|======================|============|=================|======================|");

            for (Plan p : subscribedPlans) {
                System.out.printf("| %-7d | %-20s |", p.getPlanId(), p.getPlanName());

                if (detailed) {
                    // Display additional details for the plan (Speed, Data Limit, Price)
                    System.out.printf(" %-10d | %-15s | %-20.2f |%n", p.getSpeed(), p.getDataLimit(), p.getPrice());
                } else {
                    System.out.println(); // Print a new line without additional details
                }
            }

            return subscribedPlans;
        } else {
            System.out.println("Currently No Plans Subscribed");
        }
        return new ArrayList<>();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }






}
