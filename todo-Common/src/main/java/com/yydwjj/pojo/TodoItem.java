package com.yydwjj.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;
import javax.persistence.*;

@Entity
@Data
@ToString(exclude = {"user"}) // 排除 user 字段
public class TodoItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String category;

    public boolean isComplete() {
        return complete;
    }

    private String name;
    private boolean complete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference // 忽略反向引用
    @JoinColumn(name = "user_id")
    private User user;

    public TodoItem() {}

    public TodoItem(String category, String name) {
        this.category = category;
        this.name = name;
        this.complete = false;
    }

}