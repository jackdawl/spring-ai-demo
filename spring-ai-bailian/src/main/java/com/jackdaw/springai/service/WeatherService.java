package com.jackdaw.springai.service;

import com.jackdaw.springai.tools.weather.method.WeatherToolImpl;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {


    private final ChatClient dashScopeChatClient;

    public WeatherService(ChatClient.Builder chatClientBuilder) {
        this.dashScopeChatClient = chatClientBuilder
                //也可全局设置
                //.defaultFunctions("weatherFunction")
                //.defaultTools(new WeatherToolImpl())
                .build();
    }

    /**
     * 无工具版
     */
    public String noTool(String query) {
        return dashScopeChatClient.prompt(query).call().content();
    }

    /**
     * 调用工具版 - function
     */
    public String queryByFunction(String query) {

//        return dashScopeChatClient.prompt(query).functions("weatherFunction").call().content();
        return null;
    }



    /**
     * 调用工具版 - method
     */
    public String queryByTool(String query) {

        return dashScopeChatClient.prompt(query).tools(new WeatherToolImpl()).call().content();
    }


}
