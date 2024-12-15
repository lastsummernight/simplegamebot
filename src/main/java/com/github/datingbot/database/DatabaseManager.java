package com.github.datingbot.database;

import com.github.datingbot.auxiliary.Debugger;
import com.github.datingbot.profile.Profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:datingbotdb.db";
    private static Connection connection = null;

//    public static void main(String[] args) {
//        connectDB();
//        extraCreateTable();
//        addUser(new Profile("123123", USER_STATE_MAIN_MENU, "Motya", 20, "Msk", true, "О себе", "123"));
//        ePrintTable();
//        closeConnectDB();
//    }

    public static void connectDB() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connection to the database established successfully!");

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static void extraCreateTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id INTEGER UNIQUE PRIMARY KEY, "
                + "name TEXT, "
                + "age INTEGER, "
                + "city TEXT, "
                + "gender BOOLEAN, "
                + "info TEXT, "
                + "friends TEXT);";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
            System.out.println("table users created");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void extraPrintTable() {
        String requestSQL = "SELECT * FROM users;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(requestSQL);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                List<String> temp = new ArrayList<>();
                temp.add(resultSet.getString("user_id"));
                temp.add(resultSet.getString("name"));
                temp.add(resultSet.getString("age"));
                temp.add(resultSet.getString("city"));
                temp.add(resultSet.getString("gender"));
                temp.add(resultSet.getString("info"));
                temp.add(resultSet.getString("friends"));
                temp.add(resultSet.getString("notfriends"));
                temp.add(resultSet.getString("hobbies"));
                Debugger.printProfile(new Profile(temp));
            }

            resultSet.close();
            preparedStatement.close();
            System.out.println("table has printed");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void extraCreateAttr() {
        String statement = "ALTER TABLE users ADD COLUMN hobbies TEXT";

        try {
            Statement preparedStatement = connection.createStatement();
            preparedStatement.execute(statement);
            System.out.println("attr column created");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<String, Profile> getAllUsers() {
        HashMap<String, Profile> allUsers = new HashMap<>();
        String query = "SELECT * FROM users";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String userId = Integer.toString(resultSet.getInt("user_id"));
                String userName = resultSet.getString("name");
                String userAge = resultSet.getString("age");
                String userCity = resultSet.getString("city");
                String userGender = resultSet.getString("gender");
                String userInfo = resultSet.getString("info");
                String userFriends = resultSet.getString("friends");
                String userNotFriends = resultSet.getString("notfriends");
                String userHobbies = resultSet.getString("hobbies");

                Profile user = new Profile(Arrays.asList(userId, userName, userAge, userCity,
                        userGender, userInfo, userFriends, userNotFriends, userHobbies));

                allUsers.put(userId, user);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }

    public static void addUser(Profile profile) {
        String query = "INSERT INTO users (user_id, name, age, city, gender, info, friends, notfriends, hobbies) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, profile.getChatId());
            preparedStatement.setString(2, profile.getUsername());
            preparedStatement.setString(3, Integer.toString(profile.getAge()));
            preparedStatement.setString(4, profile.getCity());
            if (profile.getGender().compareTo("Парень") == 0)
                preparedStatement.setString(5, "1");
            else
                preparedStatement.setString(5, "0");
            preparedStatement.setString(6, profile.getInfo());
            preparedStatement.setString(7, profile.getStrFriendsDB());
            preparedStatement.setString(8, profile.getStrNotFriendsDB());
            preparedStatement.setString(9, profile.getStrHobbiesDB());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void changeUser(Profile profile) {
        String query = "UPDATE users SET name = ?, age = ?, city = ?, gender = ?, info = ?, friends = ?, notfriends = ?, hobbies = ? WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, profile.getUsername());
            preparedStatement.setString(2, Integer.toString(profile.getAge()));
            preparedStatement.setString(3, profile.getCity());
            if (profile.getGender().compareTo("Парень") == 0)
                preparedStatement.setString(4, "1");
            else
                preparedStatement.setString(4, "0");
            preparedStatement.setString(5, profile.getInfo());
            preparedStatement.setString(6, profile.getStrFriendsDB());
            preparedStatement.setString(7, profile.getStrNotFriendsDB());
            preparedStatement.setString(8, profile.getStrHobbiesDB());
            preparedStatement.setString(9, profile.getChatId());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> allChatIds() {
        String query = "SELECT user_id FROM users;";
        List<String> allIds = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("user_id");
                allIds.add(name);
            }

            System.out.println("All users ids catched");
            return allIds;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void deleteUser(String chatId) {
        String query = "DELETE FROM users WHERE user_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, chatId);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Line " + chatId + " deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnectDB() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

