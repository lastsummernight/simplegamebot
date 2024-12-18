package com.github.datingbot.keyboard;

import com.github.datingbot.auxiliary.Hobbies;
import com.github.datingbot.auxiliary.State;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.datingbot.auxiliary.State.*;
import static com.github.datingbot.auxiliary.Hobbies.*;

public class KeyboardBuilder {

    private static List<Hobbies> allHobbies = Arrays.asList(LITERATURE_HOBBY, DANCE_HOBBY, VIDEOGAMES_HOBBY, SCIENCE_HOBBY,
            SPORT_HOBBY, MUSIC_HOBBY, COOKING_HOBBY, TRAVELLING_HOBBY, ART_HOBBY);

    public static ReplyKeyboardMarkup buildKeyboard(State key) {

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        switch (key) {
            case USER_GENDER:
                keyboardRow.add("Парень");
                keyboardRow.add("Девушка");
                keyboard.add(keyboardRow);
                break;

            case USER_STATE_MAIN_MENU:
                keyboardRow.add("Поиск");
                keyboardRow.add("Моя анкета");
                keyboardRow.add("Анкеты");
                keyboard.add(keyboardRow);
                break;

            case USER_STATE_FINDING:
                keyboardRow.add("♥");
                keyboardRow.add("Назад");
                keyboardRow.add("\uD83D\uDC94"); // разбитое сердечко
                keyboard.add(keyboardRow);
                break;

            case USER_PROFILE:
                keyboardRow.add("Изменить профиль");
                keyboardRow.add("Изменить интересы");
                keyboardRow.add("Назад");
                keyboard.add(keyboardRow);
                break;

            case USER_PROFILE_CHANGE:
                keyboardRow.add("Имя");
                keyboardRow.add("Возраст");
                keyboardRow.add("Город");

                keyboard.add(keyboardRow);
                keyboardRow = new KeyboardRow();

                keyboardRow.add("Пол");
                keyboardRow.add("О себе");
                keyboardRow.add("Назад");
                keyboard.add(keyboardRow);
                break;

            case USER_CONNECTIONS:
                keyboardRow.add("Изменить оценку");
                keyboardRow.add("Запросы");
                keyboardRow.add("Назад");
                keyboard.add(keyboardRow);
                break;

            case USER_MARKS:
                keyboardRow.add("Понравившиеся");
                keyboardRow.add("Не понравившиеся");
                keyboardRow.add("Назад");
                keyboard.add(keyboardRow);
                break;

            case USER_REQUESTS:
                keyboardRow.add("Дать тг");
                keyboardRow.add("Назад");
                keyboard.add(keyboardRow);
                break;


            case USER_PROFILE_HOBBIES:
                for (int i = 0; i < allHobbies.size(); i++) {
                    keyboardRow.add(allHobbies.get(i).getTitle());
                    if (i % 3 == 2) {
                        keyboard.add(keyboardRow);
                        keyboardRow = new KeyboardRow();
                    }
                }
                if (keyboardRow.isEmpty())
                    keyboardRow.add("Назад");
                else {
                    keyboard.add(keyboardRow);
                    keyboardRow = new KeyboardRow();
                    keyboardRow.add("Назад");
                }
                keyboard.add(keyboardRow);
                break;

            case EMPTY:
                keyboardRow.add("Назад");
                keyboard.add(keyboardRow);
                break;
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        if (key == USER_GENDER)
            replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }
}