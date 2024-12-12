package com.github.datingbot.auxiliary.exceptions;

public class MyException extends Exception {
    private String traceback;
    private int code;

    public MyException(String text, int code) {
        super(text);
        traceback = text;
        this.code = code;
    }

    public String returnTraceback() {
        return traceback;
    }

    public String returnCode() {
        return traceback;
    }
}