package com.splitexpense.dto;

import java.util.List;

import lombok.Data;

@Data
public class ExpenseRequest {
    private String description;
    private Double amount;
    private Long groupId;
    private Long paidById;
    private List<Long> splitAmongUserIds;
}