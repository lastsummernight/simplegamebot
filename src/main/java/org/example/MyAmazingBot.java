package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private HashMap<String, Integer> allUsers;
    private HashMap<String, ReplyKeyboardMarkup> allKeyboards;

    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        allUsers = new HashMap<>();

        keyboardBuilder BUILDER = new keyboardBuilder();

        allKeyboards = new HashMap<>();

        allKeyboards.put("sex", BUILDER.buildKeyboard("sex"));
        allKeyboards.put("main_menu", BUILDER.buildKeyboard("main_menu"));
        allKeyboards.put("like_dislike", BUILDER.buildKeyboard("like_dislike"));

        Communicator proba = new Communicator();

    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message userMessage = update.getMessage();
            String messageText = userMessage.getText();
            String chatId = userMessage.getChatId().toString();

            SendMessage botReply = new SendMessage(chatId, messageText);

            if (messageText.contains("/start")) {
                if (!allUsers.containsKey(chatId))
                    allUsers.put(chatId, 1);
                botReply.setText("Давай заполним Твою анкету\nВведи имя:");
            }

            else {
                if (allUsers.containsKey(chatId)) {
                    switch (allUsers.get(chatId)) {

                        case 1:
                            // здеся нада записать фио человека
                            allUsers.replace(chatId, 2);
                            botReply.setText("Введи возраст (число):");
                            break;

                        case 2:
                            int age = StringFunctions.isNum(messageText);
                            if (age != -1) {
                                if ((age >= 14) && (age <= 50)) {
                                    // здеся нада записать др человека
                                    allUsers.replace(chatId, 3);
                                    botReply.setText("Введи свой город:");
                                    break;
                                }
                            }
                            botReply.setText("Твой возраст должен быть числом от 14 лет ");
                            break;

                        case 3:
                            // здеся нада записать город человека
                            allUsers.replace(chatId, 4);
                            botReply.setText("Введи пол:");
                            botReply.setReplyMarkup(allKeyboards.get("sex"));
                            break;


                        case 4:
                            if ((messageText.compareTo("Парень") == 0) || (messageText.compareTo("Девушка") == 0)) {
                                // здеся нада записать пол человека
                                allUsers.replace(chatId, 5);
                                botReply.setText("Введи о себе:");
                                break;
                            }
                            botReply.setText("Неправильный ввод");
                            break;

                        case 5:
                            // здеся нада записать "о себе" человека
                            allUsers.replace(chatId, 6);
                            botReply.setText("Анкета готова");
                            botReply.setReplyMarkup(allKeyboards.get("mainmenu"));
                            break;
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