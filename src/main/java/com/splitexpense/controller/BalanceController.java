package com.splitexpense.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.splitexpense.dto.BalanceResponse;
import com.splitexpense.service.BalanceService;

@RestController
@RequestMapping("/api/balances")
@CrossOrigin(origins =  {
    "http://localhost:5173",
    "https://split-expense-backend-iemv.onrender.com"
})
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<BalanceResponse>> getBalances(@PathVariable Long groupId) {
        return ResponseEntity.ok(balanceService.getBalancesForGroup(groupId));
    }

    @PostMapping("/settle")
    public ResponseEntity<String> settleUp(
            @RequestParam Long payerId,
            @RequestParam Long receiverId,
            @RequestParam Long groupId,
            @RequestParam Double amount) {
        return ResponseEntity.ok(balanceService.settleUp(payerId, receiverId, groupId, amount));
    }
}