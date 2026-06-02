
-- Structure for table `orders_details`

CREATE TABLE `orders_details` (
  `order_id` binary(16) NOT NULL,
  `product_id` binary(16) NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX idx_od_product (`product_id`)
);
