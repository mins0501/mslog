package com.mslog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mslog.domain.Post;
import com.mslog.repository.PostRepository;
import com.mslog.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.kimzart.com", uriPort = 443)
public class PostControllerDocTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 1건 조회")
    public void get() throws Exception {
        Post post = Post.builder().title("foo").content("bar").build();
        postRepository.save(post);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/post/{postId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
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
                            PayloadDocumentation.fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @WithMockUser(username = "mins0501@gmail.com", roles = {"ADMIN"}, password = "1234")
    @DisplayName("글 작성")
    public void write() throws Exception {
        PostCreate postCreate = PostCreate.builder().title("foo").content("bar").build();
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
    

}
