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
@Table(name = "group_members")
@Data
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//group_id → ManyToOne  
// ఒక్క group → చాలా rows లో కనిపిస్తుంది
// group_id బట్టి → ఆ group లో ఎవరెవరు ఉన్నారో తెలుసుకోవచ్చు
//=======OR========
//oka group lo chala members untaaru->so group_id is many to one, by seeing group id we can findout who all there in that group.
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

//user_id → ManyToOne
// ఒక్క user → చాలా rows లో కనిపిస్తాడు
// user_id బట్టి → వాడు ఎన్ని groups లో ఉన్నాడో తెలుసుకోవచ్చు 
//=======OR=========
//oka user chala groups lo untaadu -->so user_id is many to one, user id ni batti vaadu yenni groups lo unnadoo cheppachu   
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }
}

//GroupMember.java → ఏ person ఏ group లో ఉన్నాడో వీటి మధ్య bridge!