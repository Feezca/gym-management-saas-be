package com.gym.dto.response;

import com.gym.enums.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String avatar;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
