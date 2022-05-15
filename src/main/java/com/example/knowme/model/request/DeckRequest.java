package com.example.knowme.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class DeckRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    private Set<Long> questions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Long> questions) {
        this.questions = questions;
    }
}
