package com.gym.dto.request;

import com.gym.dto.response.MemberResponse;
import com.gym.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayment {

    private MemberResponse member;

    private String amount;
    
    private LocalDateTime date;
    
    private PaymentStatus status;

    private String method;
}
