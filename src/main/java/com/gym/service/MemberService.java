package com.gym.service;

import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.response.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse getMemberById(UUID id);
    Page<MemberResponse> getAllMembers(Pageable pageable);
    MemberResponse updateMember(UUID id, CreateMemberRequest request);
    void deleteMember(UUID id);
}
