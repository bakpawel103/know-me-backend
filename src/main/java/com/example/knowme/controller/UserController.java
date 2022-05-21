package com.example.knowme.controller;

import com.example.knowme.exception.ResourceNotFoundException;
import com.example.knowme.model.Deck;
import com.example.knowme.model.User;
import com.example.knowme.model.request.UserRequest;
import com.example.knowme.model.response.MessageResponse;
import com.example.knowme.repository.DeckRepository;
import com.example.knowme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(encoder.encode(userRequest.getPassword()));

        return ResponseEntity.ok(this.userRepository.save(user));
    }

    @GetMapping("users/{id}/decks")
    public ResponseEntity<Set<Deck>> getUserDecksById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user.getDecks());
    }

    @PutMapping("users/{id}/decks")
    public ResponseEntity<?> setUserDecks(@PathVariable(value = "id") Long userId, @RequestBody String deckSecretId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        Deck newDeck = deckRepository.findBySecretId(deckSecretId)
                .orElseThrow(() -> new RuntimeException("Error: Deck is not found for this secret " + deckSecretId));

        Set<Deck> decks = user.getDecks();
        decks.add(newDeck);

        user.setDecks(decks);

        return ResponseEntity.ok(this.userRepository.save(user));
    }

    @DeleteMapping("users/{id}/decks")
    public ResponseEntity<?> deleteDeck(@PathVariable(value = "id") Long userId, @RequestBody String deckSecretId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        Deck foundDeck = user.getDecks().stream().filter(deck -> deck.getSecretId().equals(deckSecretId)).findFirst().get();
        user.getDecks().remove(foundDeck);

        return ResponseEntity.ok(this.userRepository.save(user));
    }
}
