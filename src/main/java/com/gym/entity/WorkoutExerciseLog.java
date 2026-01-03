package com.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "workout_exercise_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID workoutLogId;
    
    @Column(nullable = false)
    private UUID exerciseId;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    private BigDecimal weight;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}
