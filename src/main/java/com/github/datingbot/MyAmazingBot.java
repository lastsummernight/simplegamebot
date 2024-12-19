package com.github.datingbot;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.auxiliary.Hobbies;
import com.github.datingbot.auxiliary.exceptions.EndOfRecommendationsException;
import com.github.datingbot.auxiliary.exceptions.MyException;
import com.github.datingbot.auxiliary.State;
import com.github.datingbot.auxiliary.StringFunctions;
import com.github.datingbot.database.DatabaseManager;
import com.github.datingbot.matching.Matcher;
import com.github.datingbot.message.MessageBuilder;
import com.github.datingbot.profile.Profile;
import com.github.datingbot.profile.ProfileManager;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

import static com.github.datingbot.auxiliary.Debugger.printProfile;
import static com.github.datingbot.auxiliary.Hobbies.*;
import static com.github.datingbot.auxiliary.State.*;
import static com.github.datingbot.keyboard.Keyboard.*;

public class MyAmazingBot implements LongPollingSingleThreadUpdateConsumer {
    private static final String BROKEN_HEART ="\uD83D\uDC94" ;
    private TelegramClient telegramClient;
    public HashMap<String, Profile> allUsers;
    private HashMap<String, State> servicePrompts;
    private HashMap<String, Hobbies> allHobbies;
    private List<State> registrationStates;
    private Set<String> allChatIds;
    public MyAmazingBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
        allUsers = DatabaseManager.getAllUsers();
        servicePrompts = buildMapStates(Arrays.asList(USER_NAME, USER_AGE, USER_CITY, USER_GENDER, USER_INFO, USER_STATE_MAIN_MENU));
        registrationStates = Arrays.asList(USER_NAME, USER_AGE, USER_CITY, USER_GENDER, USER_INFO);
        allHobbies = buildMapHobbies(Arrays.asList(LITERATURE_HOBBY, DANCE_HOBBY, VIDEOGAMES_HOBBY, SCIENCE_HOBBY, SPORT_HOBBY, MUSIC_HOBBY, COOKING_HOBBY, TRAVELLING_HOBBY, ART_HOBBY));
        Debugger.setUp();
        MessageBuilder.setUp(allUsers);
    }

    private HashMap<String, State> buildMapStates(List<State> enums) {
        HashMap<String, State> result = new HashMap<>();
        for (State temp : enums) {
            result.put(temp.getTitle(), temp);
        }
        return result;
    }

    private HashMap<String, Hobbies> buildMapHobbies(List<Hobbies> enums) {
        HashMap<String, Hobbies> result = new HashMap<>();
        for (Hobbies temp : enums) {
            result.put(temp.getTitle(), temp);
        }
        return result;
    }

    private void newUserFunction(String chatId, String messageText) {
        System.out.println("||| BEGIN OF REGISTER");
        allUsers.put(chatId, new Profile(chatId, USER_NAME));
        MessageBuilder.usualMessage(chatId, "Давай заполним Твою анкету\nВведи имя:");
    }

    private void viewProfileFunction(String chatId, String messageText) {
        System.out.println("||| IN ANKETA");
        Profile currentUser = allUsers.get(chatId);
        if (messageText.equals("Назад")) {
            System.out.println("||| from <anketa> to <main menu>");
            currentUser.setUserState(USER_STATE_MAIN_MENU);
            MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD);
        }

        else if (messageText.equals("Изменить профиль")) {
            System.out.println("||| CHANGE ANKETA");
            currentUser.setUserState(USER_PROFILE_CHANGE);
            currentUser.setTempInfo(EMPTY);
            MessageBuilder.usualMessage(chatId, currentUser.getStr() + "Выбери, что хочешь изменить", PROFILE_CHANGE_KEYBOARD);
        }

        else if (messageText.equals("Изменить интересы")) {
            System.out.println("||| CHANGE HOBBIES");
            currentUser.setUserState(USER_PROFILE_HOBBIES);
            MessageBuilder.usualMessage(chatId, currentUser.getStrHobbies() + "\nВыбери, что хочешь добавить или убрать", HOBBY_KEYBOARD);
        }

        else {
            MessageBuilder.usualMessage(chatId, "Неправильный ввод", VIEW_PROFILE_KEYBOARD);
            System.out.println("||| DEFAULT viewProfileFunction");
        }
    }

    private void changeProfileFunction(String chatId, Message userMessage) {
        Profile currentUser = allUsers.get(chatId);
        String messageText = userMessage.getText();
        if (messageText.equals("Назад")) {
            System.out.println("||| from <changing anketa> to <view profile>");
            currentUser.setTempInfo(EMPTY);
            currentUser.setUserState(USER_PROFILE);
            MessageBuilder.usualMessage(chatId, currentUser.getStr(), VIEW_PROFILE_KEYBOARD);
        }
        else {
            if (servicePrompts.containsKey(messageText)) {
                System.out.println("||| CHANGING ANKETA: " + messageText);
                currentUser.setTempInfo(servicePrompts.get(messageText));
                if (messageText.equals("Пол"))
                    MessageBuilder.usualMessage(chatId, "Выбери пол:", GENDER_KEYBOARD);
                else MessageBuilder.usualMessage(chatId, "Введи " + messageText.toLowerCase() + ':');
            }
            else {
                System.out.println("||| DEFAULT");
                ProfileManager.useProfile(allUsers.get(chatId), userMessage); // MESSAGE ???
                if (currentUser.getTempInfo() == EMPTY)
                    ProfileManager.emptyState(currentUser); // MESSAGE BY DEFAULT ???
            }
        }
    }

    private void changeHobbiesFunction(String chatId, Message userMessage) {
        Profile currentUser = allUsers.get(chatId);
        String message = userMessage.getText();

        if (message.equals("Назад")) {
            System.out.println("||| from <changing hobby> to <view profile>");
            currentUser.setUserState(USER_PROFILE);
            MessageBuilder.usualMessage(chatId, currentUser.getStrHobbies(), VIEW_PROFILE_KEYBOARD);
            DatabaseManager.changeUser(currentUser);
        }
        else {
            Hobbies temp = allHobbies.get(message);
            if (temp != null) {
                System.out.println("||| CHANGING HOBBIE: " + message);
                if (currentUser.getUserHobbies().contains(temp)) {
                    currentUser.deleteHobby(temp);
                    MessageBuilder.usualMessage(chatId, message + " было удалено", HOBBY_KEYBOARD);
                }
                else {
                    currentUser.addHobby(temp);
                    MessageBuilder.usualMessage(chatId, message + " было добавлено", HOBBY_KEYBOARD);
                }
            }
            else {
                System.out.println("||| DEFAULT");
                MessageBuilder.usualMessage(chatId, "Выберите команду с клавиатуры", HOBBY_KEYBOARD);
            }
        }
    }

    private void registrationFunction(String chatId, Message userMessage) {
        Profile currentUser = allUsers.get(chatId);
        System.out.println("||| +REGISTRATION " + chatId + ' ' + currentUser.getUserState());
        ProfileManager.changeProfileLocal(userMessage, currentUser); // REGISTRATION WITH MESSAGES
    }

    private void findingFunction(String chatId, String messageText) {
        allChatIds = new HashSet<String>(allUsers.keySet());
        Profile currentUser = allUsers.get(chatId);
        if (messageText.equals("Назад")) {
            currentUser.setUserState(USER_STATE_MAIN_MENU);
            DatabaseManager.changeUser(currentUser);
            MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD);
        }
        else if (messageText.equals("♥")) {
            currentUser.addWatchedProfile(currentUser.getLastViewedProfile());
            currentUser.addFriend(currentUser.getLastViewedProfile());
            try {
                Profile FoundedMatch = Matcher.findAnotherPerson(currentUser, allUsers);
                MessageBuilder.usualMessage(chatId, FoundedMatch.getStr());
                currentUser.setLastViewedProfile(FoundedMatch.getChatId());
            }
            catch (EndOfRecommendationsException e) {
                currentUser.setUserState(USER_STATE_MAIN_MENU);
                DatabaseManager.changeUser(currentUser);
                MessageBuilder.usualMessage(chatId, "Простите в данный момент подходящих профилей нет :(", MAIN_MENU_KEYBOARD);
            }
        }
        else if (messageText.equals(BROKEN_HEART)) {
            currentUser.addWatchedProfile(currentUser.getLastViewedProfile());
            currentUser.addNotLoved(currentUser.getLastViewedProfile());
            allUsers.get(currentUser.getLastViewedProfile()).addNotLovedBy(chatId);
            //этот сценарий возможен лишь при повторном пробеге по всем пользователям
//            currentUser.deleteFriend(currentUser.getLastViewedProfile());
            // TODO: стоит сделать кнопку которая будет отвечать за удаление друзей, но не срочно.
            try {
                Profile FoundedMatch = Matcher.findAnotherPerson(currentUser, allUsers);
                MessageBuilder.usualMessage(chatId, FoundedMatch.getStr());
                currentUser.setLastViewedProfile(FoundedMatch.getChatId());
            }
            catch (EndOfRecommendationsException e) {
                currentUser.setUserState(USER_STATE_MAIN_MENU);
                DatabaseManager.changeUser(currentUser);
                MessageBuilder.usualMessage(chatId, "Простите в данный момент подходящих профилей нет :(", MAIN_MENU_KEYBOARD);
            }
        }
        else MessageBuilder.usualMessage(chatId, "something wrong", MAIN_MENU_KEYBOARD);
    }

    private void connectionMenuFunction(String chatId, String messageText) {
        Profile currentUser = allUsers.get(chatId);

        if (messageText.equals("Назад")) {
            System.out.println("||| from <connection menu> to <main menu>");
            currentUser.setUserState(USER_STATE_MAIN_MENU);
            MessageBuilder.usualMessage(chatId, "Выберите команду на клавиатуре", MAIN_MENU_KEYBOARD);
        }

        else if (messageText.equals("Изменить оценку")) {
            currentUser.setUserState(USER_MARKS);
            MessageBuilder.usualMessage(chatId, "Из какой категории: ", MARKS_KEYBOARD);
        }

        else if (messageText.equals("Запросы")) {
            List <String> MatchedUsers = new ArrayList<>();
            List <String> tempSet = currentUser.getFriends();
            for (String anotherProfile : tempSet){
                if (allUsers.get(anotherProfile).getFriends().contains(chatId)){
                    MatchedUsers.add(anotherProfile);
                }
            }
            MessageBuilder.usualMessage(chatId, "Не реализованно", CONNECTIONS_KEYBOARD);
        }

        else  {
            System.out.println("||| DEFAULT");
            MessageBuilder.usualMessage(chatId, "Выберите команду с клавиатуры", CONNECTIONS_KEYBOARD);
        }
    }

    private void marksMenuFunction(String chatId, String messageText) {
        Profile currentUser = allUsers.get(chatId);
        int currentUserToDo = StringFunctions.isNum(messageText);

        if (messageText.equals("Назад")) {
            System.out.println("||| from <marks menu> to <main menu>");
            currentUser.setUserState(USER_CONNECTIONS);
            currentUser.setFlagForMarks(0);
            MessageBuilder.usualMessage(chatId, "Люди, которые вам не понравились: \n" +
                    currentUser.getStrProfilesNotLoved(allUsers) + "\nЛюди, которые вам понравились: \n" +
                    currentUser.getStrProfilesFriends(allUsers) + "\nВыберите команду на клавиатуре", CONNECTIONS_KEYBOARD);
            DatabaseManager.changeUser(currentUser);
        }

        else if (messageText.equals("Понравившиеся")) {
            currentUser.setFlagForMarks(1);
            MessageBuilder.usualMessage(chatId, "Выбери порядковый номер человека: ", EMPTY_KEYBOARD);
        }

        else if (messageText.equals("Не понравившиеся")) {
            currentUser.setFlagForMarks(-1);
            MessageBuilder.usualMessage(chatId, "Выбери порядковый номер человека: ", EMPTY_KEYBOARD);
        }

        else if (currentUserToDo != -1) {
            if (currentUser.getFlagForMarks() == 1) {
                currentUser.deleteFriend(currentUserToDo);
            }
            else if (currentUser.getFlagForMarks() == -1) {
                currentUser.deleteNotLoved(currentUserToDo);
            }
        }

        else  {
            System.out.println("||| DEFAULT");
            MessageBuilder.usualMessage(chatId, "Выберите команду с клавиатуры", MARKS_KEYBOARD);
        }
    }

    private void mainMenuFunction(String chatId, String messageText) {
        // CHANGES STATE TO USER_PROFILE + EXTRA INFO
        Profile currentUser = allUsers.get(chatId);
        if (messageText.equals("Моя анкета")) {
            currentUser.setUserState(USER_PROFILE);
            MessageBuilder.usualMessage(chatId, currentUser.getStr(), VIEW_PROFILE_KEYBOARD);
        }

        // CHANGES STATE TO USER_STATE_FINDING
        else if (messageText.equals("Поиск")) {
            try {
                Profile FoundedMatch = Matcher.findAnotherPerson(currentUser, allUsers);
                MessageBuilder.usualMessage(chatId, FoundedMatch.getStr());
                currentUser.setUserState(USER_STATE_FINDING);
                currentUser.setLastViewedProfile(FoundedMatch.getChatId());
                MessageBuilder.usualMessage(chatId, FoundedMatch.getStr(), FINDING_KEYBOARD);
            }
            catch (EndOfRecommendationsException e) {
                MessageBuilder.usualMessage(chatId, "Простите в данный момент подходящих профилей нет :(", MAIN_MENU_KEYBOARD);
            }
        }

        else if (messageText.equals("Анкеты")) {
            currentUser.setUserState(USER_CONNECTIONS);
            MessageBuilder.usualMessage(chatId, "Люди, которые вам не понравились: \n" +
                    currentUser.getStrProfilesNotLoved(allUsers) + "\nЛюди, которые вам понравились: \n" +
                    currentUser.getStrProfilesFriends(allUsers) + "\nВыберите команду на клавиатуре", CONNECTIONS_KEYBOARD);
        }

        // UNKNOWN MESSAGE
        else MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD);
        printProfile(currentUser);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message userMessage = update.getMessage();
            String messageText = userMessage.getText();
            String chatId = userMessage.getChatId().toString();
            SendMessage botAnswer = null;
            Profile currentUser = allUsers.get(chatId);

            // FOR NEW USERS
            if (currentUser == null) {
                newUserFunction(chatId, messageText);
            }

            else {
                // USER WITH STATE REGISTRATION
                System.out.println("||| " + chatId + " USERSTATE: " + currentUser.getUserState());
                if (registrationStates.contains(currentUser.getUserState())) {
                    registrationFunction(chatId, userMessage);
                }
                else {
                    switch (currentUser.getUserState()) {
                        case USER_PROFILE -> viewProfileFunction(chatId, messageText);
                        case USER_PROFILE_CHANGE -> changeProfileFunction(chatId, userMessage);
                        case USER_PROFILE_HOBBIES -> changeHobbiesFunction(chatId, userMessage);
                        case USER_STATE_FINDING -> findingFunction(chatId, messageText);
                        case USER_CONNECTIONS -> connectionMenuFunction(chatId, messageText);
                        case USER_MARKS -> marksMenuFunction(chatId, messageText);
                        default -> mainMenuFunction(chatId, messageText);
                    }
                }
            }

            try {
                botAnswer = MessageBuilder.execute(chatId);
            }
            catch (MyException e) {
                Debugger.printException(e);
                botAnswer = new SendMessage(chatId, "Exception caused");
            }

            try {
                telegramClient.execute(botAnswer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}