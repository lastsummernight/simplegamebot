package com.github.datingbot;

import com.github.datingbot.profile.Profile;

import java.util.HashMap;

public class MatchMaker {

    public static HashMap<String, Profile> allUsers;

    public static void setUp(HashMap<String, Profile> users) {
        allUsers = users;
    }

}