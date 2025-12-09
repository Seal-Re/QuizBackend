package com.seal.vidar.entity.bo;

import com.seal.vidar.entity.dao.QuestionDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Questions {

    private List<QuestionDao> questionDaos;

}
