package com.gym.domain.tenant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "progress_photos")
@Getter
@Setter
public class ProgressPhotos {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "progress_id", nullable = false)
    private Progress progress;

    @Column(name = "photo_url")
    private String photoUrl;
}
