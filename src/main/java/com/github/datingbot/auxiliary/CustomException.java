package com.github.datingbot.auxiliary;

public enum CustomException {

    MESSAGEISCORRECT(new MyException("||| MessageBuilderException: created message invalid |||")),
    MBUILDERMAPCONTAINSKEY(new MyException("||| MessageBuilderException: map doesn't contain this chatId |||"));

    private MyException exception;

    private CustomException(MyException e) {
        exception = e;
    }

    public MyException getException() {
        return exception;
    }

    public String getTraceback() {
        return exception.returnTraceback();
    }

}