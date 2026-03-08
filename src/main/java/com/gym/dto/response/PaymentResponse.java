package com.gym.dto.response;

import com.gym.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private MemberResponse member;
    private BigDecimal amount;
    private LocalDate date;
    private PaymentStatus status;
    private String method;
}
