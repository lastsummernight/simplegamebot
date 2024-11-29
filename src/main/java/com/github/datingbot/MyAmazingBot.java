package com.github.datingbot;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.auxiliary.MyException;
import com.github.datingbot.auxiliary.State;
import com.github.datingbot.database.DatabaseManager;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.github.datingbot.auxiliary.Debugger.printProfile;
import static com.github.datingbot.auxiliary.State.*;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private TelegramClient telegramClient;
    private HashMap<String, Profile> allUsers;
    private List<State> registrationStates;

    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        allUsers = DatabaseManager.getAllUsers();
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

            if (messageText.compareTo("/start") == 0) {
                if (!allUsers.containsKey(chatId))
                    System.out.println("||| DEBUG ||| allusers contains chatId" + chatId);
                    allUsers.put(chatId, new Profile(chatId, USER_NAME));
                    MessageBuilder.createMessage(chatId);
                    MessageBuilder.setText(chatId, "Давай заполним Твою анкету\nВведи имя:");
            }

            else {
                if (allUsers.containsKey(chatId)) {

                    if (registrationStates.contains(allUsers.get(chatId).getUserState())) {
                        ProfileManager.changeProfileLocal(userMessage, allUsers.get(chatId));
                    }

                    else {
                        if (messageText.compareTo("/s") == 0) MessageBuilder.setText(chatId, "Не выбирай ничего");
                        else MessageBuilder.setText(chatId, "Выбери команду на клавиатуре");
                        printProfile(allUsers.get(chatId));
                    }
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