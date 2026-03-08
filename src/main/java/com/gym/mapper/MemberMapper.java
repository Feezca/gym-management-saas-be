package com.gym.mapper;

import com.gym.domain.public_.entity.User;
import com.gym.domain.tenant.entity.Member;
import com.gym.dto.response.CurrentUserResponse;
import com.gym.dto.response.MemberResponse;
import com.gym.dto.response.UserResponse;

public class MemberMapper {

    public static MemberResponse mapToResponse(Member member) {
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

}
