package com.seal.vidar.controller;

import com.seal.vidar.entity.dto.QuestionsDto;
import com.seal.vidar.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/questions")
    public ResponseEntity<QuestionsDto> getQuestions() {
        return apiService.getQuestions();
    }
}
