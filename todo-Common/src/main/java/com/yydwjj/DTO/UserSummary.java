package com.yydwjj.DTO;


import lombok.Data;

@Data
public class UserSummary {
    private Long id;
    private String name;

    public UserSummary(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
}