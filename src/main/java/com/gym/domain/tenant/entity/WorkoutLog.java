package com.gym.domain.tenant.entity;

import com.gym.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "workout_logs")
@Getter
@Setter
public class WorkoutLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(nullable = false)
    private LocalDate date;
}
