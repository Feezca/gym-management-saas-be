package com.gym.dto.response;

import com.gym.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


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
