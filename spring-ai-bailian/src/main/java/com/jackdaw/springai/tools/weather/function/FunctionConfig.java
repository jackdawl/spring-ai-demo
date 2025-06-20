package com.jackdaw.springai.tools.weather.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionConfig {
    @Bean
    @Description("获取指定城市的天气信息")//这是一个重要的属性，可帮助 AI 模型确定要调用哪个客户端函数。
    public Function<WeatherFunction.WeatherRequest, String> weatherFunction() {
        return new WeatherFunction();
    }
}
