package com.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID memberId;
    
    @Column(nullable = false)
    private LocalDate date;
    
    private BigDecimal weight;
    
    private BigDecimal bodyFat;
    
    private BigDecimal chest;
    
    private BigDecimal waist;
    
    private BigDecimal arms;
    
    private BigDecimal legs;
    
    @ElementCollection
    @CollectionTable(name = "progress_photos", joinColumns = @JoinColumn(name = "progress_id"))
    @Column(name = "photo_url")
    private List<String> photos = new ArrayList<>();
}
