package com.gym.dto.response;

import com.gym.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
}
