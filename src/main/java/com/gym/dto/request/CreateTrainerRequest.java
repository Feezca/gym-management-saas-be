package com.gym.dto.request;

import com.gym.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainerRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private String phone;

    @NotNull(message = "Agreed amount is required")
    @DecimalMin(value = "0.0", message = "Agreed amount cannot be negative")
    private BigDecimal agreedAmount;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;
}
