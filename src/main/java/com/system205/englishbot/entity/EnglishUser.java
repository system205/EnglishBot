package com.system205.englishbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "english_users", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class EnglishUser {

    @Id
    private Long id;

    @OneToMany(mappedBy = "user")
    private Set<Word> words;

    @Column(nullable = false)
    private Instant lastNotified;

    @Column(nullable = false)
    private Duration interval;

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    private EducationPlan educationPlan;
}
