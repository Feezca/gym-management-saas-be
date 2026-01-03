package com.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID routineId;
    
    @Column(nullable = false)
    private String name;
    
    private Integer sets;
    
    private Integer reps;
    
    private Integer rest; // in seconds
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}
