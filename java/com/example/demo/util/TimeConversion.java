package com.example.demo.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConversion {
    public static String timePostConversion(LocalDateTime createdAt) {
        LocalDateTime currentTime = LocalDateTime.now();
        Long timeDiff = Duration.between(createdAt, currentTime).getSeconds();
        String resultConversion = "";

        if ((timeDiff / 86400) > 1) {
            resultConversion = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else if ((timeDiff / 86400) > 0) { // 1 일
            resultConversion = timeDiff / 86400 + "일 전";
        } else if ((timeDiff / 3600) > 0) { // 시간
            resultConversion = timeDiff / 3600 + "시간 전";
        } else if ((timeDiff / 60) > 0) { // 분
            resultConversion = timeDiff / 60 + "분 전";
        } else {
            resultConversion = timeDiff + "초 전";
        }

        return resultConversion;
    }

    public static String timeChatConversion(LocalDateTime createdAt) {
        LocalDateTime currentTime = LocalDateTime.now();
        String resultConversion = "";

        if (currentTime.getDayOfMonth() > createdAt.getDayOfMonth()) {
            resultConversion = createdAt.format(DateTimeFormatter.ofPattern("MM월 dd일"));
        }
         else {
            resultConversion = createdAt.format(DateTimeFormatter.ofPattern("a hh시 mm분 ss초"));
        }

        return resultConversion;
    }
}
