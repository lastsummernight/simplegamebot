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
        }
        allChatIds.removeAll(notRecomendedMatches);
        List<String> notUsedMatches = new ArrayList<String>(allChatIds);
        profile.deleteWatchedProfiles();
        returningProfile = allUsers.get(notUsedMatches.getFirst());
        return returningProfile;
    }

    public static Integer ValueFunction(Profile seeker, Profile target){
        //Оценивает профиль исходя из предпочтений пользователя
        //Сейчас не важно
        return 1;
    }
}
