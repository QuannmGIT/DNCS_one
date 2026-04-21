
-- Structure for table `invoices`

CREATE TABLE `invoices` (
  `invoice_id` binary(16) NOT NULL PRIMARY KEY,
  `staff_id` binary(16) NOT NULL UNIQUE,
  `invoice_date` date DEFAULT NULL,
  `total` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1 -- 1: paid, 0: unpaid
);