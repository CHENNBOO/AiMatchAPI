package com.aimatch.business.controller;

import com.aimatch.business.entity.PersonalityMatch;
import com.aimatch.business.service.PersonalityMatchService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 性格匹配控制器
 */
@RestController
@RequestMapping("/api/personality-match")
@RequiredArgsConstructor
public class PersonalityMatchController {

    private final PersonalityMatchService personalityMatchService;

    /**
     * 创建初始性格匹配分析
     */
    @PostMapping("/initial")
    public ResponseEntity<PersonalityMatch> createInitialMatch(@RequestBody PersonalityMatch personalityMatch) {
        return ResponseEntity.ok(personalityMatchService.createInitialMatch(personalityMatch));
    }

    /**
     * 获取性格匹配分析结果
     */
    @GetMapping("/result")
    public ResponseEntity<PersonalityMatch> getMatchResult(@RequestParam Long userId) {
        return ResponseEntity.ok(personalityMatchService.getMatchResult(userId));
    }

    /**
     * 获取历史匹配记录
     */
    @GetMapping("/history")
    public ResponseEntity<List<PersonalityMatch>> getMatchHistory(
            @RequestParam Long userId) {
        return ResponseEntity.ok(personalityMatchService.getMatchHistory(userId));
    }

    /**
     * 获取匹配详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonalityMatch> getMatchById(@PathVariable Long id) {
        return ResponseEntity.ok(personalityMatchService.getMatchById(id));
    }
} 