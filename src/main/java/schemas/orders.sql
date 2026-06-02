
-- Structure for table `orders`

CREATE TABLE `orders` (
  `order_id` binary(16) NOT NULL PRIMARY KEY,
  `invoice_id` binary(16) NOT NULL,
  `staff_id` binary(16) NOT NULL,
  `status` tinyint(1) DEFAULT 1, -- 1: active, 0: cancelled
  `order_date` date DEFAULT NULL,
  `total` int DEFAULT NULL,
  INDEX idx_orders_staff (`staff_id`),
  INDEX idx_orders_invoice (`invoice_id`)
);
