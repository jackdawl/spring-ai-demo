package com.jackdaw.springai.service;

import com.jackdaw.springai.memory.RedisChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class RedisMemoryChatService {
    private final ChatClient chatClient;

    //初始化基于内存的对话记忆
    ChatMemory chatMemory = new InMemoryChatMemory();

    public RedisMemoryChatService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("你是一个旅游规划师，根据用户的问题提供旅游规划建议")
                .defaultAdvisors(new MessageChatMemoryAdvisor(new RedisChatMemory()))
                .build();

    }

    public String chat(String input, String conversantId) {


        String content = chatClient.prompt()
                .user(input)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, conversantId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .content();


        return content;
    }

}
