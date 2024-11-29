package com.github.datingbot.message;

import com.github.datingbot.keyboard.Keyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.github.datingbot.keyboard.Keyboard.*;

public class Message {

    private String message;
    private Keyboard keyboard;
    private String chatId;

    public Message(String chatId) {
        this.chatId = chatId;
        keyboard = EMPTY_KEYBOARD;
    }

    public Message(String chatId, String text) {
        this.chatId = chatId;
        message = text;
        keyboard = EMPTY_KEYBOARD;
    }

    public boolean isCorrect() {
        return message != null;
    }

    public void reset() {
        message = null;
        keyboard = EMPTY_KEYBOARD;
    }

    public SendMessage execute() {
        SendMessage temp = new SendMessage(chatId, message);
        temp.setReplyMarkup(keyboard.getKeyboardMarkup());
        return temp;
    }

    public ReplyKeyboardMarkup getKeyboard() {
        return keyboard.getKeyboardMarkup();
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        System.out.println("|||DEBUG message before: " + this.message);
        this.message = message;
        System.out.println("|||DEBUG message after: " + this.message);
    }
}
