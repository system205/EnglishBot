package com.system205.englishbot.services;

import com.system205.englishbot.entity.EnglishUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService {

    private EnglishUser lastUser;

    public EnglishUser getUser(Long id) {
        if (this.lastUser != null) return this.lastUser;

        final EnglishUser user = new EnglishUser(id, new HashSet<>());
        this.lastUser = user;
        return user;
    }

}
