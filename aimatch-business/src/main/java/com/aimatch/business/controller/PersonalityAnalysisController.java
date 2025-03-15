package com.aimatch.business.controller;

import com.aimatch.business.dto.PersonalityMatchRequest;
import com.aimatch.business.dto.PersonalityMatchResult;
import com.aimatch.business.service.PersonalityAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "性格匹配分析接口")
@RestController
@RequestMapping("/api/personality")
@RequiredArgsConstructor
public class PersonalityAnalysisController {

    private final PersonalityAnalysisService personalityAnalysisService;

    @ApiOperation("分析两个性格的匹配程度")
    @PostMapping("/match")
    public PersonalityMatchResult analyzeMatch(@RequestBody PersonalityMatchRequest request) {
        return personalityAnalysisService.analyzePersonalityMatch(
            request.getPersonality1(), 
            request.getPersonality2()
        );
    }
} 