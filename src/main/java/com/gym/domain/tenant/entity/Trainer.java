package com.gym.domain.tenant.entity;

import com.gym.domain.public_.entity.User;
import com.gym.entity.BaseEntity;
import com.gym.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "trainers")
@Getter
@Setter
public class Trainer extends BaseEntity {

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    private String phone;

}
