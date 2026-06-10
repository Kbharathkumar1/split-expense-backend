package com.splitexpense.service;

import com.splitexpense.dto.*;
import com.splitexpense.entity.*;
import com.splitexpense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    // addExpense()          → Expense add + auto split
    public ExpenseResponse addExpense(ExpenseRequest request) {

        User paidBy = userRepository.findById(request.getPaidById())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found!"));

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        Expense saved = expenseRepository.save(expense);

        // Split logic — equal split among all selected users
        int totalMembers = request.getSplitAmongUserIds().size();
        double splitAmount = Math.round((request.getAmount() / totalMembers) * 100.0) / 100.0;

        for (Long userId : request.getSplitAmongUserIds()) {
            User member = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found!"));

            ExpenseSplit split = new ExpenseSplit();
            split.setExpense(saved);
            split.setUser(member);
            split.setSplitAmount(splitAmount);

            // Person who paid — their split is already paid
            if (userId.longValue() == request.getPaidById().longValue()) {
                split.setIsPaid(true);
            } else {
                split.setIsPaid(false);
            }

            expenseSplitRepository.save(split);
        }

        return mapToResponse(saved);
    }

    //getExpensesByGroup()  → Group యొక్క అన్ని expenses
    public List<ExpenseResponse> getExpensesByGroup(Long groupId) {
        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        return expenses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //mapToResponse()       → Entity → DTO convert
    public ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setPaidBy(userService.mapToResponse(expense.getPaidBy()));
        response.setCreatedAt(expense.getCreatedAt());

        List<ExpenseSplit> splits = expenseSplitRepository.findByExpenseId(expense.getId());
        List<ExpenseResponse.SplitDetail> splitDetails = splits.stream().map(split -> {
            ExpenseResponse.SplitDetail detail = new ExpenseResponse.SplitDetail();
            detail.setUser(userService.mapToResponse(split.getUser()));
            detail.setSplitAmount(split.getSplitAmount());
            detail.setIsPaid(split.getIsPaid());
            return detail;
        }).collect(Collectors.toList());

        response.setSplits(splitDetails);
        return response;
    }
}