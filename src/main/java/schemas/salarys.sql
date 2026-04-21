
-- Structure for table `staff salarys`

CREATE TABLE `salarys` (
  `staff_id` binary(16) NOT NULL PRIMARY KEY,
  `baseSalary` decimal DEFAULT NULL,
  `CommissionRate` decimal DEFAULT NULL
);