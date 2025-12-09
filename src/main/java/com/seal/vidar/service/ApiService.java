package com.seal.vidar.service;

import com.seal.vidar.entity.bo.Questions;
import com.seal.vidar.entity.dto.QuestionsDto;
import org.springframework.http.ResponseEntity;

public interface ApiService {

    ResponseEntity<QuestionsDto> getQuestions();
}
