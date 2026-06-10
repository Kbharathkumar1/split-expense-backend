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
@Table(name = "expenses")
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    //చాలా expenses → ఒక్క group లో ఉంటాయి, అన్నీ same group_id=1 కలిగి ఉంటాయి అందుకే → @ManyToOne
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    //చాలా expenses → ఒక్కో user pay చేస్తాడు అందుకే → @ManyToOne on paid_by
    //ex:Goa Trip లో 3 members ఉన్నారు కానీ Dinner కి ఒక్కడే pay చేశాడు → Bharath
    @ManyToOne
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}