package com.gym.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gyms", schema = "public")
@Getter
@Setter
public class Gym extends BaseEntity{

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String schemaName;
    
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
