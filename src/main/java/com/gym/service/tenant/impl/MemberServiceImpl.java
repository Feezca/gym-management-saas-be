package com.gym.service.tenant.impl;

import com.gym.domain.public_.entity.Gym;
import com.gym.domain.public_.entity.User;
import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.MemberResponse;
import com.gym.domain.tenant.entity.Member;
import com.gym.dto.response.UserResponse;
import com.gym.enums.MemberStatus;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.mapper.UserMapper;
import com.gym.repository.public_.GymRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.repository.tenant.MemberRepository;
import com.gym.service.tenant.MemberService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final PasswordEncoder passwordEncoder;
    private SpecificationService<User> specificationService;

    public MemberServiceImpl(MemberRepository memberRepository,
                             UserRepository userRepository,
                             GymRepository gymRepository,
                             PasswordEncoder passwordEncoder,
                             SpecificationService<User> specificationService) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.gymRepository = gymRepository;
        this.passwordEncoder = passwordEncoder;
        this.specificationService = specificationService;
    }

    @Override
    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        log.debug("Creating new member with email: {}", request.getEmail());
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMembershipType(request.getMembershipType());
        member.setJoinDate(request.getJoinDate());
        member.setStatus(MemberStatus.ACTIVE);
        User user = userRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + request.getTrainerId()));
        member.setTrainer(user);

        Member savedMember = memberRepository.save(member);
        return mapToResponse(savedMember);
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return mapToResponse(member);
    }

    @Override
    public Page<MemberResponse> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long id, CreateMemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMembershipType(request.getMembershipType());
        member.setJoinDate(request.getJoinDate());
        member.setStatus(request.getStatus());
        //member.setTrainerId(request.getTrainerId());

        Member updatedMember = memberRepository.save(member);
        return mapToResponse(updatedMember);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    private MemberResponse mapToResponse(Member member) {
        MemberResponse res = new MemberResponse();
        res.setId(member.getId());
        res.setName(member.getName());
        res.setEmail(member.getEmail());
        res.setPhone(member.getPhone());
        res.setMembershipType(member.getMembershipType());
        res.setJoinDate(member.getJoinDate());
        res.setStatus(member.getStatus());
        return res;
    }


    @Override
    @Transactional
    public UserResponse createTrainer(CreateUserRequest request) {
        // 1️⃣ tenant actual (del JWT)
        String tenant = TenantContext.getTenant();

        Gym gym = gymRepository.findBySchemaName(tenant)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gym not found for tenant: " + tenant)
                );

        User trainer = new User();
        trainer.setName(request.getName());
        trainer.setEmail(request.getEmail());
        trainer.setRole(Role.TRAINER);
        trainer.setPassword(passwordEncoder.encode(request.getPassword()));
        trainer.setGym(gym);

        return UserMapper.userToUserResponse(userRepository.save(trainer));
    }

    @Override
    public Page<UserResponse> getAllTrainers(BaseFilterDto filter) {
        String tenant = TenantContext.getTenant();
        Gym gym = gymRepository.findBySchemaName(tenant)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gym not found for tenant: " + tenant)
                );

        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        Specification<User> spec = new SpecificationBuilder<User>()
                .equals("role", Role.TRAINER)
                .equals("gym.id", gym.getId())
                .equals("active", filter.getActive())
                .orLikeIgnoreCase(
                        List.of("name", "email"),
                        filter.getKeyword()
                )
                .build();

        return userRepository.findAll(spec, pageable)
                .map(UserMapper::userToUserResponse);

    }
}
