package com.example.verityx.controller;

import com.example.verityx.entity.UserLogin;
import com.example.verityx.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

@Tag(name = "登录接口", description = "登录接口")
@RestController
@RequestMapping("/api/user")
public class UserLoginController {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private UserLoginService userLoginService;

    @Operation(summary = "登录请求", description = "登录请求接口")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest loginRequest) {
        logger.debug("开始处理登录请求");
        logger.info("用户尝试登录: {}", loginRequest.getUsername());

        // 调用服务层进行登录验证
        UserLogin user = userLoginService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (user != null) {
            // 登录成功
            logger.info("用户登录成功: {}", user.getUsername());
            return new ApiResponseResult("success", "登录成功，欢迎 " + user.getUsername(), user);
        }

        // 登录失败
        logger.warn("用户登录失败: {}", loginRequest.getUsername());
        return new ApiResponseResult("fail", "用户名、密码错误或账户未激活", null);
    }
}

// 用于统一返回的响应体
class ApiResponseResult {
    private String status; // success 或 fail
    private String message; // 错误或成功信息
    private Object data; // 用户数据或其他附加数据

    public ApiResponseResult(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
