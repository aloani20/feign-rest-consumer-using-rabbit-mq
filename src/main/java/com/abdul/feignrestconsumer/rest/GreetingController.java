package com.abdul.feignrestconsumer.rest;

import com.abdul.feignrestconsumer.client.GreetingClient;
import com.abdul.feignrestconsumer.client.JSONPlaceHolderClient;
import com.abdul.feignrestconsumer.model.Post;
import com.abdul.feignrestconsumer.rabbitmq.Producer;
import com.abdul.feignrestconsumer.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GreetingController {

    @Autowired
    JSONPlaceHolderClient jsonPlaceHolderClient;

    @Autowired
    private GreetingClient greetingClient;

    @Autowired
     private Producer producer;

    @Autowired
    GreetingService greetingService;

    @RequestMapping("/get-greeting/{username}")
    public ResponseEntity<String> getGreeting(@PathVariable("username") String username) {
        String message;
        try{
            message = greetingClient.greeting(username);
        }catch (Exception e ){
            message = "error from remote server";
            System.out.println("error caught "+ e.getMessage());
        }
        return  new ResponseEntity<>("Hi "+ message, HttpStatus.OK);
    }

    @RequestMapping("/get-posts")
    public ResponseEntity<List<Post>> getPosts(){
          return new ResponseEntity<>(jsonPlaceHolderClient.getPosts(),HttpStatus.OK);
    }

    @RequestMapping("/get/{postId}")
    public ResponseEntity<Post> getPostById( @PathVariable("postId") Long postId){
        return new ResponseEntity<>(jsonPlaceHolderClient.getPostById(postId),HttpStatus.OK);
    }

    @RequestMapping("/send/{msg}")
    public ResponseEntity<String> sendMsg(@PathVariable("msg")String msg){
        producer.produceMsg(msg);
        return  new ResponseEntity<>("Done",HttpStatus.OK);
    }

    @RequestMapping("/greeting/{name}")
    public ResponseEntity<String> sendGreeting(@PathVariable("name")String name){
        greetingService.sendGreeting(name);
        return  new ResponseEntity<>("Done",HttpStatus.OK);
    }

}
