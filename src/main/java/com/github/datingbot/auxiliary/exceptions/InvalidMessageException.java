package com.github.datingbot.auxiliary.exceptions;

public class InvalidMessageException extends MyException {
    public InvalidMessageException() {
        super("||| MessageBuilderException: created message invalid |||", -1);
    }
}