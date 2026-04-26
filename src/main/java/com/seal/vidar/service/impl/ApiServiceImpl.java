package com.seal.vidar.service.impl;

import com.seal.vidar.entity.dao.QuestionDao;
import com.seal.vidar.entity.dto.QuestionsDto;
import com.seal.vidar.repository.QuestionRepository;
import com.seal.vidar.service.ApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    private final QuestionRepository questionRepository;

    public ApiServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public ResponseEntity<QuestionsDto> getQuestions() {
        List<QuestionDao> questionDaos = questionRepository.findAll();
        QuestionsDto questions = new QuestionsDto();
        questions.setQuestionList(questionDaos);
        return ResponseEntity.ok(questions);
    }
}
