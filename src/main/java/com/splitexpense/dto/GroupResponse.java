package com.splitexpense.dto;

import java.util.List;

import lombok.Data;

@Data
public class GroupResponse {
    private Long id;
    private String name;
    private String description;
    private UserResponse createdBy;
    private List<UserResponse> members;
}