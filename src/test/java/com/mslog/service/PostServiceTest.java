package com.mslog.service;

import com.mslog.domain.Post;
import com.mslog.exception.PostNotFound;
import com.mslog.repository.PostRepository;
import com.mslog.request.PostCreate;
import com.mslog.request.PostEdit;
import com.mslog.request.PostSearch;
import com.mslog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    public void write() {

        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();

        postService.write(postCreate);

        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);

        Assertions.assertEquals("제목입니다.", post.getTitle());
        Assertions.assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1건 조회")
    public void get() {

        Post requestPost = Post.builder().title("foo").content("bar").build();
        postRepository.save(requestPost);

        PostResponse postResponse = postService.get(requestPost.getId());

        Assertions.assertNotNull(postResponse);
        Assertions.assertEquals(1L, postRepository.count());
        Assertions.assertEquals("foo", postResponse.getTitle());
        Assertions.assertEquals("bar", postResponse.getContent());

        Assertions.assertThrows(PostNotFound.class, () -> postService.get(requestPost.getId() + 1L));
    }

    @Test
    @DisplayName("글 1페이지 조회")
    public void getList() {

        List<Post> requestPost = IntStream.range(1, 31)
                        .mapToObj(i -> {
                            return Post.builder()
                                    .title("foo" + i)
                                    .content("bar" + i)
                                    .build();
                        })
                                .collect(Collectors.toList());

        postRepository.saveAll(requestPost);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        PostSearch postSearch = PostSearch.builder().page(1).size(10).build();

        List<PostResponse> post = postService.getList(postSearch);

        Assertions.assertNotNull(post);
        Assertions.assertEquals(10L, post.size());
        Assertions.assertEquals("foo30", post.get(0).getTitle());
        Assertions.assertEquals("bar30", post.get(0).getContent());
    }

    @Test
    @DisplayName("글 수정")
    public void edit() {

        Post post = Post.builder().title("foo").content("bar").build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("far").content("boo").build();

        postService.edit(post.getId(), postEdit);

        Post changePost = postRepository.findById(post.getId()).orElseThrow(() -> new PostNotFound());

        Assertions.assertEquals("far", changePost.getTitle());
        Assertions.assertEquals("boo", changePost.getContent());

        Assertions.assertThrows(PostNotFound.class, () -> postService.edit(post.getId() + 1L, postEdit));
    }

    @Test
    @DisplayName("글 삭제")
    public void delete() {

        Post post = Post.builder().title("foo").content("bar").build();

        postRepository.save(post);

        postService.delete(post.getId());

        Assertions.assertEquals(0, postRepository.count());

        Assertions.assertThrows(PostNotFound.class, () -> postService.delete(post.getId() + 1L));
    }

    @Test
    @DisplayName("예외 처리")
    public void error() throws Exception {
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        Assertions.assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 1L));

    }

}
