package com.github.datingbot;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.auxiliary.MyException;
import com.github.datingbot.auxiliary.State;
import com.github.datingbot.database.DatabaseManager;
import com.github.datingbot.keyboard.Keyboard;
import com.github.datingbot.message.MessageBuilder;
import com.github.datingbot.profile.Profile;
import com.github.datingbot.profile.ProfileManager;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.w3c.dom.ls.LSOutput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.github.datingbot.auxiliary.Debugger.printProfile;
import static com.github.datingbot.auxiliary.State.*;
import static com.github.datingbot.keyboard.Keyboard.*;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient;
    private HashMap<String, Profile> allUsers;
    private HashMap<String, State> servicePrompts;
    private List<State> registrationStates;

    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        allUsers = DatabaseManager.getAllUsers();
        servicePrompts = new HashMap<>();
        servicePrompts.put("Имя", USER_NAME);
        servicePrompts.put("Возраст", USER_AGE);
        servicePrompts.put("Город", USER_CITY);
        servicePrompts.put("Пол", USER_GENDER);
        servicePrompts.put("О себе", USER_INFO);
        servicePrompts.put("Назад", USER_STATE_MAIN_MENU);
        registrationStates = Arrays.asList(USER_NAME, USER_AGE, USER_CITY, USER_GENDER, USER_INFO);
        Debugger.setUp();
        MessageBuilder.setUp(allUsers);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message userMessage = update.getMessage();
            String messageText = userMessage.getText();
            String chatId = userMessage.getChatId().toString();
            SendMessage botAnswer = null;

            if (!allUsers.containsKey(chatId)) {
                allUsers.put(chatId, new Profile(chatId, USER_NAME));
                MessageBuilder.createMessage(chatId);
                MessageBuilder.usualMessage(chatId, "Давай заполним Твою анкету\nВведи имя:");
            }

            else {
                if (registrationStates.contains(allUsers.get(chatId).getUserState())) {
                    ProfileManager.changeProfileLocal(userMessage, allUsers.get(chatId));
                }

                else if (allUsers.get(chatId).getUserState() == USER_PROFILE) {
                    // служебные | контекстная
                    if (messageText.compareTo("Назад") == 0) {
                        allUsers.get(chatId).setTempInfo(null);
                        allUsers.get(chatId).setUserState(USER_STATE_MAIN_MENU);
                        MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD);
                    }
                    else {
                        if (servicePrompts.containsKey(messageText)) {
                            allUsers.get(chatId).setTempInfo(servicePrompts.get(messageText));
                            if (messageText.compareTo("Пол") == 0)
                                MessageBuilder.usualMessage(chatId, "Выбери пол :", GENDER_KEYBOARD);
                            else MessageBuilder.usualMessage(chatId, "Введи " + messageText.toLowerCase() + ':');
                        }
                        else {
                            ProfileManager.useProfile(allUsers.get(chatId), userMessage);
                            if (allUsers.get(chatId).getTempInfo() == EMPTY)
                                ProfileManager.emptyState(allUsers.get(chatId));
                        }
                    }
                }

                else {
                    if (messageText.compareTo("/s") == 0) MessageBuilder.usualMessage(chatId, "Не выбирай ничего");
                    else if (messageText.compareTo("Моя анкета") == 0) {
                        allUsers.get(chatId).setUserState(USER_PROFILE);
                        allUsers.get(chatId).setTempInfo(EMPTY);
                        ProfileManager.useProfile(allUsers.get(chatId), userMessage);
                    }
                    else MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD);
                    printProfile(allUsers.get(chatId));
                }
            }

            try {
                botAnswer = MessageBuilder.execute(chatId);
            }
            catch (MyException e) {
                Debugger.printException(e);
                botAnswer = new SendMessage(chatId, "Exception caused");
            }

            try {
                telegramClient.execute(botAnswer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}