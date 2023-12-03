package com.system205.englishbot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "english_users", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class EnglishUser {

    @Id
    @NotNull(message = "TelegramId has to be provided")
    private Long id;

    @OneToMany(mappedBy = "user")
    private Set<Word> words;

    public EnglishUser() {
        // Empty constructor for builder
    }
}
