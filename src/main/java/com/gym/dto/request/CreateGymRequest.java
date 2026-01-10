package com.gym.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGymRequest {
    @NotBlank
    private String name;

    @NotNull
    private Long adminUserId; // ID del usuario con rol de admin

}
