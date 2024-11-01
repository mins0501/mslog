package com.mslog.request;

import com.mslog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력하세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("<script>")) {
            throw new InvalidRequest("title", "<script>를 포함할 수 없습니다.");
        } else if (content.contains("<script>")) {
            throw new InvalidRequest("content", "<script>를 포함할 수 없습니다.");
        } else {}
    }

}

