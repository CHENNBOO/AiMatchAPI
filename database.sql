CREATE DATABASE aimatch DEFAULT CHARACTER SET utf8mb4;

-- 创建用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建性格匹配表
CREATE TABLE personality_matches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    personality_text_1 TEXT NOT NULL,
    personality_text_2 TEXT NOT NULL,
    match_score DECIMAL(5,2),
    match_description TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建虚拟互动记录表
CREATE TABLE `virtual_interaction_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_message` VARCHAR(500) NOT NULL COMMENT '用户消息',
    `ai_reply` VARCHAR(500) NOT NULL COMMENT 'AI回复',
    `personality` VARCHAR(1000) NOT NULL COMMENT '性格特征',
    `create_time` DATETIME  COMMENT '创建时间',
    `user_id` VARCHAR(50) DEFAULT NULL COMMENT '用户ID',
    `session_id` VARCHAR(50) DEFAULT NULL COMMENT '会话ID',
    `prompt` VARCHAR(200) DEFAULT NULL COMMENT '完整提示词',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='虚拟互动记录表';