package com.jackdaw.springai.config;

import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromptTemplateConfig {

    @Bean
    public ConfigurablePromptTemplateFactory configurablePromptTemplateFactory() {
        return new ConfigurablePromptTemplateFactory();
        //也支持有参构造器
    }

}
