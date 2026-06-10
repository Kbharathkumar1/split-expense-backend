package com.splitexpense.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ExpenseResponse {
    private Long id;
    private String description;
    private Double amount;
    private UserResponse paidBy;
    private List<SplitDetail> splits;
    private LocalDateTime createdAt;

    @Data
    public static class SplitDetail {
        private UserResponse user;
        private Double splitAmount;
        private Boolean isPaid;
    }
}