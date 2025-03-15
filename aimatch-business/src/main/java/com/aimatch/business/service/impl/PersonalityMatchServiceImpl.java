package com.aimatch.business.service.impl;

import com.aimatch.business.entity.PersonalityMatch;
import com.aimatch.business.mapper.PersonalityMatchMapper;
import com.aimatch.business.service.PersonalityMatchService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 性格匹配服务实现类
 */
@Service
public class PersonalityMatchServiceImpl extends ServiceImpl<PersonalityMatchMapper, PersonalityMatch> implements PersonalityMatchService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityMatch createInitialMatch(PersonalityMatch personalityMatch) {
        // 设置创建时间和更新时间
        personalityMatch.setCreatedAt(LocalDateTime.now());
        personalityMatch.setUpdatedAt(LocalDateTime.now());
        
        // 保存记录
        save(personalityMatch);
        return personalityMatch;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityMatch updateMatchResult(Long id, BigDecimal matchScore, String matchDescription) {
        // 获取现有记录
        PersonalityMatch match = getById(id);
        if (match == null) {
            throw new RuntimeException("匹配记录不存在");
        }
        
        // 更新匹配结果
        match.setMatchScore(matchScore);
        match.setMatchDescription(matchDescription);
        match.setUpdatedAt(LocalDateTime.now());
        
        // 更新记录
        updateById(match);
        return match;
    }

    @Override
    public List<PersonalityMatch> getMatchHistory(Long userId) {
        // 创建查询条件
        LambdaQueryWrapper<PersonalityMatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersonalityMatch::getUserId, userId)
                   .orderByDesc(PersonalityMatch::getCreatedAt);
        
        // 执行查询
        return list(queryWrapper);
    }

    @Override
    public PersonalityMatch getMatchById(Long id) {
        return getById(id);
    }
} 