package com.github.datingbot.auxiliary;

public enum State {

    USER_NAME("Имя"),
    USER_AGE("Возраст"),
    USER_CITY("Город"),
    USER_GENDER("Пол"),
    USER_INFO("О себе"),
    USER_PHOTO("Фото"),
    USER_HOBBIES,
    USER_STATE_MAIN_MENU("Назад"),
    USER_STATE_FINDING,
    USER_PROFILE,
    USER_PROFILE_CHANGE,
    USER_PROFILE_HOBBIES,
    USER_CONNECTIONS,
    USER_MARKS,
    USER_REQUESTS,
    EMPTY;

    private String title;

    private State() {
    }

    private State(String name) {
        title = name;
    }

    public String getTitle() {
        return title;
    }
}