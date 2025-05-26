package com.yydwjj.repository;

import com.yydwjj.pojo.TodoItem;
import com.yydwjj.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByUser(User user);
    List<TodoItem> findByUser_Id(Long userId);

    @Query("SELECT t FROM TodoItem t JOIN FETCH t.user WHERE t.user.id = :userId")
    List<TodoItem> findByUserIdWithUser(@Param("userId") Long userId);
}