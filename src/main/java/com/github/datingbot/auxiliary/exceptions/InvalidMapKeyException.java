package com.github.datingbot.auxiliary.exceptions;

public class InvalidMapKeyException extends MyException {
    public InvalidMapKeyException() {
        super("||| MessageBuilderException: map doesn't contain this chatId |||", -2);
    }
}