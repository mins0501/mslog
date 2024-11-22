package com.mslog.controller;

import com.mslog.config.UserPrincipal;
import com.mslog.request.PostCreate;
import com.mslog.request.PostEdit;
import com.mslog.request.PostSearch;
import com.mslog.response.PagingResponse;
import com.mslog.response.PostResponse;
import com.mslog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/gets")
    public String gets() {
        return "Hello World";
    }

    @PostMapping("/posts")
    public String posts(@RequestBody PostCreate postCreate) {
        log.info("params = {}", postCreate);

        return "Hello World";
    }

    @PostMapping("/dataValid")
    public Map<String, String> dataValid(@RequestBody @Valid PostCreate postCreate) {
        return Map.of();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public void write(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostCreate postCreate) {
        postService.write(userPrincipal.getMemberId(), postCreate);
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        PostResponse postResponse = postService.get(id);
        return postResponse;
    }

    @GetMapping("/post")
    public PagingResponse<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/post/{postId}")
    public void edit(@PathVariable(name = "postId") Long id, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(id, postEdit);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/post/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }


}
