package com.gym.domain.public_.entity;

import com.gym.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "gyms")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Gym extends BaseEntity {
    @Column(nullable = false)
    private String name;
    
    @Column(name = "schema_name", nullable = false)
    private String schemaName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

}
