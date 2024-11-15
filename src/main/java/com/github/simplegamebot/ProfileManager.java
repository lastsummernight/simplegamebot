package com.github.simplegamebot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Arrays;
import java.util.List;

import static com.github.simplegamebot.Keyboard.*;
import static com.github.simplegamebot.State.*;

public class ProfileManager {

    public ProfileManager() {

    }

    private void supportName(Message message, Profile profile, SendMessage botReply) {
        profile.setUsername(message.getText());
        profile.setUserState(USER_AGE);
        botReply.setText("Введи возраст (число):");
    }

    private void supportAge(Message message, Profile profile, SendMessage botReply) {
        int age = StringFunctions.isNum(message.getText());
        if (age != -1) {
            if ((age >= 18) && (age <= 50)) {
                profile.setAge(age);
                profile.setUserState(USER_CITY);
                botReply.setText("Введи свой город:");
                return;
            }
        }
        botReply.setText("Твой возраст должен быть числом от 18 лет ");
    }

    private void supportCity(Message message, Profile profile, SendMessage botReply) {
        profile.setCity(message.getText());
        profile.setUserState(USER_GENDER);
        botReply.setText("Введи пол:");
        botReply.setReplyMarkup(GENDER_KEYBOARD.getKeyboardMarkup());

    }

    private void supportGender(Message message, Profile profile, SendMessage botReply) {
        if ((message.getText().compareTo("Парень") == 0) || (message.getText().compareTo("Девушка") == 0)) {
            profile.setGender(message.getText());
            profile.setUserState(USER_INFO);
            botReply.setText("Введи о себе:");
            return;
        }
        botReply.setText("Неправильный ввод");
        botReply.setReplyMarkup(GENDER_KEYBOARD.getKeyboardMarkup());
    }

    private void supportInfo(Message message, Profile profile, SendMessage botReply) {
        profile.setInfo(message.getText());
        profile.setUserState(USER_STATE_MAIN_MENU);
        botReply.setText("Анкета готова");
        botReply.setReplyMarkup(MAIN_MENU_KEYBOARD.getKeyboardMarkup());
    }

    public void changeProfileLocal(Message message, Profile profile, SendMessage botReply) {

        switch (profile.getUserState()) {
            case USER_NAME -> supportName(message, profile, botReply);

        }
        switch (profile.getUserState()) {

            case USER_NAME:
                supportName(message, profile, botReply);
                break;

            case USER_AGE:
                supportAge(message, profile, botReply);
                break;

            case USER_CITY:
                supportCity(message, profile, botReply);
                break;

            case USER_GENDER:
                supportGender(message, profile, botReply);
                break;

            case USER_INFO:
                supportInfo(message, profile, botReply);
                break;

            default:
                botReply.setText("Выберите команду на клавиатуре");
                break;
        }

    }

}
