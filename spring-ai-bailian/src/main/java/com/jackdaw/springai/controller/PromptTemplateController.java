package com.jackdaw.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promptTemplate")
public class PromptTemplateController {

    @Value("classpath:/prompts/joke-prompt.st")
    private Resource jokeResource;

    @Value("classpath:/prompts/system-prompt.st")
    private Resource systemResource;

    //静态RAG
    @Value("classpath:/docs/parrot.md")
    private Resource docsToStuffResource;

    @Value("classpath:/prompts/qa-prompt.st")
    private Resource qaPromptResource;

    private final ChatClient chatClient;

//    private final ConfigurablePromptTemplateFactory configurablePromptTemplateFactory;


    public PromptTemplateController(
            ChatClient.Builder builder
    ) {

        this.chatClient = builder.build();
    }


    @GetMapping("/config")
    public AssistantMessage configPromptTemplate(
            @RequestParam(value = "actor", defaultValue = "周星驰") String actor
    ) {

        PromptTemplate template = new PromptTemplate("请列出 {actor} 参演的最著名的三部影视作品。");

        Prompt prompt;
        if (StringUtils.hasText(actor)) {
            prompt = template.create(Map.of("actor", actor));
        } else {
            prompt = template.create();
        }

        return chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
    }

    @GetMapping("/dynamic")
    public AssistantMessage dynamicPromptTemplate(
            @RequestParam(value = "topic", defaultValue = "鹦鹉") String topic,
            @RequestParam(value = "adjective", defaultValue = "搞笑的") String adjective
    ) {

        PromptTemplate promptTemplate = new PromptTemplate(jokeResource);
        //动态提示词模板
        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "adjective", adjective));

        return chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
    }


    @GetMapping("/system")
    public AssistantMessage systemPromptTemplate(
            @RequestParam(value = "message",
                    defaultValue = "请介绍一下三位最著名的香港喜剧演员，以及他们的代表作品，为每个演员写一句总结。") String message,
            @RequestParam(value = "name", defaultValue = "沈腾") String name,
            @RequestParam(value = "voice", defaultValue = "喜剧") String voice
    ) {

        UserMessage userMessage = new UserMessage(message);

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        return chatClient.prompt(new Prompt(List.of(userMessage, systemMessage)))
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
    }


    @GetMapping(value = "/rag")
    public AssistantMessage ragPromptTemplate(@RequestParam(value = "message", defaultValue = "请给我推荐一款Parrot系列的手机") String message,
                                 @RequestParam(value = "ragFlag", defaultValue = "false") boolean ragFlag
    ) {

        PromptTemplate promptTemplate = new PromptTemplate(qaPromptResource);

        Map<String, Object> map = new HashMap<>();
        map.put("question", message);
        if (ragFlag) {
            map.put("context", docsToStuffResource);
        } else {
            map.put("context", "");
        }
        Prompt prompt = promptTemplate.create(map);

        return chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
    }



}
