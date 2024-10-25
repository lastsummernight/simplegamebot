package com.github.simplegamebot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.HashMap;

import static com.github.simplegamebot.State.*;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private HashMap<String, State> allUsers;
    private HashMap<State, ReplyKeyboardMarkup> allKeyboards;
    private ReplyKeyboardMarkup currentMarkup;

    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        allUsers = new HashMap<>();

        KeyboardBuilder BUILDER = new KeyboardBuilder();

        allKeyboards = new HashMap<>();

        allKeyboards.put(USER_STATE_GENDER, BUILDER.buildKeyboard(USER_STATE_GENDER));
        allKeyboards.put(USER_STATE_MAIN_MENU, BUILDER.buildKeyboard(USER_STATE_MAIN_MENU));
        allKeyboards.put(USER_STATE_FINDING, BUILDER.buildKeyboard(USER_STATE_FINDING));
        allKeyboards.put(EMPTY, BUILDER.buildKeyboard(EMPTY));

        currentMarkup = allKeyboards.get("empty");

        Communicator proba = new Communicator();

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
                    allUsers.put(chatId, USER_STATE_START);
                    botReply.setText("Давай заполним Твою анкету\nВведи имя:");
            }

            else {
                if (allUsers.containsKey(chatId)) {
                    switch (allUsers.get(chatId)) {

                        case USER_STATE_START:
                            // здеся нада записать фио человека
                            allUsers.replace(chatId, USER_STATE_AGE);
                            botReply.setText("Введи возраст (число):");
                            break;

                        case USER_STATE_AGE:
                            int age = StringFunctions.isNum(messageText);
                            if (age != -1) {
                                if ((age >= 14) && (age <= 50)) {
                                    // здеся нада записать др человека
                                    allUsers.replace(chatId, USER_STATE_CITY);
                                    botReply.setText("Введи свой город:");
                                    break;
                                }
                            }
                            botReply.setText("Твой возраст должен быть числом от 14 лет ");
                            break;

                        case USER_STATE_CITY:
                            // здеся нада записать город человека
                            allUsers.replace(chatId, USER_STATE_GENDER);
                            botReply.setText("Введи пол:");
                            currentMarkup = allKeyboards.get(USER_STATE_GENDER);
                            break;


                        case USER_STATE_GENDER:
                            if ((messageText.compareTo("Парень") == 0) || (messageText.compareTo("Девушка") == 0)) {
                                currentMarkup = allKeyboards.get("empty");
                                // здеся нада записать пол человека
                                allUsers.replace(chatId, USER_STATE_INFO);
                                botReply.setText("Введи о себе:");
                                break;
                            }
                            botReply.setText("Неправильный ввод");
                            break;

                        case USER_STATE_INFO:
                            // здеся нада записать "о себе" человека
                            allUsers.replace(chatId, USER_STATE_MAIN_MENU);
                            botReply.setText("Анкета готова");
                            currentMarkup = allKeyboards.get(USER_STATE_MAIN_MENU);
                            break;

                        default:
                            botReply.setText("Выберите команду на клавиатуре");
                            break;
                    }
                }

                botReply.setReplyMarkup(currentMarkup);
            }

            try {
                telegramClient.execute(botReply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}