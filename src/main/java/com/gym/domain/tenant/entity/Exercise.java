package com.gym.domain.tenant.entity;

import com.gym.domain.public_.entity.ExerciseGlobal;
import com.gym.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "exercises")
@Getter
@Setter
public class Exercise extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "catalog_exercise_id", nullable = false)
    private ExerciseGlobal catalogExercise;

    @Column(name = "custom_name")
    private String customName;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;
}
