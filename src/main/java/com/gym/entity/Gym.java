package com.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "gyms", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String schemaName;
    
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
