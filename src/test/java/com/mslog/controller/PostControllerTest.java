package com.mslog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mslog.domain.Post;
import com.mslog.repository.PostRepository;
import com.mslog.request.PostCreate;
import com.mslog.request.PostEdit;
import com.mslog.service.PostService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/gets 요청 - Hello World 출력")
    public void getsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/gets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 - Hello World 출력")
    public void postsTest() throws Exception {
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();
        String jsonData = objectMapper.writeValueAsString(postCreate);
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Data 검증")
    public void dataValidTest() throws Exception {
        PostCreate postCreate = PostCreate.builder().title("").content("내용입니다.").build();
        String jsonData = objectMapper.writeValueAsString(postCreate);
        mockMvc.perform(MockMvcRequestBuilders.post("/dataValid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 작성")
    public void write() throws Exception {
        PostCreate postCreate = PostCreate.builder().title("제목입니다.").content("내용입니다.").build();
        String jsonData = objectMapper.writeValueAsString(postCreate);
        mockMvc.perform(MockMvcRequestBuilders.post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1건 조회")
    public void get() throws Exception {
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/post/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("foo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    public void getList() throws Exception {

        List<Post> requestPost = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("foo" + i)
                            .content("bar" + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPost);

        mockMvc.perform(MockMvcRequestBuilders.get("/post?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("foo30"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("bar30"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 수정")
    public void edit() throws Exception {

        Post post = Post.builder().title("foo").content("bar").build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("far").content("boo").build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/post/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 삭제")
    public void delete() throws Exception {

        Post post = Post.builder().title("foo").content("bar").build();

        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("예외 처리")
    public void error1() throws Exception {
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.get("/post/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("foo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    public void error2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    public void error3() throws Exception {
        PostEdit postEdit = PostEdit.builder().title("far").content("boo").build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글 작성 validation")
    public void error4() throws Exception {

        PostCreate postCreate = PostCreate.builder().title("<script>").content("bar").build();

        String json = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(MockMvcRequestBuilders.post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

    }

}