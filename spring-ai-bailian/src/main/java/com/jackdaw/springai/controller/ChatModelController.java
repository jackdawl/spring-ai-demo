package com.jackdaw.springai.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioTranscriptionOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;

@RestController
@RequestMapping("/model")
public class ChatModelController {
    private static final String AUDIO_RESOURCES_URL = "https://dashscope.oss-cn-beijing.aliyuncs.com/samples/audio/paraformer/hello_world_female2.wav";

    private final ChatModel chatModel;
    private final ImageModel imageModel;
    private final SpeechSynthesisModel speechSynthesisModel;
    private final DashScopeAudioTranscriptionModel dashScopeAudioTranscriptionModel;

    public ChatModelController(ChatModel chatModel,
                               ImageModel imageModel,
                               SpeechSynthesisModel speechSynthesisModel,
                               DashScopeAudioTranscriptionModel dashScopeAudioTranscriptionModel) {
        this.chatModel = chatModel;
        this.imageModel = imageModel;
        this.speechSynthesisModel = speechSynthesisModel;
        this.dashScopeAudioTranscriptionModel = dashScopeAudioTranscriptionModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input) {
        ChatResponse response = chatModel.call(new Prompt(input));
        return response.getResult().getOutput().getText();
    }

    @GetMapping("/streamChat")
    public Flux<String> streamChat(@RequestParam(value = "input") String input, HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        return chatModel.stream(input);
    }

    @GetMapping("/image")
    public String image(@RequestParam(value = "input") String input) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .model("wanx2.1-t2i-turbo")
                .height(1024)
                .width(1024)
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(input, options);
        ImageResponse response = imageModel.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();

        return "redirect:" + imageUrl;
    }

    @GetMapping("/text2Audio")
    public ResponseEntity<byte[]> text2Audio(@RequestParam(value = "input") String input) throws IOException {

        SpeechSynthesisPrompt prompt = new SpeechSynthesisPrompt(input);

        SpeechSynthesisResponse response = speechSynthesisModel.call(prompt);
        ByteBuffer audioData = response.getResult().getOutput().getAudio();

        // 将 ByteBuffer 转换为字节数组
        byte[] audioBytes = new byte[audioData.remaining()];
        audioData.get(audioBytes);

        // 返回音频流（MP3格式）
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=output.mp3")
                .body(audioBytes);
    }
    @GetMapping("/audio2Text")
    public String audio2Text() throws MalformedURLException {
        Resource resource =new UrlResource(AUDIO_RESOURCES_URL);

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(resource,
                DashScopeAudioTranscriptionOptions.builder()
                        .withModel("sensevoice-v1")
                        .build());

        return dashScopeAudioTranscriptionModel.call(prompt).getResult().getOutput();
    }



}
