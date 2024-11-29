package com.github.datingbot.auxiliary;

public class MyException extends Exception {
    private String traceback;

    public MyException(String text) {
        super(text);
        traceback = text;
    }

    public String returnTraceback() {
        return traceback;
    }
}