package com.gym.repository;

import com.gym.entity.Payment;
import com.gym.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByMemberId(Long memberId, Pageable pageable);
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
}
