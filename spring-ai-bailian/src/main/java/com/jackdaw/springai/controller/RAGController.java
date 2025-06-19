package com.jackdaw.springai.controller;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
public class RAGController {

    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    private final ChatClient ragChatClient;

    private final VectorStore vectorStore;

    private final DashScopeAgent agent;

    public RAGController(ChatClient ragChatClient,
                         VectorStore vectorStore,
                         DashScopeAgentApi dashscopeAgentApi
    ) {
        this.ragChatClient = ragChatClient;
        this.vectorStore = vectorStore;
        this.agent = new DashScopeAgent(dashscopeAgentApi);
    }


    @GetMapping(value = "/chat-config", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chatConfig(@RequestParam(value = "input") String input) {

        return ragChatClient.prompt()
                .user(input)
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();
    }

    @GetMapping("/bailian-agent/chat")
    public String call(@RequestParam(value = "input") String input) {
        return agent.call(
                new Prompt(input, DashScopeAgentOptions.builder()
                        .withAppId(appId)
                        .build()))
                .getResult()
                .getOutput()
                .getText();

    }


}
