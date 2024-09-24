package org.example;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class keyboardBuilder {

    public ReplyKeyboardMarkup buildKeyboard(List<String> array) {

        int rows = 2;
        int columns = 2;

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int countRow = 0; countRow != rows; countRow++) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (int countCol = 0; countCol != columns; countCol++) {
                keyboardRow.add(array.get(countRow * rows + countCol));
            }
            keyboard.add(keyboardRow);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        //replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup buildKeyboard() {

        List<String> ListOfButtons = Arrays.asList("Предложение ничьи", "Сдаться", "NW", "N", "NE", "W", "C", "E", "SW", "S", "SE");
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (int countRow = -1; countRow != 3; countRow++) {
            KeyboardRow keyboardRow = new KeyboardRow();

            if (countRow == -1) {
                keyboardRow.add(ListOfButtons.get(0));
                keyboardRow.add(ListOfButtons.get(1));
            }

            else {
                for (int countCol = 0; countCol != 3; countCol++) {
                    keyboardRow.add(ListOfButtons.get(countRow * 3 + countCol + 2));
                }
            }

            keyboard.add(keyboardRow);

        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        //replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup buildKeyboard(int rows, int columns) {

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int countRow = 0; countRow != rows; countRow++) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (int countCol = 0; countCol != columns; countCol++) {
                keyboardRow.add(Integer.toString(countRow * 3 + countCol + 1));
            }
            keyboard.add(keyboardRow);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}