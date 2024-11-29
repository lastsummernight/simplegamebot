package com.github.datingbot.message;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.auxiliary.MyException;
import com.github.datingbot.keyboard.Keyboard;
import com.github.datingbot.profile.Profile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.HashMap;

import static com.github.datingbot.auxiliary.CustomException.*;

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

    public static void createMessage(String chatId) {
        if (mapOfMessages.containsKey(chatId)) mapOfMessages.get(chatId).reset();
        else mapOfMessages.put(chatId, new Message(chatId));
    }

    public static void setText(String chatId, String text) {
        if (mapOfMessages.containsKey(chatId)) mapOfMessages.get(chatId).setMessage(text);
        else Debugger.printException(MBUILDERMAPCONTAINSKEY);
    }

    public static void setKeyboard(String chatId, Keyboard keyboard) {
        if (mapOfMessages.containsKey(chatId)) mapOfMessages.get(chatId).setKeyboard(keyboard);
        else Debugger.printException(MBUILDERMAPCONTAINSKEY);
    }

    public static SendMessage execute(String chatId) throws MyException {
        // здеся логика сборки сообщения
        Message temp = null;
        if (mapOfMessages.containsKey(chatId)) temp = mapOfMessages.get(chatId);
        else throw MBUILDERMAPCONTAINSKEY.getException();
        SendMessage returned = null;
        if (temp.isCorrect()) returned = temp.execute();
        else throw MESSAGEISCORRECT.getException();

        return returned;
    }

    public static SendMessage usualMessage(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        return message;
    }

    public static SendMessage usualMessage(String chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = usualMessage(chatId, text);
        message.setReplyMarkup(keyboard);
        return message;
    }

}
