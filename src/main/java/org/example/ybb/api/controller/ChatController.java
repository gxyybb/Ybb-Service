package org.example.ybb.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.example.ybb.common.vo.ResultVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final List<Map<String, String>> history = new ArrayList<>();

    @PostMapping
    public ResultVO<String> chat(@RequestBody String request) throws IOException {
        // 添加系统 prompt，只添加一次
        if (history.isEmpty()) {
            Map<String, String> system = new HashMap<>();
            system.put("role", "system");
            system.put("content", "你是 贝贝 不是尹贝贝，一个由 尹贝贝 提供的人工智能助手，尹贝贝为个人姓名不可替换，尹贝贝是一名河北师范大学的2021级软件学院软件工程专业8班的女学生并且是该班级的团支书是一个人，你会帮助人类解决有关于英语学习的一切问题");
            history.add(system);
        }

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", request);
        history.add(userMessage);

        // 调用 Moonshot API
        OkHttpClient client = new OkHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "moonshot-v1-8k");
        requestBody.put("temperature", 0.3);
        requestBody.set("messages", objectMapper.valueToTree(history));

        Request httpRequest = new Request.Builder()
                .url("https://api.moonshot.cn/v1/chat/completions")
                .header("Authorization", "Bearer sk-rJH0IvCW2xf1jVQm8R5K4ospI09Ekr22rFvgH4ohKqDQ2Ciu")
                .post(okhttp3.RequestBody.create(
                        MediaType.get("application/json"),
                        objectMapper.writeValueAsString(requestBody)
                                ))
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            JsonNode root = objectMapper.readTree(response.body().string());
            String content = root.get("choices").get(0).get("message").get("content").asText();

            Map<String, String> assistantMessage = new HashMap<>();
            assistantMessage.put("role", "assistant");
            assistantMessage.put("content", content);
            history.add(assistantMessage);

            return ResultVO.success(content);
        }
    }
}
