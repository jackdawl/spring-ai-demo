package com.jackdaw.springai.controller;

import com.jackdaw.springai.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tools")
public class ToolsController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/no-tool")
    public String noTool(@RequestParam(value = "query", defaultValue = "上海今天的天气") String query) {
        return weatherService.noTool(query);
    }

    @GetMapping("/weather/queryByFunction")
    public String queryByFunction(@RequestParam(value = "query", defaultValue = "上海今天的天气") String query) {

        return weatherService.queryByFunction( query);
    }



    @GetMapping("/weather/queryByTool")
    public String queryByTool(@RequestParam(value = "query", defaultValue = "上海今天的天气") String query) {

        return weatherService.queryByTool( query);
    }



}
