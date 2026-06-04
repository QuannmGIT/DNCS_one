
-- Structure for table `chat_messages`

CREATE TABLE `chat_messages` (
  `message_id` BINARY(16) NOT NULL,
  `sender_id` BINARY(16) NOT NULL,
  `receiver_id` BINARY(16) NOT NULL,
  `content` VARCHAR(255),
  `message_type` VARCHAR(10) DEFAULT 'TEXT',
  `file_path` VARCHAR(255),
  `created_at` DATETIME NOT NULL,
  INDEX idx_chat_messages_message (`message_id`),
  INDEX idx_chat_messages_sender (`sender_id`),
  INDEX idx_chat_messages_receiver (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
