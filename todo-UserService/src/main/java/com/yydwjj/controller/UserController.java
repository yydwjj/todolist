package com.yydwjj.controller;

import com.yydwjj.pojo.User;
import com.yydwjj.repository.UserRepository;
import com.yydwjj.utils.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户操作
 * 基本的用户操作：登录、注册、状态认证、权限认证
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 用户登录
     * @param body username,password两个key,对应登录账户和登录密码
     * @return 登陆成功返回jwtToken，登录失败返回错误码+错误提示
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        logger.info("Login request received");
        String username = body.get("username");
        String password = body.get("password");

        // 1. 查找用户
        User user = userRepository.findByName(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found")); // 直接返回 Map，Spring 会自动转为 JSON
        }

        // 2. 验证密码（明文比较仅用于示例，实际应使用 BCrypt 等加密验证）
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid password"));
        }

        // 3. 生成 Token
        String token = jwtHelper.createToken(user.getId(),user.getRole());

        // 4. 返回标准 JSON 格式
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * 用户注册
     * @param body username,password两个key,对应登录账户和登录密码
     * @return 注册成功返回JwtToken+成功提示，注册失败返回错误码400+错误原因
     */
    @PostMapping("sign")
    public ResponseEntity<String> sign(@RequestBody Map<String, String> body) {
        logger.info("Sign up request received");
        String username = body.get("username");
        String password = body.get("password");
        // 1. 检查用户名是否已存在
        if (userRepository.findByName(username).isPresent()) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // 2. 创建新用户并保存到数据库
        User user = new User();
        user.setName(username);
        user.setPassword(password); // 注意：生产环境应加密密码！
        user.setRole("user");
        user.setDeleted(false);
        User savedUser = userRepository.save(user);

        // 3. 生成 JWT Token
        String token = jwtHelper.createToken(savedUser.getId(),savedUser.getRole());

        // 4. 返回成功信息和 Token
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body("Registration successful");
    }

    /**
     * 登陆状态检查
     * 用户登录状态的api
     * 登录成功返回 用户id
     * @return 成功返回用户id，失败返回错误码+提示
     * */
    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Object>> checkLoginStatus(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        logger.info("Check login status request received");
        // 1. 提取 Token
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid token format");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authorizationHeader.substring(7); // 去掉 "Bearer " 前缀

        // 2. 验证 Token 是否过期
        if (jwtHelper.isExpiration(token)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", "Token expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        System.out.println("Server Current Time: " + new Date());

        // 3. 解析 Token 获取用户 ID
        try {
            Long userId = jwtHelper.getUserId(token);
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "User is logged in");
            response.put("userId", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    /**
     * 用户身份检查
     * 获取用户身份
     * @return 有效JwtToken返回用户角色
     * @return 无效返回错误码+错误提示
     * */
    @GetMapping("/check-role")
    public ResponseEntity<Map<String, Object>> checkRole(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        logger.info("Check role request received");
        // 1. 提取 Token
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid token format");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authorizationHeader.substring(7); // 去掉 "Bearer " 前缀

        // 2. 验证 Token 是否过期
        if (jwtHelper.isExpiration(token)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", "Token expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        System.out.println("Server Current Time: " + new Date());

        // 3. 解析 Token 获取用户 Role
        try {
            String role = jwtHelper.getUserRole(token);
            Map<String, Object> response = new HashMap<>();
            response.put("status", true);
            response.put("message", "User is logged in");
            response.put("Role", role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
