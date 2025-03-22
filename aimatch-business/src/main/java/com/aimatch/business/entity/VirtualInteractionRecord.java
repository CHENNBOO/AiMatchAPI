package com.aimatch.business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("virtual_interaction_record")
public class VirtualInteractionRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("user_message")
    private String userMessage;
    
    @TableField("ai_reply")
    private String aiReply;
    
    @TableField("personality")
    private String personality;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField("user_id")
    private String userId;
    
    @TableField("session_id")
    private String sessionId;
    
    @TableField("prompt")
    private String prompt;
} 