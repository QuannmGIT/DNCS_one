
-- Structure for table `products`

CREATE TABLE `products` (
  `product_id` binary(16) NOT NULL PRIMARY KEY,
  `product_name` varchar(50) NOT NULL UNIQUE,
  `category` varchar(100) DEFAULT NULL,
  `price` decimal DEFAULT NULL,
  `cost` decimal DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1 -- 1: available, 0: out of stock
);