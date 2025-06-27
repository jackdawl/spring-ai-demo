package com.jackdaw.mcp.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
public class MCPController {

    private final ChatClient dashScopeChatClient;

    public MCPController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools) {
        this.dashScopeChatClient = chatClientBuilder

                .defaultToolCallbacks(tools)
                .build();
    }


    @GetMapping("/weather/sse")
    public String weather(@RequestParam(value = "query", defaultValue = "上海今天的天气") String query) {

        System.out.println("\n>>> QUESTION: " + query);
        String content = dashScopeChatClient.prompt(query).call().content();

        System.out.println("\n>>> ASSISTANT: " + content);
        return content;
    }


}
