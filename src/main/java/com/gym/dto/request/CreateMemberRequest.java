package com.gym.dto.request;

import com.gym.enums.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    private String phone;
    
    private String membershipType;
    
    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    @NotNull(message = "Member status is required")
    private MemberStatus status = MemberStatus.ACTIVE;

    @NotNull(message = "Trainer ID is required")
    private Long trainerId;
}
