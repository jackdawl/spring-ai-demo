package com.jackdaw.springai.tools.weather.function;

import java.util.function.Function;

public class WeatherFunction implements Function<WeatherFunction.WeatherRequest, String> {
    @Override
    public String apply(WeatherRequest request) {
        // todo 调用具体天气API
        return "The weather in " + request.getCity() + " is sunny.";
    }
    public static class WeatherRequest {
        private String city;
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }
}
