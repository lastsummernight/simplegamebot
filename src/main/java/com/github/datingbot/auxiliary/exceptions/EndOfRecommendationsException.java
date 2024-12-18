package com.github.datingbot.auxiliary.exceptions;

public class EndOfRecommendationsException extends MyException {
    public EndOfRecommendationsException() {
        super("||| MatcherException: list of recommendations is over", -3);
    }
}
