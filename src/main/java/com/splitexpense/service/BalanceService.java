package com.splitexpense.service;

import com.splitexpense.dto.*;
import com.splitexpense.entity.*;
import com.splitexpense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalanceService {

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    //getBalancesForGroup() → ఆ group లో balances చూపించడం
    public List<BalanceResponse> getBalancesForGroup(Long groupId) {

    // Step 1: Group లో అన్ని expenses తీసుకో
    List<ExpenseSplit> allSplits = expenseSplitRepository.findAll()
            .stream()
            .filter(split -> split.getExpense().getGroup().getId().equals(groupId)
                    && !split.getIsPaid())
            .collect(Collectors.toList());

    // Step 2: Calculate who owes whom
    Map<String, Double> balanceMap = new HashMap<>();

    for (ExpenseSplit split : allSplits) {
        Long owerId = split.getUser().getId();
        Long ownerId = split.getExpense().getPaidBy().getId();

        if (owerId.equals(ownerId)) continue;

        String key = owerId + "_" + ownerId;
        balanceMap.put(key, balanceMap.getOrDefault(key, 0.0) + split.getSplitAmount());
    }

    // Step 3: Convert to BalanceResponse
    List<BalanceResponse> balances = new ArrayList<>();

    for (Map.Entry<String, Double> entry : balanceMap.entrySet()) {
        String[] ids = entry.getKey().split("_");
        Long owerId = Long.parseLong(ids[0]);
        Long ownerId = Long.parseLong(ids[1]);

        User ower = userRepository.findById(owerId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        BalanceResponse balance = new BalanceResponse();
        balance.setOwedBy(userService.mapToResponse(ower));
        balance.setOwedTo(userService.mapToResponse(owner));
        balance.setAmount(Math.round(entry.getValue() * 100.0) / 100.0);

        balances.add(balance);
    }

    return balances;
}

    //settleUp()  → Debt clear చేయడం
    public String settleUp(Long payerId, Long receiverId, Long groupId, Double amount) {

        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found!"));

        // Mark splits as paid
        List<ExpenseSplit> unpaidSplits = expenseSplitRepository.findByUserId(payerId)
                .stream()
                .filter(s -> !s.getIsPaid()
                        && s.getExpense().getGroup().getId().equals(groupId)
                        && s.getExpense().getPaidBy().getId().equals(receiverId))
                .collect(Collectors.toList());

        for (ExpenseSplit split : unpaidSplits) {
            split.setIsPaid(true);
            expenseSplitRepository.save(split);
        }

        // Save settlement record
        Settlement settlement = new Settlement();
        settlement.setPaidBy(payer);
        settlement.setPaidTo(receiver);
        settlement.setGroup(group);
        settlement.setAmount(amount);
        settlementRepository.save(settlement);

        return payer.getName() + " settled ₹" + amount + " with " + receiver.getName();
    }
}