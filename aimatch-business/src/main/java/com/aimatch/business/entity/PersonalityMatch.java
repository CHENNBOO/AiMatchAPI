package com.aimatch.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 性格匹配实体类
 */
@Data
@TableName("personality_matches")
public class PersonalityMatch {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 性格描述文本1
     */
    @TableField("personality_text_1")
    private String personalityText1;
    
    /**
     * 性格描述文本2
     */
    @TableField("personality_text_2")
    private String personalityText2;
    
    /**
     * 匹配分数
     */
    @TableField("match_score")
    private BigDecimal matchScore;
    
    /**
     * 匹配描述
     */
    @TableField("match_description")
    private String matchDescription;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
} 