create database RevSpeed_Project;

use revspeed_project;

CREATE TABLE IF NOT EXISTS broadband_service(
  Broadband_Service_ID INT NOT NULL AUTO_INCREMENT,
  Broadband_Plan_ID INT,
  CustomerID int,
  PRIMARY KEY (`Broadband_Service_ID`),
  foreign key fk_user_id (CustomerID) references users(CustomerID),
  foreign key fk_plan_id (Broadband_Plan_ID) references broadband_plan_details(Broadband_Plan_ID)
);
    
    CREATE TABLE IF NOT EXISTS broadband_plan_details(
  `Broadband_Plan_ID` INT NOT NULL AUTO_INCREMENT,
  `PlanName` VARCHAR(50) NOT NULL,
  `Speed` INT NULL DEFAULT NULL,
  `DataLimit` INT NULL DEFAULT NULL,
  `Price` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`Broadband_Plan_ID`),
  UNIQUE INDEX `unique_plan` (`PlanName` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci

select * from users;
select * from broadband_plan_details;
set foreign_key_checks=0;
truncate table users;

INSERT INTO broadband_plan_details (PlanName, Speed, DataLimit, Price)
VALUES 
  ('Basic Plan', 50, '100 GB', 29.99),
  ('Standard Plan', 100, '200 GB', 49.99),
  ('Premium Plan', 200, 'Unlimited', 79.99),
  ('Lite Plan', 30, '50 GB', 19.99),
  ('Ultra Plan', 500, 'Unlimited', 99.99);

INSERT INTO broadband_service (Broadband_Plan_ID, CustomerID)
VALUES 
  (1, 101),
  (2, 102),
  (3, 103),
  (1, 104),
  (4, 105);

select * from broadband_service;
ALTER TABLE users AUTO_INCREMENT = 101;

-- Insert data into users
INSERT INTO users (CustomerName, Email, PhoneNumber, Address, Password)
VALUES 
  ('charu', 'charu@gmail.com', '1234567890', '123 Main St', 'charu123'),
  ('udhaya', 'udhaya@gmail.com', '9876543210', '456 Oak St', 'udhaya123'),
  ('Bob Johnson', 'bob.johnson@example.com', '1112223333', '789 Pine St', 'password789'),
  ('Alice Williams', 'alice.williams@example.com', '9998887777', '101 Maple St', 'password101'),
  ('Charlie Brown', 'charlie.brown@example.com', '7778889999', '202 Birch St', 'password202');

set foreign_key_checks=1;

truncate table broadband_service;


delete from users where customerID = 102;
