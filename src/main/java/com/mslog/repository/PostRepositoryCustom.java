package com.mslog.repository;

import com.mslog.domain.Post;
import com.mslog.request.PostSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> getList(PostSearch postSearch);

}
