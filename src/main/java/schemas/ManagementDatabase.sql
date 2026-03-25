
--
-- Cơ sở dữ liệu: `quanly`
--

DELIMITER $$
--
-- Thủ tục
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `AutoUpdateKPI` (IN `p_user_id` INT)   BEGIN
    IF p_user_id IS NOT NULL THEN
        -- Kỹ thuật UPSERT: Nếu chưa có thì Thêm, có rồi thì Cập nhật
        INSERT INTO Average (user_id, Sum_order, Point)
        SELECT 
            p_user_id,
            -- 1. Đếm tổng số đơn hàng của nhân viên này
            (SELECT COUNT(*) FROM Orders WHERE user_id = p_user_id),
            
            -- 2. Cộng tổng số lượng hàng (Point) của nhân viên này
            (SELECT COALESCE(SUM(od.quantity), 0) 
             FROM Orders o 
             JOIN OrderDetails od ON o.order_id = od.order_id 
             WHERE o.user_id = p_user_id)
        ON DUPLICATE KEY UPDATE 
            Sum_order = VALUES(Sum_order), 
            Point = VALUES(Point);
    END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `average`
--

CREATE TABLE `average` (
  `user_id` int(11) NOT NULL,
  `Sum_order` int(11) DEFAULT 0,
  `Point` int(11) DEFAULT 0
);


-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orderdetails`
--

CREATE TABLE `orderdetails` (
  `detail_id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1
);

DELIMITER $$
CREATE TRIGGER `trg_kpi_detail_delete` AFTER DELETE ON `orderdetails` FOR EACH ROW BEGIN
    DECLARE v_user_id INT;
    SELECT user_id INTO v_user_id FROM Orders WHERE order_id = OLD.order_id;
    CALL AutoUpdateKPI(v_user_id);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_kpi_detail_insert` AFTER INSERT ON `orderdetails` FOR EACH ROW BEGIN
    DECLARE v_user_id INT;
    SELECT user_id INTO v_user_id FROM Orders WHERE order_id = NEW.order_id;
    CALL AutoUpdateKPI(v_user_id);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_kpi_detail_update` AFTER UPDATE ON `orderdetails` FOR EACH ROW BEGIN
    DECLARE v_user_id INT;
    SELECT user_id INTO v_user_id FROM Orders WHERE order_id = NEW.order_id;
    CALL AutoUpdateKPI(v_user_id);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_update_total_after_delete` AFTER DELETE ON `orderdetails` FOR EACH ROW BEGIN
    UPDATE Orders
    SET total_amount = (
        SELECT COALESCE(SUM(od.quantity * p.price), 0)
        FROM OrderDetails od
        JOIN Products p ON od.product_id = p.product_id
        WHERE od.order_id = OLD.order_id
    )
    WHERE order_id = OLD.order_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_update_total_after_insert` AFTER INSERT ON `orderdetails` FOR EACH ROW BEGIN
    UPDATE Orders
    SET total_amount = (
        SELECT COALESCE(SUM(od.quantity * p.price), 0)
        FROM OrderDetails od
        JOIN Products p ON od.product_id = p.product_id
        WHERE od.order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_update_total_after_update` AFTER UPDATE ON `orderdetails` FOR EACH ROW BEGIN
    -- Cập nhật cho đơn hàng MỚI (nếu lỡ sửa order_id)
    UPDATE Orders
    SET total_amount = (
        SELECT COALESCE(SUM(od.quantity * p.price), 0)
        FROM OrderDetails od
        JOIN Products p ON od.product_id = p.product_id
        WHERE od.order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
    
    -- Cập nhật cho đơn hàng CŨ (trường hợp chuyển món sang đơn khác)
    IF NEW.order_id <> OLD.order_id THEN
        UPDATE Orders
        SET total_amount = (
            SELECT COALESCE(SUM(od.quantity * p.price), 0)
            FROM OrderDetails od
            JOIN Products p ON od.product_id = p.product_id
            WHERE od.order_id = OLD.order_id
        )
        WHERE order_id = OLD.order_id;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) DEFAULT 0.00
);

DELIMITER $$
CREATE TRIGGER `trg_kpi_order_delete` AFTER DELETE ON `orders` FOR EACH ROW BEGIN
    CALL AutoUpdateKPI(OLD.user_id);
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_kpi_order_insert` AFTER INSERT ON `orders` FOR EACH ROW BEGIN
    CALL AutoUpdateKPI(NEW.user_id);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL
);




--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` enum('admin','staff') DEFAULT 'staff',
  `status` tinyint(1) DEFAULT 1
);

ALTER TABLE `average`
  ADD PRIMARY KEY (`user_id`);

--
-- Chỉ mục cho bảng `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD PRIMARY KEY (`detail_id`),
  ADD KEY `fk_detail_order` (`order_id`),
  ADD KEY `fk_detail_product` (`product_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `fk_order_user` (`user_id`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

-- AUTO_INCREMENT cho bảng `orderdetails`
--
ALTER TABLE `orderdetails`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=90;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Các ràng buộc cho bảng `average`
--
ALTER TABLE `average`
  ADD CONSTRAINT `average_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `orderdetails`
--
ALTER TABLE `orderdetails`
  ADD CONSTRAINT `fk_detail_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_detail_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL;
COMMIT;