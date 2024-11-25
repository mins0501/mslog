package com.mslog.config;

import com.mslog.domain.Member;
import com.mslog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class MslogMockSecurityContext implements WithSecurityContextFactory<MslogMockUser> {

    public final MemberRepository memberRepository;

    @Override
    public SecurityContext createSecurityContext(MslogMockUser annotation) {
        Member member = Member.builder()
                .name(annotation.name())
                .email(annotation.email())
                .password(annotation.password())
                .build();

        memberRepository.save(member);

        var principal = new UserPrincipal(member);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");

        var authenticationToken = new UsernamePasswordAuthenticationToken(principal, member.getPassword(), List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
