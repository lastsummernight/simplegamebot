package com.github.datingbot.profile;

import com.github.datingbot.auxiliary.State;
import com.github.datingbot.auxiliary.StringFunctions;

import java.util.*;

public class Profile {

    private String username;
    private int age;
    private String gender;
    private String city;
    private String info;
    private State userState;
    private String chatId;
    private List<String> friends;

    private Set<String> watchedProfiles;

    private List<String> notLovedBy; //Пользователи которые отвергли этот профиль

    private String lastViewedProfile;

    private State tempInfo = null;

    public Profile(String id, State state) {
        chatId = id;
        userState = state;
        friends = new ArrayList<>();
        watchedProfiles = new HashSet<>();
        watchedProfiles.add(chatId);
        notLovedBy = new ArrayList<>();
    }

    public Profile(String id, State state, String name, int age, String city, boolean gender, String info, String friends, String notFriends) {
        chatId = id;
        userState = state;
        username = name;
        this.age = age;
        this.city = city;
        if (gender)
            this.gender = "Парень";
        else
            this.gender = "Девушка";
        this.info = info;

        if (friends != null)
            this.friends = new ArrayList<>(Arrays.asList(friends.split(",")));
        else
            this.friends = new ArrayList<>();

        if (notFriends != null)
            this.notLovedBy = new ArrayList<>(Arrays.asList(friends.split(",")));
        else
            this.notLovedBy = new ArrayList<>();
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

        if (t.get(6) != null || !t.get(6).equals("None"))
            this.friends = new ArrayList<>(Arrays.asList(t.get(6).split(",")));
        else
            this.friends = new ArrayList<>();

        if (t.get(7) != null) {
            if (!t.get(7).equals("None"))
                this.notLovedBy = new ArrayList<>(Arrays.asList(t.get(7).split(",")));
            else
                this.notLovedBy = new ArrayList<>();
        }
        else
            this.notLovedBy = new ArrayList<>();
    }

    public String getStrFriends() {
        if (friends.isEmpty())
            return "";
        return String.join(",", friends);
    }

    public List<String> getFriends() {
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

    public String getStrNotFriends() {
        if (notLovedBy.isEmpty())
            return "";
        return String.join(",", notLovedBy);
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

    public List<String> getNotLovedBy() {
        if (notLovedBy == null) {
            notLovedBy = new ArrayList<>();
            notLovedBy.add(chatId);
        }
        return notLovedBy;
    }

    public void addNotLovedBy(String anothersUserChatId) {
        notLovedBy.add(anothersUserChatId);
    }

    public void deleteNotLovedBy() {
        notLovedBy.clear();
    }
}