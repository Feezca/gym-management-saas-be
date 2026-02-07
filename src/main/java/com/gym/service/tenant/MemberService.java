package com.gym.service.tenant;

import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.MemberResponse;
import com.gym.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse getMemberById(Long id);
    Page<MemberResponse> getAllMembers(Pageable pageable);
    MemberResponse updateMember(Long id, CreateMemberRequest request);
    void deleteMember(Long id);
    UserResponse createTrainer(CreateUserRequest request);
}
