package com.example.knowme.controller;

import com.example.knowme.exception.ResourceNotFoundException;
import com.example.knowme.model.Deck;
import com.example.knowme.model.User;
import com.example.knowme.model.request.UserDeckRequest;
import com.example.knowme.model.request.UserRequest;
import com.example.knowme.model.response.MessageResponse;
import com.example.knowme.repository.DeckRepository;
import com.example.knowme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    @Autowired()
    private UserRepository userRepository;
    @Autowired()
    private DeckRepository deckRepository;
    @Autowired
    PasswordEncoder encoder;

    @GetMapping("users")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long userId, @Validated @RequestBody UserRequest userRequest) throws ResourceNotFoundException {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        Set<Long> longDecks = userRequest.getDecks();
        Set<Deck> decks = new HashSet<>();

        longDecks.forEach(deckLong -> {
            Deck newDeck = deckRepository.findById(deckLong)
                    .orElseThrow(() -> new RuntimeException("Error: Deck is not found."));
            decks.add(newDeck);
        });

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setDecks(decks);

        return ResponseEntity.ok(this.userRepository.save(user));
    }

    @GetMapping("users/{id}/decks")
    public ResponseEntity<Set<Deck>> getUserDecksById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user.getDecks());
    }

    @PutMapping("users/{id}/decks/set")
    public ResponseEntity<?> setUserDecks(@PathVariable(value = "id") Long userId, @Validated @RequestBody UserDeckRequest userDeckRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        Set<Deck> decks = new HashSet<>();

        userDeckRequest.getDecks().forEach(deckLong -> {
            Deck newDeck = deckRepository.findById(deckLong)
                    .orElseThrow(() -> new RuntimeException("Error: Deck is not found."));
            decks.add(newDeck);
        });

        user.setDecks(decks);

        return ResponseEntity.ok(this.userRepository.save(user));
    }

    @PutMapping("users/{id}/decks/add")
    public ResponseEntity<?> addUserDecks(@PathVariable(value = "id") Long userId, @Validated @RequestBody UserDeckRequest userDeckRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        Set<Deck> decks = new HashSet<>();

        userDeckRequest.getDecks().forEach(deckLong -> {
            Deck newDeck = deckRepository.findById(deckLong)
                    .orElseThrow(() -> new RuntimeException("Error: Deck is not found."));
            decks.add(newDeck);
        });
        decks.addAll(user.getDecks());

        user.setDecks(decks);

        return ResponseEntity.ok(this.userRepository.save(user));
    }
}
