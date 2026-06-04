
-- Structure for table `salaries`

CREATE TABLE `salaries` (
  `staff_id` BINARY(16) NOT NULL PRIMARY KEY,
  `base_salary` DECIMAL(10,2),
  `commission_rate` DECIMAL(10,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
