
-- Structure for table `salaries`

CREATE TABLE `salaries` (
  `staff_id` binary(16) NOT NULL PRIMARY KEY,
  `baseSalary` decimal DEFAULT NULL,
  `commissionRate` decimal DEFAULT NULL
);
