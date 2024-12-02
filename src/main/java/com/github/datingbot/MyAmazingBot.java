package com.github.datingbot;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.auxiliary.MyException;
import com.github.datingbot.auxiliary.State;
import com.github.datingbot.auxiliary.StringFunctions;
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
            Profile currentUser = allUsers.get(chatId);

            // FOR NEW USERS
            if (currentUser == null) {
                allUsers.put(chatId, new Profile(chatId, USER_NAME));
                MessageBuilder.usualMessage(chatId, "Давай заполним Твою анкету\nВведи имя:"); // MESSAGE
            }

            else {
                // USER WITH STATE REGISTRATION
                if (registrationStates.contains(currentUser.getUserState())) {
                    ProfileManager.changeProfileLocal(userMessage, currentUser); // REGISTRATION WITH MESSAGES
                }

                // USER WITH STATE OF BUTTON "МОЯ АНКЕТА"
                else if (currentUser.getUserState() == USER_PROFILE) {
                    if (messageText.compareTo("Назад") == 0) {
                        currentUser.setUserState(USER_STATE_MAIN_MENU);
                        MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
                    }
                    else if (messageText.compareTo("Изменить профиль") == 0) {
                        currentUser.setUserState(USER_PROFILE_CHANGE);
                        currentUser.setTempInfo(EMPTY);
                        ProfileManager.useProfile(currentUser, userMessage); // MESSAGE ???
                    }
                    else MessageBuilder.usualMessage(chatId, "Неправильный ввод", VIEW_PROFILE); // MESSAGE
                }

                else if (currentUser.getUserState() == USER_PROFILE_CHANGE) {
                    // служебные | контекстная
                    if (messageText.compareTo("Назад") == 0) {
                        currentUser.setTempInfo(EMPTY);
                        currentUser.setUserState(USER_PROFILE);
                        MessageBuilder.usualMessage(chatId, currentUser.getStr(), VIEW_PROFILE);
                    }
                    else {
                        if (servicePrompts.containsKey(messageText)) {
                            currentUser.setTempInfo(servicePrompts.get(messageText));
                            if (messageText.compareTo("Пол") == 0)
                                MessageBuilder.usualMessage(chatId, "Выбери пол :", GENDER_KEYBOARD); // MESSAGE
                            else MessageBuilder.usualMessage(chatId, "Введи " + messageText.toLowerCase() + ':'); // MESSAGE
                        }
                        else {
                            ProfileManager.useProfile(allUsers.get(chatId), userMessage); // MESSAGE ???
                            if (currentUser.getTempInfo() == EMPTY)
                                ProfileManager.emptyState(currentUser); // MESSAGE BY DEFAULT ???
                        }
                    }
                }

                // USER WITH STATE OF BUTTON "ДИАЛОГИ"
                else if (currentUser.getUserState() == USER_MESSAGES) {
                    if (messageText.compareTo("Назад") == 0) {
                        currentUser.setUserState(USER_STATE_MAIN_MENU);
                        MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
                    }
                    else if (messageText.compareTo("Выбрать диалог") == 0) {
                        currentUser.setUserState(USER_MESSAGES_CHOOSE);
                        MessageBuilder.usualMessage(chatId, "Введи порядковый номер диалога", BACK); // MESSAGE
                    }
                    else MessageBuilder.usualMessage(chatId, "Неправильный ввод", VIEW_MESSAGES); // MESSAGE
                }

                else if (currentUser.getUserState() == USER_MESSAGES_CHOOSE) {
                    int num = StringFunctions.isNum(messageText);
                    if (messageText.compareTo("Назад") == 0) {
                        currentUser.setUserState(USER_STATE_MAIN_MENU);
                        MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
                    }
                    else if (num != -1) {
                        if (num > 0 && num <= currentUser.getFriends().size()) {
                            MessageBuilder.usualMessage(chatId, "Вы выбрали пользователя "
                                    + currentUser.getFriends().get(num - 1), BACK); // MESSAGE
                            // EXTRA LOGIC
                        }
                        else MessageBuilder.usualMessage(chatId, "Неправильный ввод", BACK); // MESSAGE
                    }
                    else MessageBuilder.usualMessage(chatId, "Неправильный ввод", BACK); // MESSAGE
                }

                // IN OTHER STATES
                else {
                    // CHANGES STATE TO USER_PROFILE + EXTRA INFO
                    if (messageText.compareTo("Моя анкета") == 0) {
                        currentUser.setUserState(USER_PROFILE);
                        MessageBuilder.usualMessage(chatId, currentUser.getStr(), VIEW_PROFILE);
                    }
                    // CHANGES STATE TO USER_MESSAGES
                    else if (messageText.compareTo("Сообщения") == 0) {
                        currentUser.setUserState(USER_MESSAGES);
                        MessageBuilder.usualMessage(chatId, "Ваши сообщения:\n"
                                + StringFunctions.formatFriends(currentUser.getFriends()), VIEW_MESSAGES); // MESSAGE
                    }
                    // UNKNOWN MESSAGE
                    else MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
                    printProfile(currentUser);
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