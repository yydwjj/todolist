package com.yydwjj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
//@RequestMapping("/app")
public class TodoAppController {

    private static final Logger logger = LoggerFactory.getLogger(TodoAppController.class);

    @GetMapping("/")
    public String index(Model model) {
        logger.info("Index page requested");
        return "index";
    }

    @GetMapping("/todolist")
    public String todolist() {
        logger.info("Todo list page requested");
        return "todolist";
    }

    @GetMapping("/manage")
    public String manage() {
        logger.info("Manage page requested");
        return "manage";
    }
}