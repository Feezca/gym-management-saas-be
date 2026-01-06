package com.gym.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "progress")
@Getter
@Setter
public class Progress extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
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
