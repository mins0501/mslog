package com.mslog.repository;

import com.mslog.domain.Post;
import com.mslog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
