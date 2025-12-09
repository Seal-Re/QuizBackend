package com.seal.vidar.repository;

import com.seal.vidar.entity.dao.QuestionDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface QuestionRepository extends MongoRepository<QuestionDao, String> {

}