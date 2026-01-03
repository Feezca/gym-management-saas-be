package com.gym.dto.response;

import com.gym.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String membershipType;
    private LocalDate joinDate;
    private MemberStatus status;
    private UUID trainerId;
}
