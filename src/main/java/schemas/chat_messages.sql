
-- Structure for table `chat_messages`

CREATE TABLE `chat_messages` (
  `message_id` binary(16) NOT NULL PRIMARY KEY,
  `sender_id` binary(16) NOT NULL,
  `receiver_id` binary(16) NOT NULL,
  `content` text NOT NULL,
  `message_type` varchar(10) DEFAULT 'TEXT',
  `file_path` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  INDEX idx_chat_sender (`sender_id`),
  INDEX idx_chat_receiver (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
