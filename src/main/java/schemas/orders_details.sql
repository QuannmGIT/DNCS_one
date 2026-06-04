
-- Structure for table `orders_details`

CREATE TABLE `orders_details` (
  `order_id` BINARY(16) NOT NULL,
  `product_id` BINARY(16) NOT NULL,
  `quantity` INT,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX idx_od_product (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
