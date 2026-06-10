package com.splitexpense.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "expense_groups")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne  //Relationship define చేస్తుంది (Many groups → One user:oka user many groups create cheyochu)
    @JoinColumn(name = "created_by", nullable = false)  //Database లో ఆ relationship ఏ column లో store అవుతుందో చెప్తుంది
    private User createdBy;

    @Column(name = "created_at")    
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}


//Group.java అనేది మన application లో ఒక్కో friend group ని represent చేస్తుంది.

// Overall Group.java లో జరిగేది:
// "Goa Trip" group create అయినప్పుడు →
// → id = 1 automatically
// → name = "Goa Trip"
// → description = "Trip expenses"
// → created_by = 1 (Bharath యొక్క id) ---> evadaithe(user) group create chesintaadoo vani id store ithundi
// → created_at = current time
// → Database లో ఒక్క row గా save అవుతుంది