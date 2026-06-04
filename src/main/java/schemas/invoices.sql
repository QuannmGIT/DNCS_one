
-- Structure for table `invoices`

CREATE TABLE `invoices` (
  `invoice_id` BINARY(16) NOT NULL PRIMARY KEY,
  `staff_id` BINARY(16) NOT NULL,
  `invoice_date` DATE,
  `total` INT,
  `status` TINYINT(1) DEFAULT 1,
  INDEX idx_invoices_staff (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
