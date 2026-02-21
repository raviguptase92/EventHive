-- Order Service
CREATE USER order_user WITH PASSWORD 'order_pwd';
CREATE DATABASE order_db OWNER order_user;
GRANT ALL PRIVILEGES ON DATABASE order_db TO order_user;

-- Payment Service
CREATE USER payment WITH PASSWORD 'payment';
CREATE DATABASE payment_db OWNER payment;
GRANT ALL PRIVILEGES ON DATABASE payment_db TO payment;

-- Inventory Service
CREATE USER inventory WITH PASSWORD 'inventory';
CREATE DATABASE inventory_db OWNER inventory;
GRANT ALL PRIVILEGES ON DATABASE inventory_db TO inventory;
