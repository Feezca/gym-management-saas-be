package com.gym.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class Exercise extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;
    
    @Column(nullable = false)
    private String name;
    
    private Integer sets;
    
    private Integer reps;
    
    private Integer rest; // in seconds
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}
