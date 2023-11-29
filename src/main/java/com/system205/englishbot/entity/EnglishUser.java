package com.system205.englishbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EnglishUser {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private Set<Word> words;

    public EnglishUser() {
        // Empty constructor for builder
    }
}
