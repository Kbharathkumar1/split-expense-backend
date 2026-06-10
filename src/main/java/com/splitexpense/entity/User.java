package com.splitexpense.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data   //lombok-> getters, setters, toString అన్నీ automatically generate చేస్తుంది
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)    //email లేకుండా register చేయలేరు,  same email తో రెండు accounts కాదు
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist //మనం manually time set చేయక్కర్లేదు,  Save అయ్యే ముందు ఒక్కసారి automatically పిలవబడే method
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}



//summary:
// ఒక person register చేసుకుంటాడు
// → User object create అవుతుంది
// → id automatically 1,2,3 అవుతుంది
// → email unique గా check అవుతుంది
// → createdAt automatically set అవుతుంది
// → Database లో ఒక్క row గా save అవుతుంది