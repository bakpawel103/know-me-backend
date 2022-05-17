package com.example.knowme.repository;

import com.example.knowme.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    Optional<Deck> findBySecretId(String secretId);
}
