package com.github.simplegamebot;

import org.telegram.telegrambots.meta.api.objects.User;

public enum State {

    USER_STATE_START,
    USER_STATE_AGE,
    USER_STATE_CITY,
    USER_STATE_GENDER,
    USER_STATE_INFO,
    USER_STATE_MAIN_MENU,
    USER_STATE_FINDING,
    EMPTY;


    private String title;

    State() {}

    State(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}