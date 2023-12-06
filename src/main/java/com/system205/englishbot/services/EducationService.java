package com.system205.englishbot.services;

import com.system205.englishbot.dto.Notification;
import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.entity.Word;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
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
        for (EnglishUser user : userService.getAllUsers()) {
            if (Duration.between(user.getLastNotified(), now).compareTo(user.getInterval()) > 0) {

                Word suggestedWord = user.getEducationPlan().suggestWord();
                notifications.add(Notification.builder()
                    .user(user)
                    .text(suggestedWord.toString())
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

}
