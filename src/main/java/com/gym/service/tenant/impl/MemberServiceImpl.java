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
import com.gym.dto.response.MemberStatisticsResponse;
import com.gym.dto.response.TrainerResponse;
import com.gym.dto.response.UserResponse;
import com.gym.enums.MemberStatus;
import com.gym.enums.Role;
import com.gym.exception.BadRequestException;
import com.gym.exception.ConflictException;
import com.gym.exception.ResourceNotFoundException;
import com.gym.mapper.MemberMapper;
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

import java.time.LocalDateTime;
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
        return MemberMapper.mapToResponse(savedMember);
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return MemberMapper.mapToResponse(member);
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

        return memberRepository.findAll(spec, pageable).map(MemberMapper::mapToResponse);
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
        return MemberMapper.mapToResponse(updatedMember);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }


    @Override
    @Transactional
    public UserResponse createTrainer(CreateTrainerRequest request) {
        String tenant = TenantContext.getTenant();

        Gym gym = gymRepository.findBySchemaName(tenant)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Gym not found for tenant: " + tenant)
                );

        if (trainerRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Trainer already exists with email: " + request.getEmail());
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user != null) {
            if (user.getRole() != Role.TRAINER) {
                throw new ConflictException("User exists but does not have the TRAINER role.");
            }

            if (user.getTenant() == null) {
                user.setGym(gym);
                user.setTenant(tenant);
                user = userRepository.save(user);
            } else if (!tenant.equals(user.getTenant())) {
                throw new ConflictException("Trainer user is already associated with another gym.");
            }
        } else {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new BadRequestException("Password is required to create a new trainer user.");
            }

            user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.TRAINER);
            user.setEnabled(true);
            user.setGym(gym);
            user.setTenant(tenant);
            user = userRepository.save(user);
        }

        Trainer trainer = new Trainer();
        trainer.setEmail(request.getEmail());
        trainer.setName(request.getName());
        trainer.setPhone(request.getPhone());
        trainer.setAgreedAmount(request.getAgreedAmount());
        trainer.setPaymentType(request.getPaymentType());
        trainerRepository.save(trainer);

        return UserMapper.userToUserResponse(user);
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
        response.setAgreedAmount(entity.getAgreedAmount());
        response.setPaymentType(entity.getPaymentType());
        return response;
    }

    @Override
    public List<MemberResponse> lastFourMembers() {
        return memberRepository.findTop4ByOrderByJoinDateDesc()
                .stream()
                .map(MemberMapper::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteTrainer(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Trainer not found with id: " + id);
        }
        trainerRepository.deleteById(id);
    }

    @Override
    public MemberStatisticsResponse getMemberStatistics() {

        Long totalMembers = memberRepository.count();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        Long newMembersThisMonth = memberRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
        Long activeMembers = memberRepository.countByStatus(MemberStatus.ACTIVE);


        int growthPercentage = 0;

        if (totalMembers > 0) {
            growthPercentage = (int) ((newMembersThisMonth * 100) / totalMembers);
        }
        MemberStatisticsResponse response = new MemberStatisticsResponse();
        response.setTotalMembers(totalMembers);
        response.setActiveMembers(activeMembers);
        response.setNewMembersThisMonth(newMembersThisMonth);
        response.setMonthlyGrowthPercentage(growthPercentage);
        return response;
    }

    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberMapper::mapToResponse)
                .toList();
    }
}
