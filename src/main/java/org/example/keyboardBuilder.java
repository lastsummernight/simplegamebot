package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class keyboardBuilder {

    public ReplyKeyboardMarkup buildKeyboard(String key) {


        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        switch (key) {
            case "sex":
                keyboardRow.add("Парень");
                keyboardRow.add("Девушка");
                keyboard.add(keyboardRow);
                break;

            case "main_menu":
                keyboardRow.add("Поиск");
                keyboardRow.add("Моя анкета");
                keyboardRow.add("Сообщения");
                keyboard.add(keyboardRow);
                break;

            case "like_dislike":
                keyboardRow.add("♥");
                keyboardRow.add("\uD83D\uDC94"); // разбитое сердечко
                keyboard.add(keyboardRow);
                break;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }
}