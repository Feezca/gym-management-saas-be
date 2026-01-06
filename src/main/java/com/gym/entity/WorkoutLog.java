package com.gym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "workout_logs")
@Getter
@Setter
public class WorkoutLog extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(nullable = false)
    private LocalDate date;
}
