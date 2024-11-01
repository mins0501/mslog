package com.mslog.controller;

import com.mslog.domain.Post;
import com.mslog.request.PostCreate;
import com.mslog.request.PostEdit;
import com.mslog.request.PostSearch;
import com.mslog.response.PostResponse;
import com.mslog.service.PostService;
import jakarta.persistence.PostUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("/post")
    public void write(@RequestBody @Valid PostCreate postCreate) {
        postCreate.validate();
        postService.write(postCreate);
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        PostResponse postResponse = postService.get(id);
        return postResponse;
    }

    @GetMapping("/post")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/post/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }

    @DeleteMapping("/post/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }


}
