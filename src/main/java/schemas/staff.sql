
-- Structure for table `staff`

CREATE TABLE `staff` (
  `staff_id` binary(16) NOT NULL PRIMARY KEY,
  `staff_name` varchar(50) NOT NULL UNIQUE,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `baseSalary` decimal DEFAULT NULL,
  `CommissionRate` decimal DEFAULT NULL,
  `role` enum('admin','staff') DEFAULT 'staff',
  `status` tinyint(1) DEFAULT 1 -- 1: in working, 0: layoff
);