package com.aimatch.business.controller;

import com.aimatch.business.dto.VirtualInteractionRequest;
import com.aimatch.business.dto.VirtualInteractionResponse;
import com.aimatch.business.service.VirtualInteractionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "虚拟互动接口")
@RestController
@RequestMapping("/api/virtual")
@RequiredArgsConstructor
public class VirtualInteractionController {

    private final VirtualInteractionService virtualInteractionService;

    @ApiOperation("虚拟互动")
    @PostMapping("/interaction")
    public VirtualInteractionResponse interact(@RequestBody VirtualInteractionRequest request) {
        return virtualInteractionService.interact(request.getMessage(), request.getPersonality());
    }
} 