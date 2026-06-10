package com.splitexpense.dto;

import java.util.List;

import lombok.Data;

@Data
public class GroupRequest {
    private String name;
    private String description;
    private Long createdBy;
    private List<Long> memberIds;
}