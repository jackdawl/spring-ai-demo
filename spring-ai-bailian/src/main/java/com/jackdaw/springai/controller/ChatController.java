package com.jackdaw.springai.controller;

import com.jackdaw.springai.entity.ActorFilms;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/ali-ai")
public class ChatController {

    private final ChatClient  chatClient;

    public ChatController(ChatClient.Builder  builder) {
        this.chatClient = builder.build();
    }


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input) {
        return chatClient.prompt()
                .user(input)
                .call()
                .content();
    }

    /**
     * 注意一定要设置 produces = MediaType.TEXT_EVENT_STREAM_VALUE
     * @param input
     * @return
     */
    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestParam(value = "input") String input) {
        return chatClient.prompt()
                .user(input)
                .stream()
                .content();
    }

    @GetMapping("/actorFilms")
    public ActorFilms films(@RequestParam(value = "input") String input) {
        return chatClient.prompt()
                .user(input)
                .call()
                .entity(ActorFilms.class);
    }


    @GetMapping("/actorFilmsList")
    public List<ActorFilms> films2(@RequestParam(value = "input") String input) {
        return chatClient.prompt()
                .user(input)
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilms>>() {
                });
    }



}
