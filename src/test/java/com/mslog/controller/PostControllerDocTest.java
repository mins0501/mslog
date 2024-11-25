package com.mslog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mslog.config.MslogMockUser;
import com.mslog.domain.Member;
import com.mslog.domain.Post;
import com.mslog.exception.PostNotFound;
import com.mslog.repository.MemberRepository;
import com.mslog.repository.PostRepository;
import com.mslog.request.PostCreate;
import com.mslog.request.PostEdit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.msKym.com", uriPort = 443)
public class PostControllerDocTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void clean() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }


    @Test
    @MslogMockUser
    @DisplayName("게시글 작성")
    public void write() throws Exception {

        PostCreate postCreate = PostCreate.builder()
                .title("foo")
                .content("bar")
                .build();
        String jsonData = objectMapper.writeValueAsString(postCreate);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("write",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("title").description("제목"),
                                PayloadDocumentation.fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 목록 조회")
    public void getList() throws Exception {

        List<Post> requestPost = IntStream.range(0, 30)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("foo" + i)
                            .content("bar" + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPost);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/post?page={page}&size={size}", 1L, 5L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("getList",
                        PayloadDocumentation.responseFields(
                                List.of(
                                        PayloadDocumentation.fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("전체 조회 수"),
                                        PayloadDocumentation.fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지"),
                                        PayloadDocumentation.fieldWithPath("size").type(JsonFieldType.NUMBER).description("사이즈"),
                                        PayloadDocumentation.fieldWithPath("items").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        PayloadDocumentation.fieldWithPath("items[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        PayloadDocumentation.fieldWithPath("items[].title").type(JsonFieldType.STRING).description("제목"),
                                        PayloadDocumentation.fieldWithPath("items[].content").type(JsonFieldType.STRING).description("내용"),
                                        PayloadDocumentation.fieldWithPath("items[].regDate").type(JsonFieldType.STRING).description("작성일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("게시글 1건 조회")
    public void get() throws Exception {

        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("get",
                        RequestDocumentation.pathParameters(
                            RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                            PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                            PayloadDocumentation.fieldWithPath("title").description("제목"),
                            PayloadDocumentation.fieldWithPath("content").description("내용"),
                            PayloadDocumentation.fieldWithPath("regDate").description("작성일자")
                        )
                ));
    }

    @Test
    @MslogMockUser
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
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("edit",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("title").description("제목"),
                                PayloadDocumentation.fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @MslogMockUser()
    @DisplayName("글 삭제")
    public void delete() throws Exception {

        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/post/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("delete",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        )
                ));
    }



}
