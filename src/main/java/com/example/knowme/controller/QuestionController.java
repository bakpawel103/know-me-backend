package com.example.knowme.controller;

import com.example.knowme.exception.ResourceNotFoundException;
import com.example.knowme.model.Question;
import com.example.knowme.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/")
public class QuestionController {
    @Autowired()
    private QuestionRepository questionRepository;

    @GetMapping("questions")
    public List<Question> getAllQuestions() {
        return this.questionRepository.findAll();
    }

    @GetMapping("questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable(value = "id") Long questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found for this id :: " + questionId));
        return ResponseEntity.ok().body(question);
    }

    @PostMapping("questions")
    public Question createQuestion(@RequestBody Question question) {
        return this.questionRepository.save(question);
    }

    // update question
    @PutMapping("questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable(value = "id") Long questionId, @Validated @RequestBody Question questionDetails) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found for this id :: " + questionId));
        question.setName(questionDetails.getName());
        question.setDescription(questionDetails.getDescription());
        question.setAnswered(questionDetails.getAnswered());

        return ResponseEntity.ok(this.questionRepository.save(question));
    }

    @DeleteMapping("questions/{id}")
    public Map<String, Boolean> deleteQuestion(@PathVariable(value = "id") Long questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found for this id :: " + questionId));

        this.questionRepository.delete(question);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
