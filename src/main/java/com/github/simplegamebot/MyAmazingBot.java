package com.github.simplegamebot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.github.simplegamebot.Debugger.printProfile;
import static com.github.simplegamebot.State.*;
import static com.github.simplegamebot.Keyboard.*;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient;
    private HashMap<String, Profile> allUsers;
    private HashMap<State, Keyboard> allKeyboards;
    private ProfileManager PM;
    private List<State> settingStates;

    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        allUsers = new HashMap<>();

        allKeyboards = new HashMap<>();

        allKeyboards.put(USER_GENDER, GENDER_KEYBOARD);
        allKeyboards.put(USER_STATE_MAIN_MENU, MAIN_MENU_KEYBOARD);
        allKeyboards.put(USER_STATE_FINDING, FINDING_KEYBOARD);
        allKeyboards.put(EMPTY, EMPTY_KEYBOARD);

        settingStates = Arrays.asList(USER_NAME, USER_AGE, USER_CITY, USER_GENDER, USER_INFO);

//        Communicator proba = new Communicator();
        PM = new ProfileManager(); // profile manager

    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message userMessage = update.getMessage();
            String messageText = userMessage.getText();
            String chatId = userMessage.getChatId().toString();

            SendMessage botReply = new SendMessage(chatId, messageText);

            if (messageText.compareTo("/start") == 0) {
                if (!allUsers.containsKey(chatId))
                    allUsers.put(chatId, new Profile(chatId, USER_NAME));
                    botReply.setText("Давай заполним Твою анкету\nВведи имя:");
            }

            else {
                if (allUsers.containsKey(chatId)) {

                    if (settingStates.contains(allUsers.get(chatId).getUserState())) {
                        PM.changeProfileLocal(userMessage, allUsers.get(chatId), botReply);
                    }

                    else {
                        try {
                            printProfile(allUsers.get(chatId));
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }

            try {
                telegramClient.execute(botReply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}