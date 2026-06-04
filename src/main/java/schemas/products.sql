
-- Structure for table `products`

CREATE TABLE `products` (
  `product_id` BINARY(16) NOT NULL PRIMARY KEY,
  `product_name` VARCHAR(255),
  `category` VARCHAR(255),
  `price` DECIMAL(10,2),
  `cost` DECIMAL(10,2),
  `image` VARCHAR(255),
  `status` TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
