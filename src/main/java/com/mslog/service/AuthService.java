package com.mslog.service;

import com.mslog.domain.Member;
import com.mslog.exception.AlreadyExistsEmailException;
import com.mslog.repository.MemberRepository;
import com.mslog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(Signup signup) {
        Optional<Member> memberOptional = memberRepository.findByEmail(signup.getEmail());
        if (memberOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encode(signup.getPassword());

        Member member = Member.builder()
                        .name(signup.getName())
                        .password(encryptedPassword)
                        .email(signup.getEmail())
                        .build();
        memberRepository.save(member);
    }
}
