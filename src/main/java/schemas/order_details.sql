
-- Structure for table `orders_details`

CREATE TABLE `orders_details` (
  `order_id` binary(16) NOT NULL PRIMARY KEY,
  `product_id` binary(16) NOT NULL UNIQUE,
  `quantity` int DEFAULT NULL
);