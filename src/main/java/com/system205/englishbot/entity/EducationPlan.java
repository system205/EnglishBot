package com.system205.englishbot.entity;

import com.system205.englishbot.utils.Utils;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public final class EducationPlan {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate lastUpdate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "plan_words",
        joinColumns = @JoinColumn(name = "plan_id"),
        inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private Collection<Word> dailyWords;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int numberOfWords;

    public static EducationPlan defaultPlan() {
        EducationPlan plan = new EducationPlan();
        plan.lastUpdate = LocalDate.ofEpochDay(0); // Never updated
        plan.dailyWords = Collections.emptyList();
        plan.numberOfWords = 3;
        return plan;
    }

    public Word suggestWord() {
        if (dailyWords.isEmpty()) {
            log.info("There is nothing to suggest. Daily words are empty. Need to initialize");
            return null;
        }

        return Utils.getRandomElement(dailyWords);
    }
}
