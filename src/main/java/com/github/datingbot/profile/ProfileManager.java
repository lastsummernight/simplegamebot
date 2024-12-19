package com.github.datingbot.profile;

import com.github.datingbot.message.MessageBuilder;
import com.github.datingbot.auxiliary.StringFunctions;
import com.github.datingbot.database.DatabaseManager;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.github.datingbot.keyboard.Keyboard.*;
import static com.github.datingbot.auxiliary.State.*;

public class ProfileManager {

    private static void supportName(Message message, Profile profile, boolean flag) {
        profile.setUsername(message.getText());
        if (!flag) {
            profile.setUserState(USER_AGE);
            MessageBuilder.usualMessage(profile.getChatId(), "Введи возраст (число):");
        }
        else {
            profile.setTempInfo(EMPTY);
            DatabaseManager.changeUser(profile);
        }
    }

    private static void supportAge(Message message, Profile profile, boolean flag) {
        int age = StringFunctions.isNum(message.getText());
        if (age != -1) {
            if ((age >= 18) && (age <= 50)) {
                profile.setAge(age);
                if (!flag) {
                    profile.setUserState(USER_CITY);
                    MessageBuilder.usualMessage(profile.getChatId(), "Введи свой город:");
                }
                else {
                    profile.setTempInfo(EMPTY);
                    DatabaseManager.changeUser(profile);
                }
                return;
            }
        }
        MessageBuilder.usualMessage(profile.getChatId(), "Твой возраст должен быть числом от 18 лет ");
    }

    private static void supportCity(Message message, Profile profile, boolean flag) {
        profile.setCity(message.getText());
        if (!flag) {
            profile.setUserState(USER_GENDER);
            MessageBuilder.usualMessage(profile.getChatId(), "Введи пол:", GENDER_KEYBOARD);
        }
        else {
            profile.setTempInfo(EMPTY);
            DatabaseManager.changeUser(profile);
        }
    }

    private static void supportGender(Message message, Profile profile, boolean flag) {
        if ((message.getText().equals("Парень")) || (message.getText().equals("Девушка"))) {
            profile.setGender(message.getText());
            if (!flag) {
                profile.setUserState(USER_INFO);
                MessageBuilder.usualMessage(profile.getChatId(), "Введи о себе:");
            }
            else {
                profile.setTempInfo(EMPTY);
                DatabaseManager.changeUser(profile);
            }
            return;
        }
        MessageBuilder.usualMessage(profile.getChatId(), "Неправильный ввод", GENDER_KEYBOARD);
    }

    private static void supportInfo(Message message, Profile profile, boolean flag) {
        profile.setInfo(message.getText());
        if (!flag) {
            profile.setUserState(USER_STATE_MAIN_MENU);
            DatabaseManager.addUser(profile);
            MessageBuilder.usualMessage(profile.getChatId(), "Анкета готова", MAIN_MENU_KEYBOARD);
        }
        else {
            profile.setTempInfo(EMPTY);
            DatabaseManager.changeUser(profile);
        }
    }

    public static void changeProfileLocal(Message message, Profile profile) {

        switch (profile.getUserState()) {
            case USER_NAME -> supportName(message, profile, false);
            case USER_AGE -> supportAge(message, profile, false);
            case USER_CITY -> supportCity(message, profile, false);
            case USER_GENDER -> supportGender(message, profile, false);
            case USER_INFO -> supportInfo(message, profile, false);
            default -> MessageBuilder.usualMessage(profile.getChatId(), "Выберите команду на клавиатуре");
        }
    }

    public static void changeProfileGlobal(Profile profile) {
        DatabaseManager.changeUser(profile);
    }

    public static void emptyState(Profile profile) {
        MessageBuilder.usualMessage(profile.getChatId(), "Что Вы хотите изменить?", PROFILE_CHANGE_KEYBOARD);
    }

    public static void useProfile(Profile profile, Message message) {
        switch (profile.getTempInfo()) {
            case EMPTY -> emptyState(profile);
            case USER_NAME -> supportName(message, profile, true);
            case USER_AGE -> supportAge(message, profile, true);
            case USER_CITY -> supportCity(message, profile, true);
            case USER_GENDER -> supportGender(message, profile, true);
            case USER_INFO -> supportInfo(message, profile, true);
        }
    }
}
