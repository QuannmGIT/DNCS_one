
-- Structure for table `staff`

CREATE TABLE `staff` (
  `staff_id` BINARY(16) NOT NULL PRIMARY KEY,
  `staff_name` VARCHAR(255),
  `email` VARCHAR(255),
  `password` VARCHAR(255),
  `full_name` VARCHAR(255),
  `role` VARCHAR(255),
  `status` TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
