package com.github.datingbot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.github.datingbot.auxiliary.State.*;

public enum Keyboard {

    GENDER_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_GENDER)),
    MAIN_MENU_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_STATE_MAIN_MENU)),
    FINDING_KEYBOARD(KeyboardBuilder.buildKeyboard(USER_STATE_FINDING)),
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
