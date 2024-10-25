package com.github.simplegamebot;

public class StringFunctions
{
    public static String reverse(String someLine)
    {
        String reversedLine = "";
        for (int i = (someLine.length() - 1) ; i>=0 ; i--){
            reversedLine += someLine.charAt(i);
        }
        return reversedLine;
    }

    public static int isNum(String someLine) {
        String allNums = "0123456789";
        int num = 0;
        for (int i = 0; i != someLine.length(); i++) {
            String temp = someLine.substring(i, i+1);
            if (!allNums.contains(temp))
                return -1;
            num *= 10;
            num += Integer.parseInt(temp);
        }
        return num;
    }
}
