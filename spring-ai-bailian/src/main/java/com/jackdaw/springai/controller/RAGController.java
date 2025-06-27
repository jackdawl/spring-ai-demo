package com.jackdaw.springai.controller;

import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import com.jackdaw.springai.service.CloudRagService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/rag")
public class RAGController {

    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    private final ChatClient ragChatClient;

    private final VectorStore vectorStore;

    private final DashScopeAgent agent;

    private final CloudRagService cloudRagService;

    public RAGController(ChatClient ragChatClient,
                         VectorStore vectorStore,
                         DashScopeAgentApi dashscopeAgentApi,
                         CloudRagService cloudRagService
    ) {
        this.ragChatClient = ragChatClient;
        this.vectorStore = vectorStore;
        this.agent = new DashScopeAgent(dashscopeAgentApi);
        this.cloudRagService = cloudRagService;
    }


    @GetMapping(value = "/chat-config", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chatConfig(@RequestParam(value = "input") String input) {
        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build();
        return ragChatClient.prompt()
                .user(input)
                .advisors(new DocumentRetrievalAdvisor(vectorStoreDocumentRetriever))
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

   //todo: 由于百炼平台知识库上传文件都显示解析失败，后面在测试
    @GetMapping("/bailian/importDocuments")
    public void importDocuments() {
        cloudRagService.importDocuments();

    }
    @GetMapping("/bailian/retrieval")
    public Flux<String> generate(@RequestParam(value = "input",
            defaultValue = "请问你的知识库文档主要是关于什么内容的?") String input) {
        return cloudRagService.retrieve(input).map(x -> x.getResult().getOutput().getText());
    }


}
