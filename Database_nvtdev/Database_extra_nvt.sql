USE nvt_quanlynhatro;

-- ====== ADD missing columns safely (MySQL 8 friendly) ======
DELIMITER $$

DROP PROCEDURE IF EXISTS nvt_patch_contracts $$
CREATE PROCEDURE nvt_patch_contracts()
BEGIN
  -- contracts.updated_at
  IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'contracts'
      AND COLUMN_NAME = 'updated_at'
  ) THEN
    ALTER TABLE contracts
      ADD COLUMN updated_at DATETIME NOT NULL
      DEFAULT CURRENT_TIMESTAMP
      ON UPDATE CURRENT_TIMESTAMP;
  END IF;

  -- contract_members.is_primary
  IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'contract_members'
      AND COLUMN_NAME = 'is_primary'
  ) THEN
    ALTER TABLE contract_members
      ADD COLUMN is_primary TINYINT(1) NOT NULL DEFAULT 0;
  END IF;

  -- contract_members.created_at
  IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'contract_members'
      AND COLUMN_NAME = 'created_at'
  ) THEN
    ALTER TABLE contract_members
      ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
  END IF;

  -- ====== Indexes (skip if already exist) ======
  IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'contracts'
      AND INDEX_NAME = 'idx_contract_room'
  ) THEN
    CREATE INDEX idx_contract_room ON contracts(room_id);
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'contracts'
      AND INDEX_NAME = 'idx_contract_status'
  ) THEN
    CREATE INDEX idx_contract_status ON contracts(status);
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'contract_members'
      AND INDEX_NAME = 'idx_cm_tenant'
  ) THEN
    CREATE INDEX idx_cm_tenant ON contract_members(tenant_id);
  END IF;

END $$

DELIMITER ;

CALL nvt_patch_contracts();
DROP PROCEDURE nvt_patch_contracts;

DESCRIBE contracts;
DESCRIBE contract_members;

USE nvt_quanlynhatro;

CREATE TABLE IF NOT EXISTS hostels (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  address VARCHAR(255) NOT NULL,
  phone VARCHAR(30) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

USE nvt_quanlynhatro;

SHOW TABLES LIKE 'hostels';
DESCRIBE hostels;
SELECT id, name, address FROM hostels;


ALTER TABLE feedbacks MODIFY tenant_id BIGINT NULL;


