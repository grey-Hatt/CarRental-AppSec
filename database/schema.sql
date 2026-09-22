-- Car Rental System - database schema and sample data
-- Run with:  mysql -u root -p < database/schema.sql

CREATE DATABASE IF NOT EXISTS car_rental CHARACTER SET utf8mb4;
USE car_rental;

DROP TABLE IF EXISTS rented_cars;
DROP TABLE IF EXISTS cars;

-- The fleet
CREATE TABLE cars (
    reg_id         INT            PRIMARY KEY,
    car_name       VARCHAR(100)   NOT NULL,
    car_brand      VARCHAR(100)   NOT NULL,
    car_model      VARCHAR(50)    NOT NULL,
    car_engine_no  VARCHAR(50)    NOT NULL,
    car_chassis_no VARCHAR(50)    NOT NULL,
    car_status     VARCHAR(20)    NOT NULL DEFAULT 'available',  -- 'available' or 'rented'
    car_price      DECIMAL(10,2)  NOT NULL                       -- rent per day in rupees
);

-- Every rental; return_date stays NULL until the car is brought back
CREATE TABLE rented_cars (
    rental_id   INT AUTO_INCREMENT PRIMARY KEY,
    reg_id      INT          NOT NULL,
    user_cnic   VARCHAR(20)  NOT NULL,
    rent_date   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    return_date DATETIME     NULL,
    FOREIGN KEY (reg_id) REFERENCES cars (reg_id)
);

-- Sample cars
INSERT INTO cars (reg_id, car_name, car_brand, car_model, car_engine_no, car_chassis_no, car_status, car_price) VALUES
    (101, 'Corolla Altis',  'Toyota',   '2022', '2ZR-1001',  'JTD-5001', 'available', 6500.00),
    (102, 'Civic Oriel',    'Honda',    '2021', 'L15B-2002', 'HND-5002', 'available', 7500.00),
    (103, 'Sportage Alpha', 'KIA',      '2023', 'G4NA-3003', 'KIA-5003', 'available', 9000.00),
    (104, 'Swift DLX',      'Suzuki',   '2020', 'K12M-4004', 'SZK-5004', 'available', 4500.00),
    (105, 'Tucson AWD',     'Hyundai',  '2022', 'G4NA-5005', 'HYN-5005', 'available', 10500.00),
    (106, 'Yaris ATIV',     'Toyota',   '2023', '2NR-6006',  'JTD-5006', 'available', 5800.00);
