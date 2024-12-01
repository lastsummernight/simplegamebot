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

    public static String yearsOld(int age) {
        if (age % 10 == 1) return age + " год";
        else if (age % 10 > 1 && age % 10 < 5) return age + " года";
        else return age + " лет";
    }
}
