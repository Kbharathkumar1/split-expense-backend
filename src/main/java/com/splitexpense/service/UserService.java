package com.splitexpense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.splitexpense.dto.LoginRequest;
import com.splitexpense.dto.RegisterRequest;
import com.splitexpense.dto.UserResponse;
import com.splitexpense.entity.User;
import com.splitexpense.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //register()    → New user create చేయడం
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }
//checking credientials are exist in DB or not
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        return mapToResponse(user);
    }

    //getUserById() → Existing user details fetch cheyadaniki...  Long id value comes with url ,like @PathVariable..
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return mapToResponse(user);
    }

    public UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        return response;
    }
}


// ✅ register() → Email duplicate check + 
//                 BCrypt encode + save
// ✅ login()    → Email check + 
//                 Password verify
// ✅ getUserById() → id తో user fetch
// ✅ mapToResponse() → Entity → DTO convert