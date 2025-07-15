package com.lgcms.member.service;

public class NicknameUtil {
    public static String generateNickname() {
        String nicknamePrefix = nicknamePrefixes[(int) Math.floor(Math.random() * nicknamePrefixes.length)];
        return nicknamePrefix + "-" + (int) Math.floor(Math.random() * 1_000);
    }

    private static String[] nicknamePrefixes = {
        "너구리", "사자", "호랑이", "코끼리", "기린",
        "원숭이", "늑대", "여우", "토끼", "곰",
        "다람쥐", "고양이", "펭귄", "돌고래", "상어",
        "악어", "코알라", "수달", "하마", "카피바라"
    };
}
