package com.seal.vidar.controller;

import com.seal.vidar.entity.bo.Questions;
import com.seal.vidar.entity.dto.QuestionsDto;
import com.seal.vidar.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/questions")
    public ResponseEntity<QuestionsDto> getQuestions() {
        return apiService.getQuestions();
    }
}
