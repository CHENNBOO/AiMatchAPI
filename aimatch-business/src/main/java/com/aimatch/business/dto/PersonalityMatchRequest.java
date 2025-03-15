package com.aimatch.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "性格匹配分析请求")
public class PersonalityMatchRequest {
    
    @ApiModelProperty(value = "第一个人的性格特征", required = true, example = "性格开朗，善于交际，喜欢与人交流")
    private String personality1;
    
    @ApiModelProperty(value = "第二个人的性格特征", required = true, example = "性格内向，做事认真，喜欢独处")
    private String personality2;
} 