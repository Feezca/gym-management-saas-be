package com.gym.repository.tenant;

import com.gym.domain.tenant.entity.Member;
import com.gym.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findByStatus(MemberStatus status, Pageable pageable);
    List<Member> findByTrainerId(Long trainerId);
}
