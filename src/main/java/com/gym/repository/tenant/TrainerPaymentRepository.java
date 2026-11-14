package com.gym.repository.tenant;

import com.gym.domain.tenant.entity.TrainerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerPaymentRepository extends JpaRepository<TrainerPayment, Long> {
    List<TrainerPayment> findByTrainerIdOrderByDateDesc(Long trainerId);
}
