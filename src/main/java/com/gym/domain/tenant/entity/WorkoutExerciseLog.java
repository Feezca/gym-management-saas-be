package com.gym.domain.tenant.entity;

import com.gym.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "workout_exercise_logs")
@Getter
@Setter
public class WorkoutExerciseLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "workout_log_id", nullable = false)
    private WorkoutLog workoutLog;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    private BigDecimal weight;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}
