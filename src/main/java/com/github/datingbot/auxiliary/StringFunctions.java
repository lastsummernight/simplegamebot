package com.github.datingbot.auxiliary;

public class StringFunctions
{
    public static int isNum(String someLine) {
        try {
            return Integer.parseInt(someLine);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}
