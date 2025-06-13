package com.jackdaw.springai.controller;

import com.jackdaw.springai.service.RedisMemoryChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
public class MemoryChatController {

//    @Autowired
//    private InMemoryChatService inMemoryChatService;

    @Autowired
    private RedisMemoryChatService redisMemoryChatService;

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input, @RequestParam(value = "conversantId") String conversantId) {

        return redisMemoryChatService.chat(input, conversantId);
    }


}
