package com.github.datingbot.profile;

import com.github.datingbot.message.MessageBuilder;
import com.github.datingbot.auxiliary.StringFunctions;
import com.github.datingbot.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.github.datingbot.keyboard.Keyboard.*;
import static com.github.datingbot.auxiliary.State.*;

public class ProfileManager {

    private static void supportName(Message message, Profile profile) {
        profile.setUsername(message.getText());
        profile.setUserState(USER_AGE);
        MessageBuilder.setText(profile.getChatId(), "Введи возраст (число):");
    }

    private static void supportAge(Message message, Profile profile) {
        int age = StringFunctions.isNum(message.getText());
        if (age != -1) {
            if ((age >= 18) && (age <= 50)) {
                profile.setAge(age);
                profile.setUserState(USER_CITY);
                MessageBuilder.setText(profile.getChatId(),"Введи свой город:");
                return;
            }
        }
        MessageBuilder.setText(profile.getChatId(), "Твой возраст должен быть числом от 18 лет ");
    }

    private static void supportCity(Message message, Profile profile) {
        profile.setCity(message.getText());
        profile.setUserState(USER_GENDER);
        MessageBuilder.setText(profile.getChatId(), "Введи пол:");
        MessageBuilder.setKeyboard(profile.getChatId(), GENDER_KEYBOARD);
    }

    private static void supportGender(Message message, Profile profile) {
        if ((message.getText().compareTo("Парень") == 0) || (message.getText().compareTo("Девушка") == 0)) {
            profile.setGender(message.getText());
            profile.setUserState(USER_INFO);
            MessageBuilder.setText(profile.getChatId(), "Введи о себе:");
            return;
        }
        MessageBuilder.setText(profile.getChatId(), "Неправильный ввод");
        MessageBuilder.setKeyboard(profile.getChatId(), GENDER_KEYBOARD);
    }

    private static void supportInfo(Message message, Profile profile) {
        profile.setInfo(message.getText());
        profile.setUserState(USER_STATE_MAIN_MENU);
        DatabaseManager.addUser(profile);
        MessageBuilder.setText(profile.getChatId(), "Анкета готова");
        MessageBuilder.setKeyboard(profile.getChatId(), MAIN_MENU_KEYBOARD);
    }

    public static void changeProfileLocal(Message message, Profile profile) {

        switch (profile.getUserState()) {
            case USER_NAME -> supportName(message, profile);
            case USER_AGE -> supportAge(message, profile);
            case USER_CITY -> supportCity(message, profile);
            case USER_GENDER -> supportGender(message, profile);
            case USER_INFO -> supportInfo(message, profile);
            default -> MessageBuilder.setText(profile.getChatId(), "Выберите команду на клавиатуре");
        }
    }

    public static void changeProfileGlobal(Profile profile) {
        DatabaseManager.changeUser(profile);
    }
}
