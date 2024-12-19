package com.github.datingbot.auxiliary;

import java.util.List;

public enum Hobbies {

    LITERATURE_HOBBY(0,"Литература"),
    DANCE_HOBBY(1, "Танцы"),
    VIDEOGAMES_HOBBY(2, "Видеоигры"),
    SCIENCE_HOBBY(3, "Наука"),
    SPORT_HOBBY(4, "Спорт"),
    MUSIC_HOBBY(5, "Музыка"),
    COOKING_HOBBY(6, "Готовка"),
    TRAVELLING_HOBBY(7, "Путешествия"),
    ART_HOBBY(8, "Рисование");

    private String specificValue;
    private String title;

    private Hobbies(int num, String name) {
        specificValue = Integer.toString(num);
        title = name;
    }

    public String getTitle() {
        return title;
    }

    public String getSpecificValue() {
        return specificValue;
    }

    public static Hobbies getHobbyBySpecificValue(String key) {
        try {
            return List.of(Hobbies.values()).get(StringFunctions.isNum(key));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Hobbies getHobbyByName(String name) {
        for (Hobbies hobby : Hobbies.values()) {
            if (hobby.title.equals(name))
                return hobby;
        }
        return null;
    }
}
