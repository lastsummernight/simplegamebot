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
    private List<String> friends;
    private List<String> notLovedBy; // Пользователи которые отвергли этот профиль
    private Set<String> watchedProfiles;
    private String lastViewedProfile;
    private State tempInfo = null;

    public Profile(String id, State state) {
        chatId = id;
        userState = state;
        friends = new ArrayList<>();
        watchedProfiles = new HashSet<>();
        watchedProfiles.add(chatId);
        notLovedBy = new ArrayList<>();
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

        try {
            if (!t.get(6).equals("None"))
                this.friends = new ArrayList<>(Arrays.asList(t.get(6).split(",")));
        }
        finally {
            if (this.friends == null)
                this.friends = new ArrayList<>();
        }

        try {
            if (t.get(7) != null)
                if (!t.get(7).equals("None"))
                    this.notLovedBy = new ArrayList<>(Arrays.asList(t.get(7).split(",")));
        }
        finally {
            if (this.notLovedBy == null)
                this.notLovedBy = new ArrayList<>();
        }

        try {
            if (t.get(8) != null) {
                if (!t.get(8).equals("None")) {
                    List<String> hobbyValues = new ArrayList<>(Arrays.asList(t.get(8).split(",")));
                    userHobbies = new ArrayList<>();
                    for (String value : hobbyValues) {
                        userHobbies.add(Hobbies.getHobbyBySpecificValue(value));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (this.userHobbies == null)
                this.userHobbies = new ArrayList<>();
        }

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

    public String getStrUserHobbies() {
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