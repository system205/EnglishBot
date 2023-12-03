package com.system205.englishbot.dto;

import com.system205.englishbot.entity.EnglishUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    private EnglishUser user;
    private String text;
}
