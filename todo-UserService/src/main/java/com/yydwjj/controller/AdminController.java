package com.yydwjj.controller;

import com.yydwjj.DTO.UserSummary;
import com.yydwjj.pojo.User;
import com.yydwjj.repository.UserRepository;
import com.yydwjj.utils.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户管理
 * 处理用户相关的操作，包括新建、更新用户信息、软删除用户
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // 添加日志记录器
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private JwtHelper jwtHelper;

    /**
     * 获取用户列表
     * 获取所有 role 为 "user" 的用户、
     * 脱敏处理仅返回 id 和 name
     * @return 脱敏用户列表
     */
    @GetMapping("/users")
    public List<UserSummary> getAllUsers() {
        logger.info("Get all users request received");

        List<User> users = userRepository.findByRoleAndDeletedFalse("user");
        return users.stream()
                .map(user -> new UserSummary(user.getId(), user.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 添加用户
     * 只能添加用户而不能添加管理员
     * @param user 用户名+用户密码
     * @return 添加成功提示或添加失败提示
     */
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        logger.info("Add user request received");

        if (userRepository.existsByName(user.getName())) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }
        user.setRole("user"); // 默认设置 role 为 "user"
        userRepository.save(user);
        return ResponseEntity.ok("用户添加成功");
    }

    /**
     * 更新用户信息
     * 可以修改用户的用户名和密码
     * @param id 所修改的用户id
     * @param updatedUser 更新的用户信息
     * @return 用户更新结果
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        logger.info("Update user request received");

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.setName(updatedUser.getName());
        user.setPassword(updatedUser.getPassword());
//        user.setRole(updatedUser.getRole());

        userRepository.save(user);
        return ResponseEntity.ok("用户信息更新成功");
    }

    /**
     * 删除用户软删除
     * 在数据库中软删除用户，可以通过操作数据库的手段恢复
     * @param id 要删除的用户的id
     * @return 删除结果
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id,@RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Delete user request received");

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid token format");
        }

        String token = authorizationHeader.substring(7); // 去掉 "Bearer " 前缀

        Long deleteId = jwtHelper.getUserId(token);
        // 软删除方案：添加 isDeleted 字段
        User user = optionalUser.get();
        user.setDeleted(true); // 假设 User 类中新增了 isDeleted 字段
        user.setDeleterId(deleteId);
        userRepository.save(user);


        return ResponseEntity.ok("用户删除成功");
    }
}