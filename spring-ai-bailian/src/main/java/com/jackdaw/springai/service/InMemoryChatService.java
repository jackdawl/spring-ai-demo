package com.jackdaw.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;


@Service
public class InMemoryChatService {
    private final ChatClient chatClient;

    //初始化基于内存的对话记忆
//    ChatMemory chatMemory = new InMemoryChatMemory();
    private final InMemoryChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();
    private final int MAX_MESSAGES = 100;
    private final MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
            .chatMemoryRepository(chatMemoryRepository)
            .maxMessages(MAX_MESSAGES)
            .build();

    public InMemoryChatService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("你是一个旅游规划师，根据用户的问题提供旅游规划建议")
                .defaultAdvisors( MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
                        .build())
                .build();

    }

    public String chat(String input, String conversantId) {


        String content = chatClient.prompt()
                .user(input)
                .advisors(  a -> a.param(CONVERSATION_ID, conversantId))
                .call()
                .content();


        return content;
    }


}
