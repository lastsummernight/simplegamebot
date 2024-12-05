package com.github.datingbot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.github.datingbot.auxiliary.State.*;

public enum Keyboard {

    GENDER_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_GENDER)),
    MAIN_MENU_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_STATE_MAIN_MENU)),
    FINDING_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_STATE_FINDING)),
    PROFILE_CHANGE_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_PROFILE_CHANGE)),
    VIEW_PROFILE_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_PROFILE)),
    VIEW_MESSAGES_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_MESSAGES)),
    BACK_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_MESSAGES_CHOOSE)),
    EMPTY_KEYBOARD(KeyboardBuilder.buildKeyboard(EMPTY));

    private ReplyKeyboardMarkup keyboardMarkup;

    Keyboard() {}

    Keyboard(ReplyKeyboardMarkup keyboardMarkup) {
        this.keyboardMarkup = keyboardMarkup;
    }

    public ReplyKeyboardMarkup getKeyboardMarkup() {
        return keyboardMarkup;
    }

}
