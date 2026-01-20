package com.gym.repository.tenant;

import com.gym.domain.tenant.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByMemberId(Long memberId);
    List<Routine> findByTrainerId(Long trainerId);
}
