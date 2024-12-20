package com.github.datingbot.matching;

import com.github.datingbot.auxiliary.Hobbies;
import com.github.datingbot.auxiliary.exceptions.EndOfRecommendationsException;
import com.github.datingbot.profile.Profile;

import java.util.*;
import java.util.stream.Collectors;

public class Matcher {

    public static Profile findAnotherPerson(Profile profile, HashMap<String, Profile> allUsers) throws EndOfRecommendationsException {
        Set<String> allChatIds = allUsers.keySet().stream().collect(Collectors.toSet());

        Set<String> notRecomendedMatches = new HashSet<>();
        notRecomendedMatches.addAll(profile.getWatchedProfiles());
        notRecomendedMatches.addAll(profile.getFriends());
        notRecomendedMatches.addAll(profile.getNotLovedBy());

        allChatIds.removeAll(notRecomendedMatches);

        if (allChatIds.isEmpty()) {
            System.out.println("||| EMPTY LIST OF RECS");
            throw new EndOfRecommendationsException();
        }

        int maxValue = 0;
        Profile resultProfile = null;
        for (String chatId : allChatIds) {
            Profile tempProfile = allUsers.get(chatId);

            int valFunc = ValueFunction(profile, tempProfile);
            if (valFunc > 0) {
                if (valFunc > maxValue) {
                    resultProfile = tempProfile;
                    maxValue = valFunc;
                }
            } else
                profile.addWatchedProfile(tempProfile.getChatId());
        }

        if (maxValue == 0)
            throw new EndOfRecommendationsException();

        return resultProfile;
    }

    public static int ValueFunction(Profile seeker, Profile target) {
        if (seeker.getGender().equals(target.getGender()))
            return 0;

        int value = 1;
        List<Hobbies> seekerHobbies = seeker.getUserHobbies();
        for (Hobbies hobby : target.getUserHobbies()) {
            if (seekerHobbies.contains(hobby))
                value += 1;
        }
        System.out.println("||| value function resulted in: " + value);
        return value;
    }
}