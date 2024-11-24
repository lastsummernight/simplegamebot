package com.github.simplegamebot;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.lang.Cloneable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.github.simplegamebot.State.USER_STATE_MAIN_MENU;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/datingbotdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    private static Connection connection = null;

    public static void connectDB() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection to the database established successfully!");

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static HashMap<String, Profile> getAllUsers() {
        HashMap<String, Profile> allUsers = new HashMap<>();
        String query = "SELECT * FROM users";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                String user_id = Integer.toString(resultSet.getInt("user_id"));
                String user_name = resultSet.getString("name");
                int user_age = resultSet.getInt("age");
                String user_city = resultSet.getString("city");
                boolean user_gender = resultSet.getBoolean("gender");
                String user_info = resultSet.getString("info");

                Profile user = new Profile(user_id, USER_STATE_MAIN_MENU, user_name, user_age, user_city, user_gender, user_info);

                allUsers.put(user_id, user);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }

    public static void addUser(Profile profile) {
        String query = "INSERT INTO users (user_id, name, age, city, gender, info) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE user_id = user_id";

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

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void changeUser(Profile profile) {
        String query = "UPDATE users SET name = ?, age = ?, city = ?, gender = ?, info = ? WHERE user_id = ?";

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
            preparedStatement.setString(6, profile.getChatId());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");
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

    public static void main(String[] args) {
        connectDB();
//        addUser("1231", "Вася", 18, "Moscow", "Парень", "asdfhjhklg");
//        addUser("12313", "Вася", 18, "Moscow", "Парень", "asdfhjhklg");
//        changeUser("12313", "Вася", 18, "Moscow", "Девушка", "лофыврафоырвар");
        HashMap<String, Profile> temp = getAllUsers();
        for (String t : temp.keySet()) {
            try {
                Debugger.printProfile(temp.get(t));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        closeConnectDB();
    }
}

