package com.example.knowme.controller;

import com.example.knowme.exception.ResourceNotFoundException;
import com.example.knowme.model.Deck;
import com.example.knowme.model.Question;
import com.example.knowme.model.request.DeckRequest;
import com.example.knowme.repository.DeckRepository;
import com.example.knowme.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/")
public class DeckController {
    @Autowired()
    private DeckRepository deckRepository;
    @Autowired()
    private QuestionRepository questionRepository;


    @GetMapping("decks/{id}")
    public ResponseEntity<Deck> getDeckById(@PathVariable(value = "id") Long deckId) throws ResourceNotFoundException {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found for this id :: " + deckId));
        return ResponseEntity.ok().body(deck);
    }

    @PostMapping("decks")
    public Deck createDeck(@RequestBody DeckRequest deckRequest) {
        Deck deck = new Deck(deckRequest.getName());

        Set<Long> longQuestions = deckRequest.getQuestions();
        Set<Question> questions = new HashSet<>();

        longQuestions.forEach(questionLong -> {
            Question newQuestion = questionRepository.findById(questionLong)
                    .orElseThrow(() -> new RuntimeException("Error: Question is not found."));
            questions.add(newQuestion);
        });

        deck.setQuestions(questions);

        return this.deckRepository.save(deck);
    }

    @PutMapping("decks/{id}")
    public ResponseEntity<Deck> updateDeck(@PathVariable(value = "id") Long deckId, @Validated @RequestBody DeckRequest deckRequest) throws ResourceNotFoundException {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found for this id :: " + deckId));
        deck.setName(deckRequest.getName());

        Set<Long> longQuestions = deckRequest.getQuestions();
        Set<Question> questions = new HashSet<>();

        longQuestions.forEach(questionLong -> {
            Question newQuestion = questionRepository.findById(questionLong)
                    .orElseThrow(() -> new RuntimeException("Error: Question is not found."));
            questions.add(newQuestion);
        });

        deck.setQuestions(questions);

        return ResponseEntity.ok(this.deckRepository.save(deck));
    }

    @DeleteMapping("decks/{id}")
    public Map<String, Boolean> deleteDeck(@PathVariable(value = "id") Long deckId) throws ResourceNotFoundException {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found for this id :: " + deckId));

        deckRepository.delete(deck);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }
}
