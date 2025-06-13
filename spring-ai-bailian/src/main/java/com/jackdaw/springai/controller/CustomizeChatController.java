package com.jackdaw.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom")
public class CustomizeChatController {

    private final ChatClient chatClient;

    public CustomizeChatController(ChatClient.Builder  builder) {
        this.chatClient = builder.defaultSystem("你是一个友好的聊天机器人，回答问题时要使用 {voice} 的语气。")
                .build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input, @RequestParam(value = "voice") String voice) {
        return chatClient.prompt()
                .system(pus -> pus.param("voice", voice))
                .user(input)
                .call()
                .content();
    }


}
