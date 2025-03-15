package com.aimatch.business.service.impl;

import com.aimatch.business.entity.PersonalityMatch;
import com.aimatch.business.mapper.PersonalityMatchMapper;
import com.aimatch.business.service.PersonalityMatchService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
        // 查询用户的历史性格记录
        LambdaQueryWrapper<PersonalityMatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersonalityMatch::getUserId, personalityMatch.getUserId())
                   .orderByDesc(PersonalityMatch::getCreatedAt)
                   .last("LIMIT 1");
        
        PersonalityMatch existingMatch = getOne(queryWrapper);
        
        if (existingMatch != null) {
            // 如果存在历史记录，则更新性格文本并删除分析结果
            LambdaUpdateWrapper<PersonalityMatch> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(PersonalityMatch::getId, existingMatch.getId())
                        .set(PersonalityMatch::getPersonalityText1, personalityMatch.getPersonalityText1())
                        .set(PersonalityMatch::getPersonalityText2, personalityMatch.getPersonalityText2())
                        .set(PersonalityMatch::getMatchScore, null)
                        .set(PersonalityMatch::getMatchDescription, null)
                        .set(PersonalityMatch::getUpdatedAt, LocalDateTime.now());
            
            // 更新记录
            update(updateWrapper);
            return existingMatch;
        }
        
        // 如果不存在历史记录，则创建新记录
        personalityMatch.setCreatedAt(LocalDateTime.now());
        personalityMatch.setUpdatedAt(LocalDateTime.now());
        
        // 保存新记录
        save(personalityMatch);
        return personalityMatch;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PersonalityMatch updateMatchResult(Long userId, BigDecimal matchScore, String matchDescription) {
        // 查询用户最新的性格匹配记录
        LambdaQueryWrapper<PersonalityMatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersonalityMatch::getUserId, userId)
                   .orderByDesc(PersonalityMatch::getCreatedAt)
                   .last("LIMIT 1");
        
        PersonalityMatch match = getOne(queryWrapper);
        if (match == null) {
            throw new RuntimeException("未找到用户的性格匹配记录");
        }

        // 检查是否已存在分析结果
        if (match.getMatchScore() != null && match.getMatchDescription() != null) {
            // 如果已存在分析结果，直接返回
            return match;
        }

        // TODO: 调用AI分析接口获取匹配结果
        // 这里需要实现调用AI接口的逻辑
        
        // 更新匹配结果
        LambdaUpdateWrapper<PersonalityMatch> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PersonalityMatch::getId, match.getId())
                    .set(PersonalityMatch::getMatchScore, matchScore)
                    .set(PersonalityMatch::getMatchDescription, matchDescription)
                    .set(PersonalityMatch::getUpdatedAt, LocalDateTime.now());
        
        // 更新记录
        update(updateWrapper);
        
        // 重新查询并返回更新后的记录
        return getById(match.getId());
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