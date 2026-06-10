package com.splitexpense.dto;

import lombok.Data;

@Data
public class BalanceResponse {
    private UserResponse owedBy;
    private UserResponse owedTo;
    private Double amount;
}