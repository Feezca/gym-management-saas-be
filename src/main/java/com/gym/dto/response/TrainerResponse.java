package com.gym.dto.response;

import com.gym.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private BigDecimal agreedAmount;
    private PaymentType paymentType;
}
