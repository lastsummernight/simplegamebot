package com.github.datingbot.auxiliary;

import java.util.List;

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
        if (age % 10 == 1)
            return age + " год";
        else if (age % 10 > 1 && age % 10 < 5)
            return age + " года";
        return age + " лет";
    }

    public static String formatFriends(List<String> temp) {
        if (temp.size() == 0)
            return "";
        StringBuilder returned = new StringBuilder();
        for (int i = 0; i < temp.size(); i++) {
            returned.append(i + 1);
            returned.append("). ");
            returned.append(temp.get(i));
            returned.append("\n");
        }
        return returned.toString();
    }
}
