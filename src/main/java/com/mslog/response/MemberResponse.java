package com.mslog.response;

import com.mslog.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long id;
    private final String name;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }

}