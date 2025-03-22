package com.aimatch.business.service.impl;

import com.aimatch.business.config.DeepseekConfig;
import com.aimatch.business.dto.VirtualInteractionResponse;
import com.aimatch.business.service.VirtualInteractionService;
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
public class VirtualInteractionServiceImpl implements VirtualInteractionService {

    private final DeepseekConfig deepseekConfig;
    private final RestTemplate restTemplate;

    public VirtualInteractionServiceImpl(DeepseekConfig deepseekConfig, RestTemplate restTemplate) {
        this.deepseekConfig = deepseekConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public VirtualInteractionResponse interact(String message, String personality) {
        String prompt = String.format(
            "你是一个具有以下性格特征人：\n" +
            "%s\n" +
            "请以这个性格特征来回复用户的消息。保持性格特征的一致性，并给出符合该性格的回应。\n" +
            "注意：你的回复必须严格控制在200字以内。\n" +
            "用户消息：%s\n" +
            "请给出回应：", personality, message);
            
        log.info("虚拟互动接口=>提示词: {}", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(deepseekConfig.getApiKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        
        List<Map<String, String>> messages = new ArrayList<>();
        
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个具有特定性格特征的人。请始终保持这个性格特征来回复用户。你的回复必须严格控制在200字以内。");
        messages.add(systemMessage);
        
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(deepseekConfig.getApiUrl(), request, Map.class);
            log.info("虚拟互动接口=>返回结果: {}", response);

            if (response != null) {
                Object choicesObj = response.get("choices");
                if (choicesObj != null && choicesObj instanceof List) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) choicesObj;
                    if (choices.size() > 0) {
                        Map<String, Object> firstChoice = choices.get(0);
                        Map<String, Object> messageMap = (Map<String, Object>) firstChoice.get("message");
                        String reply = (String) messageMap.get("content");
                        
                        // 限制回复长度为50字
//                        if (reply.length() > 50) {
//                            reply = reply.substring(0, 50) + "...";
//                        }
                        
                        VirtualInteractionResponse virtualInteractionResponse = new VirtualInteractionResponse();
                        virtualInteractionResponse.setReply(reply);
                        return virtualInteractionResponse;
                    }
                }
            }
            throw new RuntimeException("Invalid response from Deepseek API");
        } catch (Exception e) {
            log.error("Error in virtual interaction: ", e);
            throw new RuntimeException("生成回复时出现错误: " + e.getMessage());
        }
    }
} 