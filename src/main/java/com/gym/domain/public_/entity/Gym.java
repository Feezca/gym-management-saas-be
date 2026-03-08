package com.gym.domain.public_.entity;

import com.gym.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "gyms", schema = "public")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Gym {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gyms_seq")
    @SequenceGenerator(name = "gyms_seq", sequenceName = "gyms_seq",  schema = "public")
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(name = "schema_name", nullable = false)
    private String schemaName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
