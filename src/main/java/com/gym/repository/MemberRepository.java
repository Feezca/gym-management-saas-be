package com.gym.repository;

import com.gym.entity.Member;
import com.gym.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Page<Member> findByStatus(MemberStatus status, Pageable pageable);
    List<Member> findByTrainerId(UUID trainerId);
}
