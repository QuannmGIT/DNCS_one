
-- Structure for table `orders`

CREATE TABLE `orders` (
  `order_id` binary(16) NOT NULL PRIMARY KEY,
  `invoice_id` binary(16) NOT NULL,
  `staff_id` binary(16) NOT NULL,
  `order_date` date DEFAULT NULL,
  `total` int DEFAULT NULL
);
