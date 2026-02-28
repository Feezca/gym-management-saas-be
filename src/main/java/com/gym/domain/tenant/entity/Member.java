package com.gym.domain.tenant.entity;

import com.gym.domain.public_.entity.User;
import com.gym.entity.BaseEntity;
import com.gym.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "members")
@Getter
@Setter
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    private String phone;
    
    private String membershipType;
    
    @Column(nullable = false)
    private LocalDate joinDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;
}
