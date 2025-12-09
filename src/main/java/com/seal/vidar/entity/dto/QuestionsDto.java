package com.seal.vidar.entity.dto;

import com.seal.vidar.entity.dao.QuestionDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsDto {
    private List<QuestionDao> questionList;
}