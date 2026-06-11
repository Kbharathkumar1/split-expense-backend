package com.splitexpense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitexpense.dto.LoginRequest;
import com.splitexpense.dto.RegisterRequest;
import com.splitexpense.dto.UserResponse;
import com.splitexpense.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins =  {
    "http://localhost:5173",
     "https://split-expense-frontend-eta.vercel.app"
})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));    //ResponseEntity.ok(...) short-cut wrapper class. Idi HTTP Status 200 OK ni body data tho link chesthundi.
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}

// Short-cut: Nuvvu Java layer data ni pipeline loki thosthe (return ResponseEntity.ok), Java Object automatic ga body pipe nunchi bayataki vachedarki @RestController wrapper valla pressure tho JSON data ga transform ayi react code interface lo paduthundi.
