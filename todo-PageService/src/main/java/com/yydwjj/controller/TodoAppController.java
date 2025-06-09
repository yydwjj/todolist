package com.yydwjj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 页面服务接口
 * 提供页面文件接口，包括主页、待办事项页、用户管理页
 */
@Controller
//@RequestMapping("/app")
public class TodoAppController {

    private static final Logger logger = LoggerFactory.getLogger(TodoAppController.class);

    /**
     * 首页页面接口
     * 提供首页页面文件
     * @return 首页页面文件
     */
    @GetMapping("/")
    public String index() {
        logger.info("Index page requested");
        return "index";
    }

    /**
     * 待办事项页面接口
     * 提供待办事项页面文件
     * @return 待办事项页面文件
     */
    @GetMapping("/todolist")
    public String todolist() {
        logger.info("Todo list page requested");
        return "todolist";
    }

    /**
     * 用户管理页面接口
     * 提供用户管理页面的接口
     * @return 用户管理页面文件
     */
    @GetMapping("/manage")
    public String manage() {
        logger.info("Manage page requested");
        return "manage";
    }
}