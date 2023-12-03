package com.system205.englishbot.services;

import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.entity.Word;
import com.system205.englishbot.repositories.EnglishUserRepository;
import com.system205.englishbot.repositories.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EnglishUserRepository userRepository;
    private final WordRepository wordRepository;

    public EnglishUser getUser(Long id) {
        final Optional<EnglishUser> optionalUser = userRepository.findById(id);
        EnglishUser user;
        if (optionalUser.isEmpty()) {
            user = new EnglishUser(id, Collections.emptySet());
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
}
