package com.aimatch.business.service;

import com.aimatch.business.entity.PersonalityMatch;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;
import java.util.List;

/**
 * 性格匹配服务接口
 */
public interface PersonalityMatchService extends IService<PersonalityMatch> {
    
    /**
     * 创建性格匹配分析（初始状态）
     *
     * @param personalityMatch 性格匹配信息（不包含matchScore和matchDescription）
     * @return 创建的性格匹配记录
     */
    PersonalityMatch createInitialMatch(PersonalityMatch personalityMatch);
    
    /**
     * 更新性格匹配分析结果
     *
     * @param id 记录ID
     * @param matchScore 匹配分数
     * @param matchDescription 匹配描述
     * @return 更新后的性格匹配记录
     */
    PersonalityMatch updateMatchResult(Long id, BigDecimal matchScore, String matchDescription);
    
    /**
     * 获取用户的性格匹配历史记录
     *
     * @param userId 用户ID
     * @return 性格匹配记录列表
     */
    List<PersonalityMatch> getMatchHistory(Long userId);

    /**
     * 根据ID获取性格匹配记录
     *
     * @param id 记录ID
     * @return 性格匹配记录
     */
    PersonalityMatch getMatchById(Long id);
} 