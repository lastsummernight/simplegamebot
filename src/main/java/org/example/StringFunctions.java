package org.example;

public class StringFunctions
{
    public static String reverse(String SomeLine)
    {
        String reversedLine = "";
        for (int i = (SomeLine.length()-1) ; i>=0 ; i--){
            reversedLine += SomeLine.charAt(i);
        }
        return reversedLine;
    }
}
