package com.revspeed.main;

import com.revspeed.dao.CustomerDao;
import com.revspeed.dao.CustomerDao_Impl;
import com.revspeed.dao.PlanDao;
import com.revspeed.dao.PlanDao_Impl;
import com.revspeed.models.Customer;
import com.revspeed.models.Plan;
import com.revspeed.service.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import static com.revspeed.dao.CustomerDao_Impl.getValidEmail;
import static com.revspeed.dao.CustomerDao_Impl.validatePhoneNumber;
import static com.revspeed.dao.CustomerDao_Impl.validatePassword;
import static com.revspeed.dao.PlanDao_Impl.displayPlans;
import static com.revspeed.dao.PlanDao_Impl.displaySubscribedPlans;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    static {

        System.out.println("******\t\tWelcome to BrightSpeed\t\t******");
        logger.info("Application Started {}", Application.class.getSimpleName());
    }
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("1.Login\n2.Register\n3.Exit");
                int inputChoice;
                try {
                    inputChoice = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.err.println("Invalid input! Please enter a number: ");
                    sc.nextLine(); // Clear the input buffer
                    continue; // Restart the loop
                }

                switch (inputChoice) {
                    case 1:
                        try {
                            Connection connection = DatabaseConnection.getConnection();
                            System.out.println("******\t\tWelcome to the Login Page\t\t******");
                            System.out.println("Please Enter your EmailID");
                            String email = sc.next();
                            sc.nextLine();
                            System.out.println("Please Enter your Password");
                            String password = sc.next();

                            try {
                                CustomerDao customerDao = new CustomerDao_Impl(connection);
                                PlanDao planDao = new PlanDao_Impl(connection);

                                if (customerDao.authenticateCustomer(email, password) != -1) {
                                    int customerID = customerDao.authenticateCustomer(email, password);
                                    System.out.println("Successfully logged in");
                                    logger.info("Successfully logged in of the Application");
                                    int methodChoice;
                                    do {
                                        System.out.println("Enter the option you want to perform:");
                                        System.out.println("1. View Profile ");
                                        System.out.println("2. View Plans");
                                        System.out.println("3. Manage Subscriptions");
                                        System.out.println("4. Apply for a New Plan");
                                        System.out.println("5. Opt Out of a Plan");
                                        System.out.println("6. Logout");
                                        methodChoice = sc.nextInt();
                                        switch (methodChoice) {
                                            case 1:
                                                System.out.println("******\t\tWelcome to your Profile\t\t******");
                                                System.out.println();
                                                Customer profile = customerDao.getCustomer(customerID);
                                                System.out.println("| Customer ID            | Customer Name          | Customer Email         | Customer Phone Number | Customer Address                     |");
                                                System.out.println("|========================|========================|========================|========================|=====================================|");
                                                System.out.printf("| %-22s | %-22s | %-22s | %-22s | %-35s |\n",
                                                        profile.getCustomerID(), profile.getCustomerName(), profile.getEmail(),
                                                        profile.getPhoneNumber(), profile.getAddress());
                                                System.out.println();
                                                // option inside profile page!..
                                                int profileOption;
                                                do {
                                                    System.out.println("Choose an option:");
                                                    System.out.println("1. Update Profile");
                                                    System.out.println("2. Change Password");
                                                    System.out.println("3. Exit Profile Options");
                                                    profileOption = sc.nextInt();

                                                    switch (profileOption) {
                                                        case 1:
                                                            // Implement logic for updating profile
                                                            System.out.println("******\t\tProfile Update Page\t\t******");

                                                            Customer updateCustomer = new Customer();
                                                            updateCustomer.setCustomerID(customerID);

                                                            System.out.println("Please Re-enter the Name:");
                                                            updateCustomer.setCustomerName(sc.next());

                                                            String validatedPhone = null;
                                                            while (validatedPhone == null) {
                                                                System.out.println("Enter a valid 10-digit phone number:");
                                                                String phone = sc.next();
                                                                validatedPhone = validatePhoneNumber(phone);
                                                            }
                                                            updateCustomer.setPhoneNumber(validatedPhone);
                                                            sc.nextLine();


                                                            System.out.println("Enter the Address (max 25 characters):");
                                                            String addressInput = sc.nextLine();
                                                            while (addressInput.length() > 25) {
                                                                System.out.println("Address exceeds the maximum allowed characters. Please enter a shorter address:");
                                                                addressInput = sc.nextLine();
                                                            }
                                                            updateCustomer.setAddress(addressInput);

                                                            customerDao.updateCustomer(updateCustomer);

                                                            break;
                                                        case 2:
                                                            System.out.println("******\t\tPassword Update Page\t\t******");
                                                            Customer updatePasswordCustomer = new Customer();
                                                            updatePasswordCustomer.setCustomerID(customerID);
                                                            System.out.println("Please Enter your Current Password");
                                                            String currentPassword = sc.next();
                                                            sc.nextLine();
                                                            boolean isPasswordCorrect = validatePassword(currentPassword, updatePasswordCustomer, connection);
                                                            if (isPasswordCorrect) {
                                                                System.out.println("Please enter your new Password");
                                                                String newPassword = sc.next();
                                                                if (currentPassword.equals(newPassword)) {
                                                                    System.out.println("New password cannot be the same as the current password. Please enter a different password.");
                                                                } else {
                                                                    boolean isUpdateSuccessful = customerDao.updatePassword(newPassword, updatePasswordCustomer);
                                                                    if (isUpdateSuccessful) {
                                                                        System.out.println("Password updated successfully.");
                                                                        break;
                                                                    } else {
                                                                        System.out.println("Password update failed. Please try again.");
                                                                    }
                                                                }
                                                            } else {
                                                                System.out.println("Enter Correct Current password!.");
                                                            }
                                                            break;
                                                        case 3:
                                                            System.out.println("Exiting Profile Options");
                                                            break;
                                                        default:
                                                            System.out.println("Invalid option. Please choose again.");
                                                            break;
                                                    }
                                                } while (profileOption != 3);

                                                break;

                                            case 2:
                                                List<Plan> allAvailablePlans = planDao.getAllPlans();
                                                displayPlans(allAvailablePlans);
                                                break;

                                            case 3:
                                                List<Plan> viewSubscribed = displaySubscribedPlans(customerID, planDao, true);
                                                break;

                                            case 4:
                                                List<Plan> customerSubscribedPlan = displaySubscribedPlans(customerID, planDao, true);

                                                if (customerSubscribedPlan.isEmpty()) {
                                                    List<Plan> planOptions = planDao.getAllPlans();
                                                    displayPlans(planOptions);

                                                    System.out.println("Please select the plan you want to subscribe:");
                                                    int planChoice = sc.nextInt();
                                                    if (planChoice != 0 && planChoice <= planOptions.size()) {
                                                        planDao.addPlan(customerID, planChoice);
                                                    } else {
                                                        System.out.println("Please choose a plan available in the plan table");
                                                    }
                                                }else{
                                                    System.out.println("You have a existing plan, The details of your current plan is:");
                                                }

                                                break;

                                            case 5:
                                                List<Plan> displayPlan = displaySubscribedPlans(customerID, planDao, true);
                                                if (!displayPlan.isEmpty()) {
                                                    System.out.println("Are you sure you want to unsubscribe your current plan ");
                                                    System.out.println("1. Yes");
                                                    System.out.println("2. No");
                                                    int outChoice = sc.nextInt();
                                                    switch (outChoice) {
                                                        case 1:
                                                            int planIdToDelete = displayPlan.getFirst().getPlanId();
                                                            planDao.deletePlan(planIdToDelete);
                                                            break;
                                                        case 2:
                                                            System.out.println("Thank you for continuing our subscription");
                                                            break;
                                                        default:
                                                            System.out.println("Enter correct choice");
                                                    }
                                                } else {
                                                    System.out.println("No Subscription was found. \n please subscribe a new plan. ");
                                                }
                                                break;

                                            case 6:
                                                // Logout
                                                System.out.println("Logging out...");
                                                logger.info("Logging out of the Application");
                                                // Add code to handle the logout process
                                                break;

                                            default:
                                                System.out.println("Invalid option");
                                                // Handle cases where methodChoice is not within the specified options
                                        }
                                    } while (methodChoice != 6);

                                } else {
                                    System.out.println("Authentication failed. Invalid email or password.");
                                    logger.error("Authentication failed");
                                    // Handle authentication failure
                                }
                            } catch (SQLException e) {
                                System.err.println("Database error: " + e.getMessage());
                                logger.error("Database error during operations");
                                // Handle database-related exceptions
                            } catch (InputMismatchException e) {
                                // Handle input-related exceptions
                                logger.error("Invalid input error during operations");
                                System.err.println("Invalid input! Please enter a number " + e.getMessage());
                                sc.nextLine();
                            } catch (Exception e) {
                                logger.error("Unexpected error during operations");
                                System.err.println("An unexpected error occurred: " + e.getMessage());
                                // Handle other unexpected exceptions
                            }
                        } catch (Exception e) {
                            System.err.println("An unexpected error occurred: " + e.getMessage());
                            logger.error("An unexpected error occurred during log in");
                            // Handle any unexpected exceptions outside the inner try-catch block
                        }

                        break;

                    case 2:
                        try {
                            Connection connection = DatabaseConnection.getConnection();

                            if (connection != null) {
                                System.out.println("Registration");
                                System.out.println("==================");

                                CustomerDao customerDao = new CustomerDao_Impl(connection);

                                Customer newCustomer = new Customer();

                                System.out.println("Enter the Name:");
                                newCustomer.setCustomerName(sc.next());

                                String validatedPhone = null;
                                while (validatedPhone == null) {
                                    System.out.println("Enter a valid 10-digit phone number:");
                                    String phone = sc.next();
                                    validatedPhone = validatePhoneNumber(phone);
                                }
                                newCustomer.setPhoneNumber(validatedPhone);

                                String validatedEmail = getValidEmail(sc, connection); // Handle email validation
                                newCustomer.setEmail(validatedEmail);
                                sc.nextLine();

                                System.out.println("Enter the Address (max 25 characters):");
                                String addressInput = sc.nextLine();
                                while (addressInput.length() > 25) {
                                    System.out.println("Address exceeds the maximum allowed characters. Please enter a shorter address:");
                                    addressInput = sc.nextLine();
                                }
                                newCustomer.setAddress(addressInput);

                                System.out.println("Enter the password:");
                                newCustomer.setPassword(sc.nextLine());

                                customerDao.add(newCustomer);

                                System.out.println("==================");
                            }
                        } catch (SQLException e) {
                            System.err.println("Database error: " + e.getMessage());
                            logger.error("Exception in getting Database during registration");
                            // Handle database-related exceptions
                        } catch (InputMismatchException e) {
                            System.err.println("Invalid input: " + e.getMessage());
                            logger.error("Exception in input during registration");
                            // Handle input-related exceptions
                        } catch (Exception e) {
                            System.err.println("An unexpected error occurred: " + e.getMessage());
                            logger.error("unexpected error in registration process");
                            // Handle other unexpected exceptions
                        }
                        break;

                    case 3:
                        System.exit(0);
                        logger.error("Application terminated");
                        break;

                    default:
                        System.out.println("Invalid output");
                        logger.error("Entered invalid input in Main page");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Unexpected exception happened " + e.getMessage());
            logger.error("Unexpected Exception happened in application");
        }
    }
}
