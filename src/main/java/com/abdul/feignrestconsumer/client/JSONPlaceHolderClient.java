package com.abdul.feignrestconsumer.client;

import com.abdul.feignrestconsumer.config.JSONPlaceHolderClientConfiguration;
import com.abdul.feignrestconsumer.hystrix.JSONPlaceHolderFallback;
import com.abdul.feignrestconsumer.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "jplaceholder",
        url = "https://jsonplaceholder.typicode.com/",
        configuration = JSONPlaceHolderClientConfiguration.class,
        fallback = JSONPlaceHolderFallback.class)
public interface JSONPlaceHolderClient {

    @RequestMapping(method = RequestMethod.GET, value = "/posts")
    List<Post> getPosts();


    @RequestMapping(method = RequestMethod.GET, value = "/postss/{postId}", produces = "application/json")
    Post getPostById(@PathVariable("postId") Long postId);
}