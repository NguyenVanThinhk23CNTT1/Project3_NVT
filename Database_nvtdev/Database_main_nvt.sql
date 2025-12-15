-- Nếu trước đó bạn có DB cũ
DROP DATABASE IF EXISTS motel_management;

-- DB chính của bạn
CREATE DATABASE IF NOT EXISTS nvt_quanlynhatro
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE nvt_quanlynhatro;

-- 1) USERS
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name VARCHAR(100),
  phone VARCHAR(20) UNIQUE,
  email VARCHAR(100) UNIQUE,
  role ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
  status ENUM('ACTIVE','LOCKED') NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2) ROOMS
CREATE TABLE rooms (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_code VARCHAR(20) NOT NULL UNIQUE,
  room_name VARCHAR(50),
  floor INT DEFAULT 1,
  area DECIMAL(6,2),
  max_people INT DEFAULT 2,
  rent_price DECIMAL(12,2) NOT NULL,
  deposit_default DECIMAL(12,2),
  status ENUM('EMPTY','RENTING','REPAIR') NOT NULL DEFAULT 'EMPTY',
  description TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3) AMENITIES
CREATE TABLE amenities (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  amenity_name VARCHAR(100) NOT NULL UNIQUE,
  amenity_desc VARCHAR(255)
);

-- 4) ROOM_AMENITIES (many-to-many)
CREATE TABLE room_amenities (
  room_id BIGINT NOT NULL,
  amenity_id BIGINT NOT NULL,
  PRIMARY KEY (room_id, amenity_id),
  FOREIGN KEY (room_id) REFERENCES rooms(id),
  FOREIGN KEY (amenity_id) REFERENCES amenities(id)
);

-- 5) TENANTS (hồ sơ người ở)
CREATE TABLE tenants (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL UNIQUE,
  cccd VARCHAR(20) UNIQUE,
  birthday DATE,
  address VARCHAR(255),
  user_id BIGINT,
  status ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 6) CONTRACTS (1 phòng có 1 hợp đồng ACTIVE tại 1 thời điểm)
CREATE TABLE contracts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_code VARCHAR(30) NOT NULL UNIQUE,
  room_id BIGINT NOT NULL,
  tenant_id BIGINT NOT NULL, -- người đại diện hợp đồng
  start_date DATE NOT NULL,
  end_date DATE,
  deposit DECIMAL(12,2) NOT NULL DEFAULT 0,
  rent_price DECIMAL(12,2) NOT NULL,
  status ENUM('ACTIVE','ENDED','CANCELLED') NOT NULL DEFAULT 'ACTIVE',
  note TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (room_id) REFERENCES rooms(id),
  FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- 7) CONTRACT_MEMBERS (ở ghép: nhiều người chung 1 hợp đồng)
CREATE TABLE contract_members (
  contract_id BIGINT NOT NULL,
  tenant_id BIGINT NOT NULL,
  relation VARCHAR(50),
  move_in_date DATE,
  move_out_date DATE,
  PRIMARY KEY (contract_id, tenant_id),
  FOREIGN KEY (contract_id) REFERENCES contracts(id),
  FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

-- 8) SERVICES (điện/nước/dịch vụ)
CREATE TABLE services (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  service_name VARCHAR(100) NOT NULL UNIQUE,
  unit VARCHAR(20) NOT NULL,
  unit_price DECIMAL(12,2) NOT NULL,
  type ENUM('FIXED','METER') NOT NULL DEFAULT 'FIXED',
  is_active TINYINT NOT NULL DEFAULT 1
);

-- 9) METER_READINGS (chỉ số theo phòng-tháng-năm)
CREATE TABLE meter_readings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_id BIGINT NOT NULL,
  bill_month INT NOT NULL,
  bill_year INT NOT NULL,
  electric_old INT NOT NULL DEFAULT 0,
  electric_new INT NOT NULL DEFAULT 0,
  water_old INT NOT NULL DEFAULT 0,
  water_new INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_meter_room_month_year (room_id, bill_month, bill_year),
  FOREIGN KEY (room_id) REFERENCES rooms(id)
);

-- 10) BILLS (hóa đơn theo hợp đồng-tháng-năm)
CREATE TABLE bills (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_id BIGINT NOT NULL,
  room_id BIGINT NOT NULL,
  bill_month INT NOT NULL,
  bill_year INT NOT NULL,
  rent_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  electric_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  water_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  service_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  discount DECIMAL(12,2) NOT NULL DEFAULT 0,
  total_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  due_date DATE,
  status ENUM('UNPAID','PAID','OVERDUE') NOT NULL DEFAULT 'UNPAID',
  note TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_bill_contract_month_year (contract_id, bill_month, bill_year),
  FOREIGN KEY (contract_id) REFERENCES contracts(id),
  FOREIGN KEY (room_id) REFERENCES rooms(id)
);

-- 11) BILL_ITEMS (chi tiết khoản thu)
CREATE TABLE bill_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  bill_id BIGINT NOT NULL,
  service_id BIGINT,
  item_name VARCHAR(100) NOT NULL,
  quantity DECIMAL(12,2) NOT NULL DEFAULT 1,
  unit_price DECIMAL(12,2) NOT NULL DEFAULT 0,
  amount DECIMAL(12,2) NOT NULL DEFAULT 0,
  FOREIGN KEY (bill_id) REFERENCES bills(id),
  FOREIGN KEY (service_id) REFERENCES services(id)
);

-- 12) PAYMENTS (thanh toán hóa đơn)
CREATE TABLE payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  bill_id BIGINT NOT NULL,
  paid_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  amount DECIMAL(12,2) NOT NULL,
  method ENUM('CASH','BANK','MOMO','VNPAY') NOT NULL DEFAULT 'CASH',
  transaction_code VARCHAR(100),
  status ENUM('SUCCESS','FAILED','PENDING') NOT NULL DEFAULT 'SUCCESS',
  FOREIGN KEY (bill_id) REFERENCES bills(id)
);

-- 13) FEEDBACKS (phản ánh/sửa chữa)
CREATE TABLE feedbacks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tenant_id BIGINT NOT NULL,
  room_id BIGINT NOT NULL,
  title VARCHAR(150) NOT NULL,
  content TEXT NOT NULL,
  status ENUM('NEW','IN_PROGRESS','DONE','REJECTED') NOT NULL DEFAULT 'NEW',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (tenant_id) REFERENCES tenants(id),
  FOREIGN KEY (room_id) REFERENCES rooms(id)
);


ALTER TABLE rooms 
  MODIFY created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  MODIFY updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

INSERT INTO services(service_name, unit, unit_price, type, is_active)
VALUES
('Điện', 'kWh', 3500, 'METER', 1),
('Nước', 'm3', 15000, 'METER', 1)
AS newvals
ON DUPLICATE KEY UPDATE
  unit_price = newvals.unit_price,
  unit = newvals.unit,
  type = newvals.type,
  is_active = newvals.is_active;

