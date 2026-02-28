package com.gym.repository.public_;

import com.gym.domain.public_.entity.User;
import com.gym.dto.request.BaseFilterDto;
import com.gym.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByIdAndRole(Long id, Role role);
    List<User> findAllByRoleAndGymId(Role role, Long gymId);
}
