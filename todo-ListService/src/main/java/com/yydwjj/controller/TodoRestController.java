package com.yydwjj.controller;

import com.yydwjj.pojo.*;
import com.yydwjj.repository.TodoItemRepository;
import com.yydwjj.utils.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 待办事项管理
 * 处理待办事项的相关操作，包括创建、修改、查询待办事项
 */
@RestController
@RequestMapping(path = "/todo")
public class TodoRestController {

    @Autowired
    private TodoItemRepository repository;
    @Autowired
    JwtHelper jwtHelper;

    private static final Logger logger = LoggerFactory.getLogger(TodoRestController.class);

    /**
     * 查询全部待办事项
     * 如果角色是用户，则查询此用户的相关待办事项。
     * 如果角色是股那里元，则查询全部的待办事项
     * @param authorizationHeader jwtToken
     * @return 待办事项列表
     */
    @GetMapping("/all")
    public @ResponseBody Iterable<TodoItem> getAllByUser(@RequestHeader("Authorization") String authorizationHeader) {
        logger.info("getAllByUser");
        // 1. 提取 Token
        String token = authorizationHeader.substring(7);
        Long uid = jwtHelper.getUserId(token);
        String role = jwtHelper.getUserRole(token);
        Iterable<TodoItem> todoList = null;
        if (role.equals("admin")) {
            todoList = repository.findAll();
        } else {
            todoList = repository.findByUser_Id(uid);
        }
        return todoList;
    }

    /**
     * 添加待办事项
     * @param request 待办事项详情，包括事项分类和事项藐视
     * @param authorizationHeader jwtToken
     * @return 成功返回添加的事项，失败返回null与失败提示
     */
    @PostMapping("/add")
    public @ResponseBody Result addItemByUser(@RequestBody TodoItem request, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("addItemByUser");
        String token = authorizationHeader.substring(7);
        Long uid = jwtHelper.getUserId(token);
        String role = jwtHelper.getUserRole(token);
        if (role.equals("admin")) {
            return new Result("管理员不可以添加代办事项", null);
        } else {
            // 1. 根据用户ID查找用户
//            User user = userRepository.findById(uid)
//                    .orElseThrow(() -> new RuntimeException("User not found"));

            User user = new User();
            user.setId(uid);
            // 2. 创建 TodoItem 并关联用户
            String name = request.getName();
            String category = request.getCategory();
            TodoItem item = new TodoItem();
            item.setCategory(category);
            item.setName(name);
            item.setUser(user); // 关键步骤：设置 user 字段

            // 3. 保存到数据库
            TodoItem saved = repository.save(item);

            return new Result("Added", item);
        }
    }

    /**
     * 更新待办事项
     * 可以更新待办事项的状态、标题、分类
     * @param request 需要更新的事项
     * @param authorizationHeader jwtToken
     * @return 返回更新结果
     */
    @PostMapping("/update")
    public @ResponseBody Result updateItemByUser(@RequestBody TodoItem request, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("updateItemByUser");
        String token = authorizationHeader.substring(7);
        Long uid = jwtHelper.getUserId(token);
        Long id = request.getId();
        String category = request.getCategory();
        String name = request.getName();
        boolean isComplete = request.isComplete();

        // 1. 根据ID查找待更新的 TodoItem
        TodoItem existingItem = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TodoItem not found"));
        String role = jwtHelper.getUserRole(token);
        TodoItem saved = null;
        //如果是管理员直接修改
        if (role.equals("admin")) {
            existingItem.setCategory(category);
            existingItem.setName(name);
            existingItem.setComplete(isComplete);
            repository.save(existingItem);
        } else {
            //否则进行两部判断
            // 2. 根据用户ID查找用户（确保用户存在）
//            User user = userRepository.findById(uid)
//                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3. 检查用户是否有权修改该 TodoItem（关键步骤）
            if (!existingItem.getUser().getId().equals(uid)) {
                throw new RuntimeException("Unauthorized to update this TodoItem");
            }

            // 4. 更新字段
            existingItem.setName(name);
            existingItem.setCategory(category);
            existingItem.setComplete(isComplete);

            // 5. 保存到数据库
            repository.save(existingItem);
        }

        return new Result("Updated", saved);
    }

    class Result {
        private String status;
        private TodoItem item;

        public Result() {
            status = "";
            item = null;
        }

        public Result(String status, TodoItem item) {
            this.status = status;
            this.item = item;
        }

        public TodoItem getItem() {
            return item;
        }

        public void setItem(TodoItem item) {
            this.item = item;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
