package com.shevendra.dsatracker.controller;

import com.shevendra.dsatracker.model.Question;
import com.shevendra.dsatracker.repository.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionRepository repository;
    public QuestionController(QuestionRepository repository) { this.repository = repository; }

    @GetMapping("/questions")
    public List<Question> getQuestions() { return repository.findAllByOrderByIdAsc(); }

    @PatchMapping("/questions/{id}/status")
    public Question updateStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        Question question = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        question.setSolved(request.solved());
        return repository.save(question);
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        List<Question> all = repository.findAll();
        long solved = all.stream().filter(Question::isSolved).count();
        Map<String, Map<String, Long>> topics = all.stream().collect(Collectors.groupingBy(Question::getTopic, LinkedHashMap::new,
            Collectors.collectingAndThen(Collectors.toList(), list -> {
                Map<String, Long> counts = new LinkedHashMap<>();
                counts.put("total", (long) list.size());
                counts.put("solved", list.stream().filter(Question::isSolved).count());
                return counts;
            })));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", all.size()); result.put("solved", solved); result.put("remaining", all.size() - solved);
        result.put("percentage", all.isEmpty() ? 0 : Math.round(solved * 1000.0 / all.size()) / 10.0);
        result.put("topics", topics);
        return result;
    }

    public record StatusRequest(boolean solved) {}
}
