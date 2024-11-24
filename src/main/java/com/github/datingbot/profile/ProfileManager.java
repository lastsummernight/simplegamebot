package com.github.datingbot.profile;

import com.github.datingbot.auxiliary.StringFunctions;
import com.github.datingbot.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.github.datingbot.keyboard.Keyboard.*;
import static com.github.datingbot.auxiliary.State.*;

public class ProfileManager {

    private static void supportName(Message message, Profile profile, SendMessage botReply) {
        profile.setUsername(message.getText());
        profile.setUserState(USER_AGE);
        botReply.setText("Введи возраст (число):");
    }

    private static void supportAge(Message message, Profile profile, SendMessage botReply) {
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

    private static void supportCity(Message message, Profile profile, SendMessage botReply) {
        profile.setCity(message.getText());
        profile.setUserState(USER_GENDER);
        botReply.setText("Введи пол:");
        botReply.setReplyMarkup(GENDER_KEYBOARD.getKeyboardMarkup());

    }

    private static void supportGender(Message message, Profile profile, SendMessage botReply) {
        if ((message.getText().compareTo("Парень") == 0) || (message.getText().compareTo("Девушка") == 0)) {
            profile.setGender(message.getText());
            profile.setUserState(USER_INFO);
            botReply.setText("Введи о себе:");
            return;
        }
        botReply.setText("Неправильный ввод");
        botReply.setReplyMarkup(GENDER_KEYBOARD.getKeyboardMarkup());
    }

    private static void supportInfo(Message message, Profile profile, SendMessage botReply) {
        profile.setInfo(message.getText());
        profile.setUserState(USER_STATE_MAIN_MENU);
        DatabaseManager.addUser(profile);
        botReply.setText("Анкета готова");
        botReply.setReplyMarkup(MAIN_MENU_KEYBOARD.getKeyboardMarkup());
    }

    public static void changeProfileLocal(Message message, Profile profile, SendMessage botReply) {

        switch (profile.getUserState()) {
            case USER_NAME -> supportName(message, profile, botReply);
            case USER_AGE -> supportAge(message, profile, botReply);
            case USER_CITY -> supportCity(message, profile, botReply);
            case USER_GENDER -> supportGender(message, profile, botReply);
            case USER_INFO -> supportInfo(message, profile, botReply);
            default -> botReply.setText("Выберите команду на клавиатуре");
        }
    }

    public static void changeProfileGlobal(Profile profile) {
        DatabaseManager.changeUser(profile);
    }
}
