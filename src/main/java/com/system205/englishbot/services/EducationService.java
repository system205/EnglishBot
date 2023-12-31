package com.system205.englishbot.services;

import com.system205.englishbot.dto.Notification;
import com.system205.englishbot.entity.EducationPlan;
import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.entity.NotificationSettings;
import com.system205.englishbot.entity.Word;
import com.system205.englishbot.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class EducationService {

    private final UserService userService;

    public EducationService(UserService userService) {
        this.userService = userService;
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
    public List<Notification> getNotifications() {
        final List<Notification> notifications = new ArrayList<>();
        final List<EnglishUser> notifiedUsers = new ArrayList<>();

        final Instant now = Instant.now();
        final LocalTime nowTime = LocalTime.now();
        for (EnglishUser user : userService.getAllUsers()) {
            final NotificationSettings userSettings = user.getNotificationSettings();
            if (Duration.between(user.getLastNotified(), now).compareTo(userSettings.getInterval()) > 0 &&
            userSettings.getEndTime().isAfter(nowTime) && userSettings.getStartTime().isBefore(nowTime)) {

                Word suggestedWord = user.getEducationPlan().suggestWord();
                notifications.add(Notification.builder()
                    .user(user)
                    .text(suggestedWord != null ? suggestedWord.toString() : "You have no words: /add_words")
                    .build());

                // For update
                user.setLastNotified(now);
                notifiedUsers.add(user);
            }
        }

        // Update lastNotified time
        userService.updateUsers(notifiedUsers);

        return notifications;
    }

    /**
     * Update each */
    @Scheduled(cron = "${bot.education-plan.scheduling.cron}")
    private void updateEducationPlans(){
        LocalDate now = LocalDate.now();
        List<EnglishUser> updatedUsers = new ArrayList<>();

        for (EnglishUser user : userService.getAllUsers()) {
            updateUserEducationPlan(user, now).ifPresent(updatedUsers::add);
        }

        log.info("Education plan was updated for {} users", updatedUsers.size());
        userService.updateUsers(updatedUsers);

    }

    /**
     * @return Optional with the updated user or empty if nothing was required update*/
    private Optional<EnglishUser> updateUserEducationPlan(EnglishUser user, LocalDate now) {
        final EducationPlan educationPlan = user.getEducationPlan();
        final LocalDate lastUpdate = educationPlan.getLastUpdate();

        // Update education plan - suggest new words
        if (ChronoUnit.DAYS.between(lastUpdate, now) >= 1) {
            educationPlan.setLastUpdate(now);
            final List<Word> suggestedUserWords = suggestUserWords(user);
            educationPlan.setDailyWords(suggestedUserWords);
            log.info("Education plan for user {} is updated. New words: {}", user.getId(), suggestedUserWords);

            return Optional.of(user);
        }

        return Optional.empty();
    }

    /**
     * Picks N words from all users saved words <br>
     * Where N is number of daily suggested words
     * @return List of words for daily plan */
    private List<Word> suggestUserWords(EnglishUser user){
        final Set<Word> availableWords = userService.getWordsByUserId(user.getId());
        if (availableWords.isEmpty()) return Collections.emptyList();
        final int numberOfDailyWords = user.getEducationPlan().getNumberOfWords();
        Word[] suggestedWords = new Word[numberOfDailyWords];
        for (int i = 0; i < numberOfDailyWords; ++i) {
            suggestedWords[i] = Utils.getRandomElement(availableWords);
        }
        return List.of(suggestedWords);
    }

    /**
     * @return new plan for a given user
     * */
    public void getNewEducationPlanForUser(EnglishUser user){
        final EducationPlan plan = EducationPlan.defaultPlan();
        plan.setNumberOfWords(user.getEducationPlan().getNumberOfWords());
        plan.setLastUpdate(LocalDate.now());
        plan.setDailyWords(suggestUserWords(user));
        user.setEducationPlan(plan);
        userService.updateUsers(List.of(user));
    }

}
