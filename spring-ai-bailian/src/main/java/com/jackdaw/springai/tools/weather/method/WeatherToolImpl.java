package com.jackdaw.springai.tools.weather.method;


import org.springframework.ai.tool.annotation.Tool;

public class WeatherToolImpl implements WeatherTool {
    @Override
    @Tool(description = "获取指定城市的天气信息。")
    public String getWeather(String city) {
        // todo 调用具体天气API
        return "The weather in " + city + " is sunny.";
    }
}
