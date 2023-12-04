package com.system205.englishbot.services;

import com.system205.englishbot.dto.Notification;
import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.entity.Word;
import com.system205.englishbot.repositories.EnglishUserRepository;
import com.system205.englishbot.repositories.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EnglishUserRepository userRepository;
    private final WordRepository wordRepository;
    private int defaultNotificationInterval;

    public EnglishUser getUser(Long id) {
        final Optional<EnglishUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            EnglishUser user = createDefaultUser(id);
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

    /**
     * Check each user from repository and compose a list of notifications <br>
     * <br>
     * the difference between lastNotified time and current time instant is compared with the user interval<br>
     * when the difference is positive a user is chosen for notification <br>
     * their lastNotified time is set to the current one
     *
     * @return List of notifications
     * */
    public List<Notification> getScheduledNotifications() {
        final List<Notification> notifications = new ArrayList<>();
        final List<EnglishUser> notifiedUsers = new ArrayList<>();

        final Instant now = Instant.now();
        for (EnglishUser user : userRepository.findAll()) {
            if (Duration.between(user.getLastNotified(), now).compareTo(user.getInterval()) > 0) {
                Set<Word> words = wordRepository.findByUser_Id(user.getId());
                if (words.isEmpty()) continue;

                notifications.add(
                    Notification.builder()
                        .user(user)
                        .text(getRandomElement(words).toString())
                        .build()
                );
                user.setLastNotified(now);
                notifiedUsers.add(user);
            }
        }

        // Update lastNotified time
        userRepository.saveAll(notifiedUsers);

        return notifications;
    }

    private EnglishUser createDefaultUser(Long id) {
        return EnglishUser.builder()
            .id(id)
            .lastNotified(Instant.now())
            .interval(Duration.ofMinutes(defaultNotificationInterval))
            .build();
    }

    @Value("${bot.scheduling.default-interval}")
    private void setDefaultInterval(int interval) {
        defaultNotificationInterval = interval;
    }

    private static final Random random = new Random();
    private static <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty())
            throw new IllegalArgumentException("The collection is empty");

        final int randomIndex = random.nextInt(collection.size());
        int i = 0;
        for (T element : collection) {
            if (i == randomIndex) {
                return element;
            }
            i++;
        }

        throw new IllegalStateException("The element has to be present. There might be something wrong with random.");
    }

    public long getNumberOfWords(Long id) {
        return wordRepository.countByUser_Id(id);
    }
}
