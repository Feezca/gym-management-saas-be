package com.gym.controller;

import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreatePayment;
import com.gym.dto.response.PaymentResponse;
import com.gym.service.tenant.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResponse  createPayment(@Valid @RequestBody CreatePayment request) {
        return service.createPayment(request);
    }

    @GetMapping("/search")
    public Page<PaymentResponse> search(@ModelAttribute BaseFilterDto filter) {
        return service.search(filter);
    }

    @GetMapping()
    public List<PaymentResponse> getAll() {
        return service.getAll();
    }
}
