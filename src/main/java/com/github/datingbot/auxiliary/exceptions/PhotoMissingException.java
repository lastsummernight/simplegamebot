package com.github.datingbot.auxiliary.exceptions;

public class PhotoMissingException extends MyException {
    public PhotoMissingException() {
        super("||| MessageException: missing photo", -4);
    }
}
