package com.github.datingbot.profile;

import com.github.datingbot.auxiliary.Hobbies;
import com.github.datingbot.auxiliary.State;
import com.github.datingbot.auxiliary.StringFunctions;

import java.util.*;

public class Profile {

    private State userState;
    private String chatId;
    private String username;
    private int age;
    private String gender;
    private String city;
    private String info;

    private List<Hobbies> userHobbies;
    private Set<String> friends;
    private Set<String> notLovedBy; // Пользователи которые отвергли этот профиль
    private Set<String> watchedProfiles;
    private String lastViewedProfile;
    private State tempInfo = null;
    private int flagForMarks;


    public Profile(String id, State state) {
        chatId = id;
        userState = state;
        friends = new HashSet<>();
        watchedProfiles = new HashSet<>();
        watchedProfiles.add(chatId);
        notLovedBy = new HashSet<>();
        notLovedBy.add(chatId);
        userHobbies = new ArrayList<>();
    }

    public Profile(List<String> t) {
        chatId = t.get(0);
        userState = State.USER_STATE_MAIN_MENU;
        username = t.get(1);
        this.age = Integer.parseInt(t.get(2));
        this.city = t.get(3);

        if (t.get(4).equals("1"))
            this.gender = "Парень";
        else
            this.gender = "Девушка";

        this.info = t.get(5);

        if (!t.get(6).equals("None")) {
            friends = new HashSet<>(Arrays.asList(t.get(6).split(",")));
        }
        else
            friends = new HashSet<>();

        if (!t.get(7).equals("None")) {
            notLovedBy = new HashSet<>(Arrays.asList(t.get(7).split(",")));
        }
        else
            notLovedBy = new HashSet<>();

        if (!t.get(8).equals("None")) {
            List<String> hobbyValues = new ArrayList<>(Arrays.asList(t.get(8).split(",")));
            userHobbies = new ArrayList<>();
            for (String value : hobbyValues) {
                userHobbies.add(Hobbies.getHobbyBySpecificValue(value));
            }
        }
        else
            userHobbies = new ArrayList<>();
    }

    public String getStrHobbies() {
        String result = "Твои интересы: \n";
        if (userHobbies.isEmpty())
            return "Нет хобби";
        for (Hobbies temp : userHobbies) {
            result += temp.getTitle() + "\n";
        }
        return result;
    }

    public String getStrHobbiesDB() {
        if (userHobbies.isEmpty()) {
            return "None";
        }
        String result = "";
        for (Hobbies temp : userHobbies) {
            result += temp.getSpecificValue()+",";
        }
        return result;
    }

    public List<Hobbies> getUserHobbies() {
        return userHobbies;
    }

    public void addHobby(Hobbies hobby) {
        userHobbies.add(hobby);
    }

    public void deleteHobby(Hobbies hobby) {
        if (userHobbies.contains(hobby))
            userHobbies.remove(hobby);
    }

    public String getStrFriendsDB() {
        if (friends.isEmpty())
            return "None";
        return String.join(",", friends);
    }

    public String getStrFriends() {
        if (friends.isEmpty())
            return "";
        return StringFunctions.formatFriends(friends.stream().toList());
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void addFriend(String anothersUserChatId) {
        if (!(friends.contains(anothersUserChatId))) {
            friends.add(anothersUserChatId);
        }
    }

    public void deleteFriend(String anothersUserChatId) {
        if (friends.contains(anothersUserChatId)) {
            friends.remove(anothersUserChatId);
        }
    }

    public void deleteFriend(int idx) {
        List<String> temp = friends.stream().toList();
        try {
            System.out.println("||| temp.get(idx) = " + temp.get(idx - 1));
            friends.remove(temp.get(idx - 1));
        }
        catch (Exception e) {
            System.out.println("||| Exception in <profile.deleteFriend>:\n" + e);
        }
    }

    public String getStrNotLovedByDB() {
        if (notLovedBy.isEmpty())
            return "None";
        return String.join(",", notLovedBy);
    }

    public String getStrNotLovedBy() {
        if (notLovedBy.isEmpty())
            return "";
        System.out.println(notLovedBy);
        return StringFunctions.formatFriends(notLovedBy.stream().toList());
    }

    public Set<String> getNotLovedBy() {
        return notLovedBy;
    }

    public void addNotLovedBy(String anothersUserChatId) {
        notLovedBy.add(anothersUserChatId);
    }

    public void deleteNotLovedBy(String chatId) {
        if (notLovedBy.contains(chatId))
            notLovedBy.remove(chatId);
    }

    public void deleteNotLovedBy(int idx) {
        List<String> temp = notLovedBy.stream().toList();
        try {
            System.out.println("||| temp.get(idx) = " + temp.get(idx - 1));
            notLovedBy.remove(temp.get(idx - 1));
        }
        catch (Exception e) {
            System.out.println("||| Exception in <profile.deleteNotLovedBy>:\n" + e);
        }
    }

    public void deleteAllNotLovedBy() {
        notLovedBy.clear();
        notLovedBy.add(chatId);
    }

    public int getFlagForMarks() {
        return flagForMarks;
    }

    public void setFlagForMarks(int flagForMarks) {
        this.flagForMarks = flagForMarks;
    }

    public void setTempInfo(State temp) {
        tempInfo = temp;
    }

    public State getTempInfo() {
        return tempInfo;
    }

    public String getStr() {
        return username + ' ' + StringFunctions.yearsOld(age) + " (" + gender + ")\nГород: " + city + "\n" + info;
    }

    public String getChatId() {
        return chatId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public State getUserState() {
        return userState;
    }

    public void setUserState(State userState) {
        this.userState = userState;
    }

    public Set<String> getWatchedProfiles() {
        if (watchedProfiles == null) {
            watchedProfiles = new HashSet<>();
            watchedProfiles.add(chatId);
        }
        return watchedProfiles;
    }

    public void addWatchedProfile(String anothersUserChatId) {
        watchedProfiles.add(anothersUserChatId);
    }

    public String getLastViewedProfile() {
        return lastViewedProfile;
    }

    public void setLastViewedProfile(String lastViewedProfile) {
        this.lastViewedProfile = lastViewedProfile;
    }

    public void deleteWatchedProfiles() {
        watchedProfiles.clear();
        watchedProfiles.add(chatId);
    }
}