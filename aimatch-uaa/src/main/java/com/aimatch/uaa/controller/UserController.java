package com.aimatch.uaa.controller;

import com.aimatch.uaa.common.Result;
import com.aimatch.uaa.dto.UserLoginDTO;
import com.aimatch.uaa.dto.UserRegisterDTO;
import com.aimatch.uaa.entity.User;
import com.aimatch.uaa.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@Tag(name = "用户管理接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        return Result.success(user);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        User user = userService.findByUsername(loginDTO.getUsername());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token","Bearer "+ token);
        user.setPassword(null);
        result.put("user", user);
        
        return Result.success(result);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestParam Long userId,
                                     @RequestParam String oldPassword,
                                     @RequestParam String newPassword) {
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success(null);
    }
} 