package com.gym.repository;

import com.gym.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, UUID> {
    List<Routine> findByMemberId(UUID memberId);
    List<Routine> findByTrainerId(UUID trainerId);
}
