package org.example;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private ReplyKeyboardMarkup replyKeyboardMarkupPlay;
    private ReplyKeyboardMarkup replyKeyboardMarkupStart;

    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);

        keyboardBuilder BUILDER = new keyboardBuilder();

        List<String> array = new ArrayList<String>();
        array.add("Играть");
        array.add("Лидерборд");
        array.add("Настройки");
        array.add("Все");

        replyKeyboardMarkupStart = BUILDER.buildKeyboard(array);
        replyKeyboardMarkupPlay = BUILDER.buildKeyboard();

        GameMaster proba = new GameMaster();
        System.out.println(proba.GameEnd(0));

    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message userMessage = update.getMessage();
            String message_text = userMessage.getText();
            String chat_id = userMessage.getChatId().toString();

            SendMessage botReply = new SendMessage(chat_id, message_text);

            if (message_text.contains("start")) {
                botReply.setReplyMarkup(replyKeyboardMarkupStart);
            }

            else {
                botReply.setReplyMarkup(replyKeyboardMarkupPlay);
            }

            if (message_text.contains("Согласие на ничью")) {
                botReply.setText(StringFunctions.reverse(message_text));
            }

            else if (message_text.contains("Предложить ничью")) {
                botReply.setMessageEffectId("5104841245755180586");
            }

            else if (message_text.contains("Сдаться")) {
                botReply.setReplyMarkup(replyKeyboardMarkupStart);
            }

            else {
                System.out.println(message_text);
            }

            try {
                telegramClient.execute(botReply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}