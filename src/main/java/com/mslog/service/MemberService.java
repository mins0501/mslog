package com.mslog.service;

import com.mslog.domain.Member;
import com.mslog.exception.MemberNotFound;
import com.mslog.repository.MemberRepository;
import com.mslog.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getUserProfile(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(MemberNotFound::new);

        return new MemberResponse(member);
    }

}
