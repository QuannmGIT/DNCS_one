
-- Structure for table `orders`

CREATE TABLE `orders` (
  `order_id` BINARY(16) NOT NULL PRIMARY KEY,
  `invoice_id` BINARY(16) NOT NULL,
  `staff_id` BINARY(16) NOT NULL,
  `status` TINYINT(1) DEFAULT 1,
  `order_date` DATE,
  `total` INT,
  INDEX idx_orders_invoice (`invoice_id`),
  INDEX idx_orders_staff (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
