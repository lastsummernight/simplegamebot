package com.github.datingbot.message;

import com.github.datingbot.auxiliary.exceptions.PhotoMissingException;
import com.github.datingbot.keyboard.Keyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;

import static com.github.datingbot.keyboard.Keyboard.*;

public class Message {

    private String message;
    private Keyboard keyboard;
    private String chatId;
    private String photoUrl;

    public Message(String chatId, String text) {
        this.chatId = chatId;
        message = text;
        keyboard = EMPTY_KEYBOARD;
    }

    public boolean isCorrect() {
        return message != null;
    }

    public void reset(String message) {
        this.message = message;
        keyboard = EMPTY_KEYBOARD;
        photoUrl = null;
    }

    public SendMessage execute(String chatId) {
        SendMessage returned = new SendMessage(chatId, message);
        returned.setReplyMarkup(keyboard.getKeyboardMarkup());
        return returned;
    }

    public SendPhoto executeWithPhoto(String chatId) throws PhotoMissingException {
        try {
            InputFile inputFile = new InputFile();
            inputFile.setMedia(new File(photoUrl));
            SendPhoto returned = new SendPhoto(chatId, inputFile);
            returned.setCaption(message);
            returned.setReplyMarkup(keyboard.getKeyboardMarkup());
            return returned;
        } catch (Exception e) {
            throw new PhotoMissingException();
        }
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean hasPhoto() {
        if (photoUrl != null)
            return true;
        return false;
    }
}
