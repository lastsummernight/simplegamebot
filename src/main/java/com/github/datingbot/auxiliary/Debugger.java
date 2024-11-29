package com.github.datingbot.auxiliary;

import com.github.datingbot.profile.Profile;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Debugger {

    private static PrintStream printStream;

    public static void setUp() {
        try {
            printStream = new PrintStream(System.out, true, "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printProfile(Profile profile) {
        printStream.println("{ id = " + profile.getChatId());
        printStream.println("   Name = " + profile.getUsername());
        printStream.println("   Age = " + profile.getAge());
        printStream.println("   City = " + profile.getCity());
        printStream.println("   Gender = " + profile.getGender());
        printStream.println("   Info = " + profile.getInfo());
        printStream.println("}");
    }

    public static void printException(MyException e) {
        printStream.println(e.returnTraceback());
    }

    public static void printException(CustomException e) {
        printStream.println(e.getTraceback());
    }

}
