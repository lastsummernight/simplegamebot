package com.github.datingbot.matching;

import com.github.datingbot.profile.Profile;

import java.util.*;
import java.util.stream.Collectors;


public class Matcher {

    public static Profile findAnotherPerson(Profile profile, HashMap<String, Profile> allUsers){
        Profile returningProfile;
        Set<String> allChatIds = allUsers.keySet().stream().collect(Collectors.toSet());
        Set<String> notRecomendedMatches = new HashSet<>();
        notRecomendedMatches.addAll(profile.getWatchedProfiles());
        notRecomendedMatches.addAll(profile.getNotLovedBy());
        if (notRecomendedMatches.containsAll(allChatIds)){
            //Если закончились все подходящие матчи
            //должны предложить пользователю начать смотреть анкеты с самого начала?
            //Пока что будем делать это насильно
            System.out.println("empty");
            notRecomendedMatches.clear();
            profile.deleteWatchedProfiles();
            profile.deleteNotLovedBy();
        }
        allChatIds.removeAll(notRecomendedMatches);
        List<String> notUsedMatches = new ArrayList<String>(allChatIds);
        for (String chatId : notUsedMatches) {
            Profile tempProfile = allUsers.get(chatId);
            if (ValueFunction(profile, tempProfile) > 0)
                return allUsers.get(chatId);
            else
                profile.addWatchedProfile(tempProfile.getChatId());
        }
        return findAnotherPerson(profile, allUsers);
    }

    public static int ValueFunction(Profile seeker, Profile target){
        if (seeker.getGender().equals(target.getGender()))
            return 0;
        return 1;
    }
}