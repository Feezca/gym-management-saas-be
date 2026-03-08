package com.gym.domain.public_.entity;

import com.gym.enums.Equipment;
import com.gym.enums.MuscleGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "exercises", schema = "public")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ExerciseGlobal {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercises_seq")
    @SequenceGenerator(name = "exercises_seq", sequenceName = "exercises_seq", schema = "public")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group", nullable = false)
    private MuscleGroup muscleGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment")
    private Equipment equipment;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;


    @Column(name = "media_url")
    private String mediaUrl;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
