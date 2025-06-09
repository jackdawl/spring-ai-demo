package com.jackdaw.springai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiliconflowController {



    private final ChatModel chatModel;

    public SiliconflowController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/siliconflow/chat")
    public String ollamaChat(@RequestParam(value = "input") String input) {

        return chatModel.call(new Prompt(input)).getResult().getOutput().getText();
    }


}
