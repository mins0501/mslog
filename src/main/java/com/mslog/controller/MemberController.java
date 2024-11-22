package com.mslog.controller;

import com.mslog.config.UserPrincipal;
import com.mslog.domain.Member;
import com.mslog.response.MemberResponse;
import com.mslog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memeberService;

    @GetMapping("/member/me")
    public ResponseEntity<MemberResponse> getMe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MemberResponse userResponse = memeberService.getUserProfile(userPrincipal.getMemberId());
        return ResponseEntity.ok().body(userResponse);
    }

}
