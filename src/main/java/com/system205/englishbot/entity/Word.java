package com.system205.englishbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Word {

    @Id
    @GeneratedValue
    private Long id;
    private String value;
    private String translation;

    public Word(String value, String translation) {
        this.value = value;
        this.translation = translation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Word word = (Word) o;

        if (!value.equals(word.value)) return false;
        return translation.equals(word.translation);
    }

    @Override
    public String toString() {
        return "%s -> %s".formatted(value, translation);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + translation.hashCode();
        return result;
    }
}
