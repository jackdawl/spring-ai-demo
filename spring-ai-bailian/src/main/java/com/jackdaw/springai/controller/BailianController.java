package com.jackdaw.springai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BailianController {


    private final ChatModel dashScopeChatModel;

    public BailianController(ChatModel chatModel) {
        this.dashScopeChatModel = chatModel;
    }

    @GetMapping("/bailian/chat")
    public String ollamaChat(@RequestParam(value = "input") String input) {

        return dashScopeChatModel.call(new Prompt(input)).getResult().getOutput().getText();
    }

}
