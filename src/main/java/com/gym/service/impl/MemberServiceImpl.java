package com.gym.service.impl;

import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.response.MemberResponse;
import com.gym.entity.Member;
import com.gym.exception.ResourceNotFoundException;
import com.gym.repository.MemberRepository;
import com.gym.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());
        member.setMembershipType(request.getMembershipType());
        member.setJoinDate(request.getJoinDate());
        member.setStatus(request.getStatus());
        //member.setTrainer(request.getTrainerId());

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
}
