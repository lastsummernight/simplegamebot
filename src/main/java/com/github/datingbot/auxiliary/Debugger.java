package com.github.datingbot.auxiliary;

import com.github.datingbot.profile.Profile;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Debugger {

    public static void printProfile(Profile profile) throws UnsupportedEncodingException {
        PrintStream printStream = new PrintStream(System.out, true, "utf-8");

        printStream.println("{ id = " + profile.getChatId());
        printStream.println("   Name = " + profile.getUsername());
        printStream.println("   Age = " + profile.getAge());
        printStream.println("   City = " + profile.getCity());
        printStream.println("   Gender = " + profile.getGender());
        printStream.println("   Info = " + profile.getInfo());
        printStream.println("}");
    }

}
