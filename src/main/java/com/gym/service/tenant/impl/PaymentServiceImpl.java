package com.gym.service.tenant.impl;

import com.gym.domain.public_.entity.Gym;
import com.gym.domain.public_.entity.User;
import com.gym.domain.tenant.entity.Member;
import com.gym.domain.tenant.entity.Payment;
import com.gym.domain.tenant.entity.Trainer;
import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreatePayment;
import com.gym.dto.request.CreateTrainerRequest;
import com.gym.dto.response.*;
import com.gym.enums.MemberStatus;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.mapper.MemberMapper;
import com.gym.mapper.UserMapper;
import com.gym.repository.public_.GymRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.repository.tenant.MemberRepository;
import com.gym.repository.tenant.PaymentRepository;
import com.gym.repository.tenant.TrainerRepository;
import com.gym.service.tenant.MemberService;
import com.gym.service.tenant.PaymentService;
import com.gym.util.SpecificationBuilder;
import com.gym.util.SpecificationService;
import com.gym.util.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final MemberRepository memberRepository;
    private SpecificationService<Payment> specificationService;

    public PaymentServiceImpl(PaymentRepository repository,
                                MemberRepository memberRepository,
                              SpecificationService<Payment> specificationService) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.specificationService = specificationService;
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePayment request) {
        Payment payment = new Payment();

        Long longValueAmount = Long.valueOf(request.getAmount());
        BigDecimal amount = BigDecimal.valueOf(longValueAmount);

        payment.setAmount(amount);
        LocalDate date = LocalDate.from(request.getDate());
        payment.setDate(date);

        payment.setStatus(request.getStatus());
        payment.setMember(memberRepository.findById(request.getMember().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getMember().getId())));
        payment.setMethod(request.getMethod());

        Payment savedMember = repository.save(payment);

        return mapToResponse(savedMember);
    }

    @Override
    public Page<PaymentResponse> search(BaseFilterDto filter) {

        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        Specification<Payment> spec = new SpecificationBuilder<Payment>()
                .build();

        return repository.findAll(spec, pageable).map(this::mapToResponse);
    }



    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse res = new PaymentResponse();
        res.setId(payment.getId());
        res.setMember(MemberMapper.mapToResponse(payment.getMember()));
        res.setMethod(payment.getMethod());
        res.setDate(payment.getDate());
        res.setAmount(payment.getAmount());
        res.setStatus(payment.getStatus());
        return res;
    }

    @Override
    public List<PaymentResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

}
