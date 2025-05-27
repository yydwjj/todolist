package com.yydwjj.controller;

import com.yydwjj.DTO.UserSummary;
import com.yydwjj.pojo.User;
import com.yydwjj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有 role 为 "user" 的用户（仅返回 id 和 name）
     */
    @GetMapping("/users")
    public List<UserSummary> getAllUsers() {
        List<User> users = userRepository.findByRole("user");
        return users.stream()
                .map(user -> new UserSummary(user.getId(), user.getName()))
                .collect(Collectors.toList());
    }

    /**
     * 添加用户
     */
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (userRepository.existsByName(user.getName())) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }
        user.setRole("user"); // 默认设置 role 为 "user"
        userRepository.save(user);
        return ResponseEntity.ok("用户添加成功");
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
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
     * 删除用户（软删除或物理删除）
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 软删除方案：添加 isDeleted 字段
        User user = optionalUser.get();
        user.setDeleted(true); // 假设 User 类中新增了 isDeleted 字段
        userRepository.save(user);

        // 物理删除方案（直接删除）：
        // userRepository.deleteById(id);

        return ResponseEntity.ok("用户删除成功");
    }
}
