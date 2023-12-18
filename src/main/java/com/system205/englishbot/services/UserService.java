package com.system205.englishbot.services;

import com.system205.englishbot.entity.EducationPlan;
import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.entity.NotificationSettings;
import com.system205.englishbot.entity.Word;
import com.system205.englishbot.repositories.EnglishUserRepository;
import com.system205.englishbot.repositories.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final EnglishUserRepository userRepository;
    private final WordRepository wordRepository;

    public EnglishUser getUser(Long id) {
        final Optional<EnglishUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            EnglishUser user = createDefaultUser(id);
            log.info("New user is registered: {}", user);
            return userRepository.save(user);
        } else return optionalUser.get();
    }

    public void addWordsToUser(Long id, Iterable<Word> list) {
        final EnglishUser user = getUser(id);
        for (Word word : list) {
            word.setUser(user);
        }
        wordRepository.saveAll(list);
    }

    public Set<Word> getWordsByUserId(Long id) {
        return wordRepository.findByUser_Id(id);
    }

    private EnglishUser createDefaultUser(Long id) {
        return EnglishUser.builder()
            .id(id)
            .lastNotified(Instant.now())
            .notificationSettings(NotificationSettings.defaultSettings())
            .educationPlan(EducationPlan.defaultPlan())
            .build();
    }

    public long getNumberOfWords(Long id) {
        return wordRepository.countByUser_Id(id);
    }

    public void updateUsers(Iterable<EnglishUser> users) {
        userRepository.saveAll(users);
    }

    public List<EnglishUser> getAllUsers() {
        return userRepository.findAll();
    }

}
