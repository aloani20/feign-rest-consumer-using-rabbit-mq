package com.abdul.feignrestconsumer.hystrix;

import com.abdul.feignrestconsumer.client.JSONPlaceHolderClient;
import com.abdul.feignrestconsumer.model.Post;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JSONPlaceHolderFallback implements JSONPlaceHolderClient {

    @Override
    public List<Post> getPosts() {
        return Collections.emptyList();
    }

    @Override
    public Post getPostById(Long postId) {
        return new Post("abdul",100L,"manager","what a body buddy!!");
    }
}