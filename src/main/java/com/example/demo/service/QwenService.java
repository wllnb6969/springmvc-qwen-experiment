package com.example.demo.service;

import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

@Service
public class QwenService {

    private static final String API_KEY = "sk-73958587b94e46a8a71c1dee0dba163d"; // 替换！

    // ① 对话
    public String chat(String prompt) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("model", "qwen-plus");
            JSONObject input = new JSONObject();
            input.put("prompt", prompt);
            payload.put("input", input);
            HttpPost post = new HttpPost("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation");
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(payload.toJSONString()));
            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse resp = client.execute(post)) {
                String result = EntityUtils.toString(resp.getEntity());
                System.out.println("阿里云原始返回：" + result);
                JSONObject json = JSONObject.parseObject(result);
                if (json.getJSONObject("output") != null) {
                    return json.getJSONObject("output").getString("text");
                } else {
                    return "调用失败，阿里云返回：" + result;
                }
            }
        } catch (Exception e) {
            return "对话异常：" + e.getMessage();
        }
    }

    // ② 文生图
    public String generateImage(String prompt) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("model", "wanx-image-v1");
            JSONObject input = new JSONObject();
            input.put("prompt", prompt);
            payload.put("input", input);

            HttpPost post = new HttpPost("https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis");
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(payload.toJSONString()));

            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse resp = client.execute(post)) {
                String result = EntityUtils.toString(resp.getEntity());
                JSONObject json = JSONObject.parseObject(result);
                return json.getJSONObject("output").getString("image_url");
            }
        } catch (Exception e) {
            return "文生图失败：" + e.getMessage();
        }
    }

    // ④ 图像识别（qwen-vl）
    public String analyzeImage(String imageUrl) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("model", "qwen-vl-plus");
            JSONObject input = new JSONObject();
            input.put("prompt", "描述这张图片");
            input.put("image", imageUrl);
            payload.put("input", input);

            HttpPost post = new HttpPost("https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation");
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(payload.toJSONString()));

            try (CloseableHttpClient client = HttpClients.createDefault();
                 CloseableHttpResponse resp = client.execute(post)) {
                String result = EntityUtils.toString(resp.getEntity());
                JSONObject json = JSONObject.parseObject(result);
                return json.getJSONObject("output").getString("text");
            }
        } catch (Exception e) {
            return "图像识别失败：" + e.getMessage();
        }
    }
}