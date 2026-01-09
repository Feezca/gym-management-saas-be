package com.gym.dto.response;

import com.gym.enums.MemberStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String membershipType;
    private LocalDate joinDate;
    private MemberStatus status;
    private Long trainerId;
}
