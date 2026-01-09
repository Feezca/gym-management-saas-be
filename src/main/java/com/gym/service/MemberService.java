package com.gym.service;

import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.response.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MemberService {
    MemberResponse createMember(CreateMemberRequest request);
    MemberResponse getMemberById(Long id);
    Page<MemberResponse> getAllMembers(Pageable pageable);
    MemberResponse updateMember(Long id, CreateMemberRequest request);
    void deleteMember(Long id);
}
