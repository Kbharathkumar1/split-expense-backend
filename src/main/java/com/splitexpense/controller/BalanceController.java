package com.splitexpense.controller;

import com.splitexpense.dto.*;
import com.splitexpense.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/balances")
@CrossOrigin(origins = "http://localhost:5173/")
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