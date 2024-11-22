package com.mslog.service;

import com.mslog.domain.Member;
import com.mslog.exception.AlreadyExistsEmailException;
import com.mslog.repository.MemberRepository;
import com.mslog.request.Signup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void signup() {

        Signup signup = Signup.builder()
                        .name("mskym")
                        .password("1234")
                        .email("mins0501@gmail.com")
                        .build();

        authService.signup(signup);

        assertEquals(1L, memberRepository.count());

        Member member = memberRepository.findAll().iterator().next();
        assertEquals("mskym", member.getName());
        assertEquals("mins0501@gmail.com", member.getEmail());
        assertNotNull(member.getPassword());
        assertNotEquals("1234", member.getPassword());

    }

    @Test
    @DisplayName("중복 확인")
    public void duplicateSignup() {

        Member member = Member.builder()
                .email("mins0501@gmail.com")
                .password("1234")
                .name("mskym")
                .build();

        memberRepository.save(member);

        Signup signup = Signup.builder()
                .name("mskym")
                .password("1234")
                .email("mins0501@gmail.com")
                .build();

        Assertions.assertThrows(AlreadyExistsEmailException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                authService.signup(signup);
            }
        });
    }


}