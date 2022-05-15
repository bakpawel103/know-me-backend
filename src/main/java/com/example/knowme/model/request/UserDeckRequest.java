package com.example.knowme.model.request;

import java.util.Set;

public class UserDeckRequest {
    private Set<Long> decks;

    public Set<Long> getDecks() {
        return decks;
    }

    public void setDecks(Set<Long> decks) {
        this.decks = decks;
    }
}
