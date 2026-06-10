package com.splitexpense.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.splitexpense.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   //Login చేసేటప్పుడు — email enter చేస్తాడు కదా, ఆ user ని find చేయాలి!
    Boolean existsByEmail(String email);    //ఈ email already registered అయిందా లేదా check చేస్తుంది
}

//why used Optional,bcz simply--> it ised to null checks...
// DB లో null    → empty గా ఉంది అంతే
// Java లో null  → handle చేయకపోతే 💥 NullPointerException

// Optional వల్ల →
// null ని catch చేసి
// "User not found!" అని
// clear message చూపిస్తాం ✅