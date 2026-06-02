-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 02, 2026 at 09:15 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `storemanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `average`
--

CREATE TABLE `average` (
  `staff_id` binary(16) NOT NULL,
  `average_score` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `average`
--

INSERT INTO `average` (`staff_id`, `average_score`) VALUES
(0x09dc209bb3b94b08aa6cb559d02baa5f, 85),
(0x81bc0e15e02c4e6bb6ece9d8c2935d1c, 78),
(0xa11f9efdabd2464aa13506b6dc92fb82, 92);

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `invoice_id` binary(16) NOT NULL,
  `staff_id` binary(16) NOT NULL,
  `invoice_date` date DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` binary(16) NOT NULL,
  `invoice_id` binary(16) NOT NULL,
  `user_id` binary(16) DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `total` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


--
-- Table structure for table `orders_details`
--

CREATE TABLE `orders_details` (
  `order_id` binary(16) NOT NULL,
  `product_id` binary(16) NOT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` binary(16) NOT NULL,
  `product_name` varchar(50) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `cost` decimal(10,0) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `category`, `price`, `cost`, `image`, `status`) VALUES
(0x0000000000000000000000010000000d, 'Hot Matcha Latte', 'Hot', 35000, 15000, '13', 1),
(0x0000000000000000000000010000000e, 'Earl Grey Tea', 'Hot', 25000, 10000, '14', 1),
(0x0000000000000000000000010000000f, 'Iced Mocha', 'Iced', 38000, 15000, '15', 1),
(0x00000000000000000000000100000010, 'Peach Tea', 'Iced', 25000, 10000, '16', 1),
(0x00000000000000000000000100000011, 'Cold Brew', 'Iced', 35000, 15000, '17', 1),
(0x00000000000000000000000100000012, 'Mango Smoothie', 'Iced', 40000, 18000, '18', 1),
(0x00000000000000000000000100000013, 'Strawberry Macchiato', 'Iced', 42000, 18000, '19', 1),
(0x00000000000000000000000100000014, 'Cheesecake', 'Bakery', 40000, 18000, '20', 1),
(0x00000000000000000000000100000015, 'Choco Cookie', 'Bakery', 15000, 5000, '21', 1),
(0x00000000000000000000000100000016, 'Red Velvet', 'Bakery', 45000, 20000, '22', 1),
(0x00000000000000000000000100000017, 'Macaron (Set 3)', 'Bakery', 30000, 12000, '23', 1),
(0x00000000000000000000000100000018, 'Blueberry Muffin', 'Bakery', 25000, 10000, '24', 1),
(0x15f9224e50474d0aa886d2d25e718c27, 'Matcha Ice Blended', 'Iced', 35000, 20000, '1', 1),
(0x27fb51ee239146e7b33efad458a54b49, 'Americano', 'Hot', 28000, 12000, '2', 1),
(0x3cdb2c85b6294120b6b0518acb80937f, 'Croissants', 'Bakery', 25000, 10000, '3', 1),
(0x4c76bbd6fd234e6695b45ab92626ebdd, 'Ice Latte', 'Iced', 25000, 12000, '4', 1),
(0x60d8b68bf98a4157a536b4fc331fd2de, 'Ice Black Coffee', 'Iced', 35000, 15000, '5', 1),
(0x96395fa42b5a4e8a9bbcaf93f2133e21, 'Caramel Machito', 'Hot', 40000, 18000, '6', 1),
(0x96488d69996b4eafbca0557c2209ff0e, 'Tiramisu', 'Bakery', 45000, 20000, '7', 1),
(0xb1358fd3be0f47e099ee784690e7fd77, 'Lemon Tea', 'Iced', 19000, 8000, '8', 1),
(0xbccfdf85b29a48fb95b983a0181d30ea, 'Orange', 'Iced', 23000, 10000, '9', 1),
(0xc747db680398402e8495a76e112485e2, 'Espresso', 'Hot', 20000, 8000, '10', 1),
(0xca02009f077548f3b7f086934a91d5bc, 'Hot Chocolate', 'Hot', 35000, 15000, '11', 1),
(0xf2f558e1d3dc4a5197922c51e1a68ae8, 'Cappuccino', 'Hot', 32000, 12000, '12', 1);

-- --------------------------------------------------------

--
-- Table structure for table `salaries`
--

CREATE TABLE `salaries` (
  `staff_id` binary(16) NOT NULL,
  `baseSalary` decimal(10,0) DEFAULT NULL,
  `commissionRate` decimal(10,0) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `salaries`
--

INSERT INTO `salaries` (`staff_id`, `baseSalary`, `commissionRate`) VALUES
(0x09dc209bb3b94b08aa6cb559d02baa5f, 5000000, 2),
(0x81bc0e15e02c4e6bb6ece9d8c2935d1c, 4800000, 3),
(0xa11f9efdabd2464aa13506b6dc92fb82, 4500000, 2);

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

CREATE TABLE `staff` (
  `staff_id` binary(16) NOT NULL,
  `staff_name` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `baseSalary` decimal(10,0) DEFAULT NULL,
  `CommissionRate` decimal(10,0) DEFAULT NULL,
  `role` enum('admin','staff') DEFAULT 'staff',
  `status` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`staff_id`, `staff_name`, `email`, `password`, `full_name`, `baseSalary`, `CommissionRate`, `role`, `status`) VALUES
(0x09dc209bb3b94b08aa6cb559d02baa5f, 'alice', NULL, '391z9xXK6Ppqc1P0TTFWvw==:NXT6wfc9+n2jxOmaYGgHStrjFuDdDvDh5p6xobirnpA=', 'Alice Nguyen', NULL, NULL, 'staff', 1),
(0x41443030310000000000000000000000, 'admin', 'admin@hanabicafe.com', 'KztQNRrimlbwCGA9YEzLTw==:MO/Sw2q5zQDoIll2QhkDjzi+wurxfMzLXu31uxmaBdc=', 'adminhethong', 0, 0, 'admin', 1),
(0x81bc0e15e02c4e6bb6ece9d8c2935d1c, 'carol', NULL, '391z9xXK6Ppqc1P0TTFWvw==:NXT6wfc9+n2jxOmaYGgHStrjFuDdDvDh5p6xobirnpA=', 'Carol Le', NULL, NULL, 'staff', 1),
(0xa11f9efdabd2464aa13506b6dc92fb82, 'bob', NULL, '391z9xXK6Ppqc1P0TTFWvw==:NXT6wfc9+n2jxOmaYGgHStrjFuDdDvDh5p6xobirnpA=', 'Bob Tran', NULL, NULL, 'staff', 1);

-- Password addmin: admin123
-- Password staff: staff123

--
-- Indexes for table `average`
--
ALTER TABLE `average`
  ADD PRIMARY KEY (`staff_id`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`invoice_id`),
  ADD KEY `idx_invoices_staff` (`staff_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD UNIQUE KEY `invoice_id` (`invoice_id`),
  ADD KEY `idx_orders_user` (`user_id`),
  ADD KEY `idx_orders_invoice` (`invoice_id`);

--
-- Indexes for table `orders_details`
--
ALTER TABLE `orders_details`
  ADD PRIMARY KEY (`order_id`,`product_id`),
  ADD KEY `idx_od_product` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `product_name` (`product_name`);

--
-- Indexes for table `salaries`
--
ALTER TABLE `salaries`
  ADD PRIMARY KEY (`staff_id`);

--
-- Indexes for table `staff`
--
ALTER TABLE `staff`
  ADD PRIMARY KEY (`staff_id`),
  ADD UNIQUE KEY `staff_name` (`staff_name`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `average`
--
ALTER TABLE `average`
  ADD CONSTRAINT `fk_average_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE;

--
-- Constraints for table `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `fk_invoices_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`invoice_id`),
  ADD CONSTRAINT `fk_orders_staff` FOREIGN KEY (`user_id`) REFERENCES `staff` (`staff_id`);

--
-- Constraints for table `orders_details`
--
ALTER TABLE `orders_details`
  ADD CONSTRAINT `fk_od_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_od_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Constraints for table `salaries`
--
ALTER TABLE `salaries`
  ADD CONSTRAINT `fk_salaries_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
