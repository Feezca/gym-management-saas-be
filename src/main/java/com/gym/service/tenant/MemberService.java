package com.gym.service.tenant;

import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreateTrainerRequest;
import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.MemberResponse;
import com.gym.dto.response.TrainerResponse;
import com.gym.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse getMemberById(Long id);
    Page<MemberResponse> search(BaseFilterDto filter);
    List<TrainerResponse> getAllTrainers();
    MemberResponse updateMember(CreateMemberRequest request);
    void deleteMember(Long id);
    UserResponse createTrainer(CreateTrainerRequest request);
    Page<TrainerResponse> searchStaff(BaseFilterDto filter);
}
