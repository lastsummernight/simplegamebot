package com.github.datingbot.message;

import com.github.datingbot.auxiliary.exceptions.*;
import com.github.datingbot.keyboard.Keyboard;
import com.github.datingbot.profile.Profile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashMap;

public class MessageBuilder {

    private static HashMap<String, Message> mapOfMessages;

    public static void setUp(HashMap<String, Profile> allUsers) {
        mapOfMessages = new HashMap<>();
        if (!allUsers.isEmpty()) {
            for (String id : allUsers.keySet()) {
                mapOfMessages.put(id, new Message(id, "Проба пера"));
            }
        }
    }

    public static boolean hasPhoto(String chatId) throws MyException {
        Message temp = mapOfMessages.get(chatId);
        if (temp == null) throw new InvalidMapKeyException();
        return temp.hasPhoto();
    }

    public static SendMessage execute(String chatId) throws MyException {
        // здеся логика сборки сообщения
        Message temp = null;
        if (mapOfMessages.containsKey(chatId)) temp = mapOfMessages.get(chatId);
        else throw new InvalidMapKeyException();

        SendMessage returned = null;
        if (temp.isCorrect()) return temp.execute(chatId);
        throw new InvalidMessageException();
    }

    public static SendPhoto executeWithPhoto(String chatId) throws MyException {
        // здеся логика сборки сообщения
        Message temp = null;
        if (mapOfMessages.containsKey(chatId)) temp = mapOfMessages.get(chatId);
        else throw new InvalidMapKeyException();

        if (temp.isCorrect()) return temp.executeWithPhoto(chatId);
        throw new InvalidMessageException();
    }

    public static void usualMessage(String chatId, String text) {
        if (mapOfMessages.containsKey(chatId)) mapOfMessages.get(chatId).reset(text);
        else mapOfMessages.put(chatId, new Message(chatId, text));
    }

    public static void usualMessage(String chatId, String text, Keyboard keyboard) {
        usualMessage(chatId, text);
        mapOfMessages.get(chatId).setKeyboard(keyboard);
    }

    public static void usualMessage(String chatId, String text, Keyboard keyboard, String photoUrl) {
        usualMessage(chatId, text);
        mapOfMessages.get(chatId).setKeyboard(keyboard);
        mapOfMessages.get(chatId).setPhotoUrl(photoUrl);
    }
}
