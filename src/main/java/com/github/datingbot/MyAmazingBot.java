package com.github.datingbot;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.auxiliary.Hobbies;
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
        System.out.println("begin of register");
        allUsers.put(chatId, new Profile(chatId, USER_NAME));
        MessageBuilder.usualMessage(chatId, "Давай заполним Твою анкету\nВведи имя:"); // MESSAGE
    }

    private void viewProfileFunction(String chatId, String messageText) {
        System.out.println("in anketa");
        Profile currentUser = allUsers.get(chatId);
        if (messageText.equals("Назад")) {
            System.out.println("from anketa to main menu");
            currentUser.setUserState(USER_STATE_MAIN_MENU);
            MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
        }

        else if (messageText.equals("Изменить профиль")) {
            System.out.println("change anketa");
            currentUser.setUserState(USER_PROFILE_CHANGE);
            currentUser.setTempInfo(EMPTY);
            //ProfileManager.useProfile(currentUser, userMessage); // MESSAGE ???
            MessageBuilder.usualMessage(chatId, "Выбери, что хочешь изменить", PROFILE_CHANGE_KEYBOARD);
        }

        else if (messageText.equals("Изменить интересы")) {
            System.out.println("change hobbies");
            currentUser.setUserState(USER_PROFILE_HOBBIES);
            MessageBuilder.usualMessage(chatId, currentUser.getStrHobbies() + "\nВыбери, что хочешь добавить или убрать", HOBBY_KEYBOARD);
        }

        else {
            MessageBuilder.usualMessage(chatId, "Неправильный ввод", VIEW_PROFILE_KEYBOARD);
            System.out.println("bad input");
        }
    }

    private void changeProfileFunction(String chatId, Message userMessage) {
        // служебные | контекстная
        Profile currentUser = allUsers.get(chatId);
        String messageText = userMessage.getText();
        if (messageText.equals("Назад")) {
            System.out.println("from changing anketa to view profile");
            currentUser.setTempInfo(EMPTY);
            currentUser.setUserState(USER_PROFILE);
            MessageBuilder.usualMessage(chatId, currentUser.getStr(), VIEW_PROFILE_KEYBOARD);
        }
        else {
            if (servicePrompts.containsKey(messageText)) {
                System.out.println("success changing anketa");
                currentUser.setTempInfo(servicePrompts.get(messageText));
                if (messageText.equals("Пол"))
                    MessageBuilder.usualMessage(chatId, "Выбери пол :", GENDER_KEYBOARD); // MESSAGE
                else MessageBuilder.usualMessage(chatId, "Введи " + messageText.toLowerCase() + ':'); // MESSAGE
            }
            else {
                System.out.println("default");
                ProfileManager.useProfile(allUsers.get(chatId), userMessage); // MESSAGE ???
                if (currentUser.getTempInfo() == EMPTY)
                    ProfileManager.emptyState(currentUser); // MESSAGE BY DEFAULT ???
            }
        }
    }

    private void changeHobbiesFunction(String chatId, Message userMessage) {
        // служебные | контекстная
        Profile currentUser = allUsers.get(chatId);
        String messageText = userMessage.getText();
        if (messageText.equals("Назад")) {
            System.out.println("from changing hobby to view profile");
            currentUser.setUserState(USER_PROFILE);
            MessageBuilder.usualMessage(chatId, currentUser.getStrHobbies(), VIEW_PROFILE_KEYBOARD);
            DatabaseManager.changeUser(currentUser);
        }
        else {
            Hobbies temp = allHobbies.get(messageText);
            if (temp != null) {
                System.out.println("success changing hobbie");
                if (currentUser.getUserHobbies().contains(temp)) {
                    currentUser.deleteHobby(temp);
                    MessageBuilder.usualMessage(chatId, messageText + " было удалено", HOBBY_KEYBOARD); // MESSAGE
                }
                else {
                    currentUser.addHobby(temp);
                    MessageBuilder.usualMessage(chatId, messageText + " было добавлено", HOBBY_KEYBOARD); // MESSAGE
                }
            }
            else {
                System.out.println("default");
                MessageBuilder.usualMessage(chatId, "Выберите команду с клавиатуры", HOBBY_KEYBOARD); // MESSAGE
            }
        }
    }

    private void registrationFunction(String chatId, Message userMessage) {
        System.out.println("register+");
        Profile currentUser = allUsers.get(chatId);
        ProfileManager.changeProfileLocal(userMessage, currentUser); // REGISTRATION WITH MESSAGES
    }

//    private void messageChooseFunction(String chatId, String messageText) {
//        Profile currentUser = allUsers.get(chatId);
//        int num = StringFunctions.isNum(messageText);
//        if (messageText.equals("Назад")) {
//            currentUser.setUserState(USER_STATE_MAIN_MENU);
//            MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
//        }
//        else if (num != -1) {
//            if (num > 0 && num <= currentUser.getFriends().size()) {
//                MessageBuilder.usualMessage(chatId, "Вы выбрали пользователя "
//                        + currentUser.getFriends().get(num - 1), BACK_KEYBOARD); // MESSAGE
//                // EXTRA LOGIC
//            }
//            else MessageBuilder.usualMessage(chatId, "Неправильный ввод", BACK_KEYBOARD); // MESSAGE
//        }
//        else MessageBuilder.usualMessage(chatId, "Неправильный ввод", BACK_KEYBOARD); // MESSAGE
//    }
//
//
//    private void viewChatFunction(String chatId, String messageText) {
//        Profile currentUser = allUsers.get(chatId);
//        if (messageText.equals("Назад")) {
//            currentUser.setUserState(USER_STATE_MAIN_MENU);
//            MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
//        }
//        else if (messageText.equals("Выбрать диалог")) {
//            currentUser.setUserState(USER_MESSAGES_CHOOSE);
//            MessageBuilder.usualMessage(chatId, "Введи порядковый номер диалога", BACK_KEYBOARD); // MESSAGE
//        }
//        else if (false) {
//            //add extra logic for viewing chat by id
//        }
//        else MessageBuilder.usualMessage(chatId, "Неправильный ввод", VIEW_MESSAGES_KEYBOARD); // MESSAGE
//    }

    private void findingFunction(String chatId, String messageText) {
        allChatIds = new HashSet<String>(allUsers.keySet());
        Profile currentUser = allUsers.get(chatId);
        if (messageText.equals("Назад")) {
            currentUser.setUserState(USER_STATE_MAIN_MENU);
            MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
        }
        else if (messageText.equals("♥")) {
            currentUser.addWatchedProfile(currentUser.getLastViewedProfile());
            currentUser.addFriend(currentUser.getLastViewedProfile());

            Profile FoundedMatch = Matcher.findAnotherPerson(currentUser,allUsers);
            MessageBuilder.usualMessage(chatId, FoundedMatch.getStr());// MESSAGE
            currentUser.setLastViewedProfile(FoundedMatch.getChatId());
        }
        else if (messageText.equals(BROKEN_HEART)) {
            currentUser.addWatchedProfile(currentUser.getLastViewedProfile());
            currentUser.deleteFriend(currentUser.getLastViewedProfile());//этот сценарий возможен лишь при повторном пробеге по всем пользователям
            allUsers.get(currentUser.getLastViewedProfile()).addNotLovedBy(chatId);
            //TODO: стоит сделать кнопку которая будет отвечать за удаление друзей, но не срочно.

            Profile FoundedMatch = Matcher.findAnotherPerson(currentUser,allUsers);
            MessageBuilder.usualMessage(chatId, FoundedMatch.getStr());// MESSAGE
            currentUser.setLastViewedProfile(FoundedMatch.getChatId());
        }
        else MessageBuilder.usualMessage(chatId, "something wrong", MAIN_MENU_KEYBOARD); // MESSAGE
    }

    private void mainMenuFunction(String chatId, String messageText) {
        // CHANGES STATE TO USER_PROFILE + EXTRA INFO
        Profile currentUser = allUsers.get(chatId);
        if (messageText.equals("Моя анкета")) {
            currentUser.setUserState(USER_PROFILE);
            MessageBuilder.usualMessage(chatId, currentUser.getStr(), VIEW_PROFILE_KEYBOARD);
        }

        // ЭТО ВСЕ ГАВНО НАДО ПЕРЕДЕЛАТЬ
//        else if (messageText.equals("Понравившиеся")) {
//            currentUser.setUserState(USER_MESSAGES);
//            MessageBuilder.usualMessage(chatId, "Ваши сообщения:\n"
//                    + StringFunctions.formatFriends(currentUser.getFriends()), VIEW_MESSAGES_KEYBOARD); // MESSAGE
//        }
        // CHANGES STATE TO USER_STATE_FINDING
        else if (messageText.equals("Поиск")) {
            Profile FoundedMatch = Matcher.findAnotherPerson(currentUser, allUsers);
            MessageBuilder.usualMessage(chatId, FoundedMatch.getStr());// MESSAGE
            System.out.println(currentUser.getLastViewedProfile());

            currentUser.setUserState(USER_STATE_FINDING);
            currentUser.setLastViewedProfile(FoundedMatch.getChatId());
            MessageBuilder.usualMessage(chatId, FoundedMatch.getStr(), FINDING_KEYBOARD);
        }
        // UNKNOWN MESSAGE
        else MessageBuilder.usualMessage(chatId, "Выбери команду на клавиатуре", MAIN_MENU_KEYBOARD); // MESSAGE
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
                System.out.println("userState:" + currentUser.getUserState());
                if (registrationStates.contains(currentUser.getUserState())) {
                    registrationFunction(chatId, userMessage);
                }
                else {
                    switch (currentUser.getUserState()) {
                        case USER_PROFILE -> viewProfileFunction(chatId,messageText);
                        case USER_PROFILE_CHANGE -> changeProfileFunction(chatId,userMessage);
                        case USER_PROFILE_HOBBIES -> changeHobbiesFunction(chatId,userMessage);
//                        case USER_MESSAGES_CHOOSE -> messageChooseFunction(chatId,messageText);
//                        case USER_MESSAGES ->  viewChatFunction(chatId,messageText);
                        case USER_STATE_FINDING -> findingFunction(chatId,messageText);
                        default -> mainMenuFunction(chatId,messageText);
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