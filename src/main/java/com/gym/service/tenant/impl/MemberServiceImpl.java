package com.gym.service.tenant.impl;

import com.gym.domain.public_.entity.Gym;
import com.gym.domain.public_.entity.User;
import com.gym.domain.tenant.entity.Trainer;
import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreateTrainerRequest;
import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.MemberResponse;
import com.gym.domain.tenant.entity.Member;
import com.gym.dto.response.TrainerResponse;
import com.gym.dto.response.UserResponse;
import com.gym.enums.MemberStatus;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.mapper.UserMapper;
import com.gym.repository.public_.GymRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.repository.tenant.MemberRepository;
import com.gym.repository.tenant.TrainerRepository;
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
    private final TrainerRepository trainerRepository;
    private final GymRepository gymRepository;
    private final PasswordEncoder passwordEncoder;
    private SpecificationService<User> specificationService;

    public MemberServiceImpl(MemberRepository memberRepository,
                             UserRepository userRepository,
                             TrainerRepository trainerRepository,
                             GymRepository gymRepository,
                             PasswordEncoder passwordEncoder,
                             SpecificationService<User> specificationService) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
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
        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + request.getTrainerId()));
        member.setTrainer(trainer);

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
    public Page<MemberResponse> search(BaseFilterDto filter) {
        String tenant = TenantContext.getTenant();
        Gym gym = gymRepository.findBySchemaName(tenant)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gym not found for tenant: " + tenant)
                );

        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        Specification<Member> spec = new SpecificationBuilder<Member>()
                .orLikeIgnoreCase(
                        List.of("name", "email"),
                        filter.getKeyword()
                )
                .build();

        return memberRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(CreateMemberRequest request) {
        Member member = memberRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getId()));

        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMembershipType(request.getMembershipType());
        member.setJoinDate(request.getJoinDate());
        member.setStatus(request.getStatus());
        if(request.getTrainerId() != null && !request.getTrainerId().equals(member.getTrainer().getId())) {
            Trainer trainer  = trainerRepository.findById(request.getTrainerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + request.getTrainerId()));
            member.setTrainer(trainer);
        }


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
    public UserResponse createTrainer(CreateTrainerRequest request) {
        // 1️⃣ tenant actual (del JWT)
        String tenant = TenantContext.getTenant();

        Gym gym = gymRepository.findBySchemaName(tenant)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gym not found for tenant: " + tenant)
                );

        if (trainerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Trainer already exists with email: " + request.getEmail());
        }
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (existingUser != null && !existingUser.getRole().equals(Role.TRAINER)) {
            throw new IllegalArgumentException("User exists but does not have the TRAINER role.");
        }

        Trainer trainer = new Trainer();
        trainer.setEmail(request.getEmail());
        trainer.setName(request.getName());
        trainer.setPhone(request.getPhone());
        trainerRepository.save(trainer);

        if (existingUser == null) {
            User userTrainer = new User();
            userTrainer.setName(request.getName());
            userTrainer.setEmail(request.getEmail());
            userTrainer.setRole(Role.TRAINER);
            userTrainer.setPassword(passwordEncoder.encode(request.getPassword()));
            userTrainer.setGym(gym);
            userTrainer = userRepository.save(userTrainer);

            return UserMapper.userToUserResponse(userTrainer);
        }
        return UserMapper.userToUserResponse(existingUser);
    }

    @Override
    public Page<TrainerResponse> searchStaff(BaseFilterDto filter) {

        int page = filter.getPage() != null ? filter.getPage() : 0;
        int size = filter.getSize() != null ? filter.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        Specification<Trainer> spec = new SpecificationBuilder<Trainer>()
                .orLikeIgnoreCase(
                        List.of("name", "email", "phone"),
                        filter.getKeyword()
                )
                .build();

        return trainerRepository.findAll(spec, pageable)
                .map(this::trainerToResponse);

    }


    @Override
    public List<TrainerResponse> getAllTrainers() {

        return trainerRepository.findAll()
                .stream()
                .map(this::trainerToResponse)
                .toList();
    }

    public TrainerResponse trainerToResponse(Trainer entity){
        TrainerResponse response = new TrainerResponse();
        response.setEmail(entity.getEmail());
        response.setName(entity.getName());
        response.setPhone(entity.getPhone());
        response.setId(entity.getId());
        return response;
    }
}
