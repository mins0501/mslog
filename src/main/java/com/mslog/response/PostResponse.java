package com.mslog.response;

import com.mslog.domain.Post;
import lombok.Builder;
import lombok.Data;

@Data
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }


}
