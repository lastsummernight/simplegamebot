package com.github.datingbot.profile;

import com.github.datingbot.auxiliary.State;
import com.github.datingbot.auxiliary.StringFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profile {

    private String username;
    private int age;
    private String gender;
    private String city;
    private String info;
    private State userState;
    private String chatId;
    private List<String> friends;
    private State tempInfo = null;

    public Profile(String id, State state) {
        chatId = id;
        userState = state;
        friends = new ArrayList<>();
    }

    public Profile(String id, State state, String name, int age, String city, boolean gender, String info, String friends) {
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
    }

    public String getStrFriends() {
        if (friends.isEmpty())
            return "";
        return String.join(",", friends);
    }

    public List<String> getFriends() {
        return friends;
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

}
