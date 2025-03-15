package com.aimatch.business.service.impl;

import com.aimatch.business.config.DeepseekConfig;
import com.aimatch.business.dto.PersonalityMatchResult;
import com.aimatch.business.service.PersonalityAnalysisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PersonalityAnalysisServiceImpl implements PersonalityAnalysisService {

    private final DeepseekConfig deepseekConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PersonalityAnalysisServiceImpl(DeepseekConfig deepseekConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.deepseekConfig = deepseekConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public PersonalityMatchResult analyzePersonalityMatch(String personality1, String personality2) {
        log.info("进入性格匹配分析接口");
        log.info("personality1：{}", personality1);
        log.info("personality2：{}", personality2);

        String prompt = String.format(
            "作为一个专业的性格分析专家，请分析以下两种性格类型的匹配程度：\n\n" +
            "第一个人的性格特征：%s\n" +
            "第二个人的性格特征：%s\n\n" +
            "请提供：\n" +
            "1. 匹配度百分比（0-100之间的数字）\n" +
            "2. 一段详细的中文分析，包括：\n" +
            "   - 性格互补或冲突的方面\n" +
            "   - 可能存在的沟通挑战\n" +
            "   - 建议的相处之道\n\n" +
            "请以JSON格式返回，包含以下字段：\n" +
            "{\n" +
            "  \"matchPercentage\": 数字,\n" +
            "  \"analysis\": \"分析文本\"\n" +
            "}", personality1, personality2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(deepseekConfig.getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(deepseekConfig.getApiUrl(), request, Map.class);
            log.info("性格匹配分析接口，返回结果: {}", response);

            if (response != null) {
                Object choicesObj = response.get("choices");
                if (choicesObj != null && choicesObj instanceof List) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) choicesObj;
                    if (choices.size() > 0) {
                        Map<String, Object> firstChoice = choices.get(0);
                        Map<String, Object> messageMap = (Map<String, Object>) firstChoice.get("message");
                        String content = (String) messageMap.get("content");
                        
                        // 处理content中的```json标记
                        content = content.replaceAll("```json\\s*", "").replaceAll("```\\s*$", "").trim();
                        
                        return objectMapper.readValue(content, PersonalityMatchResult.class);
                    }
                }
            }
            throw new RuntimeException("Invalid response from Deepseek API");
        } catch (Exception e) {
            log.error("Error in personality analysis: ", e);
            throw new RuntimeException("分析过程中出现错误: " + e.getMessage());
        }
    }
} 