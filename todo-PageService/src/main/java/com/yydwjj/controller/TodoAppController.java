package com.yydwjj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoAppController {

	@GetMapping("/")
	public String index(Model model) {
    	return "index";
	}
	@GetMapping("/todolist")
	public String todolist() {
		return "todolist";
	}

//	@PostMapping("/add")
//	public String add(@ModelAttribute TodoItem requestItem) {
//		TodoItem item = new TodoItem(requestItem.getCategory(),requestItem.getName());
//		repository.save(item);
//	  	return "redirect:/";
//	}
//
//	@PostMapping("/update")
//	public String update(@ModelAttribute TodoListViewModel requestItems) {
//		for (TodoItem requestItem : requestItems.getTodoList() ) {
//			TodoItem item = new TodoItem(requestItem.getCategory(), requestItem.getName());
//			item.setComplete(requestItem.isComplete());
//			item.setId(requestItem.getId());
//			repository.save(item);
//		}
//		return "redirect:/";
//	}

  
}