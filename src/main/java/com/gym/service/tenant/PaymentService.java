package com.gym.service.tenant;

import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreatePayment;
import com.gym.dto.request.CreateTrainerRequest;
import com.gym.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(CreatePayment request);
    Page<PaymentResponse> search(BaseFilterDto filter);
    List<PaymentResponse> getAll();

}
