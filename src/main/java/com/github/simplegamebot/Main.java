package com.github.simplegamebot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        String botToken = token.token;
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            DatabaseManager.connectDB();
            botsApplication.registerBot(botToken, new MyAmazingBot(botToken));
            System.out.println("dating bot successfully started!");
            Thread.currentThread().join();
            DatabaseManager.closeConnectDB();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}