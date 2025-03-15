package com.aimatch.business.service;

import com.aimatch.business.dto.PersonalityMatchResult;

public interface PersonalityAnalysisService {
    PersonalityMatchResult analyzePersonalityMatch(String personality1, String personality2);
} 