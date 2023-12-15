package com.system205.englishbot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Duration;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_settings")
public class NotificationSettings {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Duration interval;

    @ColumnDefault("06:00:00")
    private LocalTime startTime;

    @ColumnDefault("21:00:00")
    private LocalTime endTime;


    public static NotificationSettings defaultSettings() {
        return NotificationSettings.builder()
            .interval(Duration.ofMinutes(10)) // 10 Minutes by default
            .startTime(LocalTime.of(6,0,0))
            .endTime(LocalTime.of(18,0,0))
            .build();
    }
}
