package com.shevendra.dsatracker.repository;

import com.shevendra.dsatracker.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByIdAsc();
    boolean existsByTopicAndTitle(String topic, String title);
}
