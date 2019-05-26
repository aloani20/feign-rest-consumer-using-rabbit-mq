package com.abdul.feignrestconsumer.service;

import com.abdul.feignrestconsumer.model.Post;

import java.util.List;

public interface JSONPlaceHolderService {

    List<Post> getPosts();

    Post getPostById(Long id);
}