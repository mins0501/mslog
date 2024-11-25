package com.mslog.service;

import com.mslog.domain.Post;
import com.mslog.domain.PostEditor;
import com.mslog.exception.MemberNotFound;
import com.mslog.exception.PostNotFound;
import com.mslog.repository.MemberRepository;
import com.mslog.repository.PostRepository;
import com.mslog.request.PostCreate;
import com.mslog.request.PostEdit;
import com.mslog.request.PostSearch;
import com.mslog.response.PagingResponse;
import com.mslog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void write(Long memberId, PostCreate postCreate) {
        var member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFound());
        Post post = Post.builder()
                        .member(member)
                        .title(postCreate.getTitle())
                        .content(postCreate.getContent())
                        .build();
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFound());
        return new PostResponse(post);
    }

    public PagingResponse<PostResponse> getList(PostSearch postSearch) {
        Page<Post> postPage = postRepository.getList(postSearch);
        PagingResponse<PostResponse> postList = new PagingResponse<>(postPage, PostResponse.class);
        return postList;
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle()).content(postEdit.getContent()).build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);
    }

}
