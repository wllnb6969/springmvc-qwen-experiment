package com.example.demo.controller;

import com.example.demo.service.QwenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QwenController {

    @Autowired
    private QwenService qwenService;

    // ① 对话
    @GetMapping("/")
    public String chatPage() {
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String prompt, Model model) {
        String response = qwenService.chat(prompt);
        model.addAttribute("prompt", prompt);
        model.addAttribute("response", response);
        return "chat";
    }

    // ② 文生图
    @GetMapping("/image")
    public String imagePage() {
        return "image";
    }

    @PostMapping("/image")
    public String generateImage(@RequestParam String prompt, Model model) {
        String imageUrl = qwenService.generateImage(prompt);
        model.addAttribute("prompt", prompt);
        model.addAttribute("imageUrl", imageUrl);
        return "image";
    }

    // ④ 图像识别
    @GetMapping("/vl")
    public String vlPage() {
        return "vl";
    }

    @PostMapping("/vl")
    public String analyzeImage(@RequestParam String imageUrl, Model model) {
        String description = qwenService.analyzeImage(imageUrl);
        model.addAttribute("imageUrl", imageUrl);
        model.addAttribute("description", description);
        return "vl";
    }
}