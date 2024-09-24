package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMaster {

    private List<List<Integer>> gameMatrix;

    private boolean DEBUG = true;

    public GameMaster() {
        gameMatrix = new ArrayList<>();
        for (int county = 0; county != 3; county++) {
            List<Integer> curList = Arrays.asList(0, 0, 0);
            gameMatrix.add(curList);
        }

        if (DEBUG) {
            for (int i = 0; i != 3; i++) {
                for (int j = 0; j != 3; j++) {
                    System.out.print(gameMatrix.get(i).get(j));
                }
                System.out.println();
            }
        }


    }

    public boolean GameEnd(int key) {

        // stage 1
        if (gameMatrix.get(0).get(0) == gameMatrix.get(1).get(0) && gameMatrix.get(1).get(0) == gameMatrix.get(2).get(0)) {
            if (gameMatrix.get(0).get(0) == key)
                return true;
        }

        if (gameMatrix.get(0).get(1) == gameMatrix.get(1).get(1) && gameMatrix.get(1).get(1) == gameMatrix.get(2).get(1)) {
            if (gameMatrix.get(0).get(1) == key)
                return true;
        }

        if (gameMatrix.get(0).get(2) == gameMatrix.get(1).get(2) && gameMatrix.get(1).get(2) == gameMatrix.get(2).get(2)) {
            if (gameMatrix.get(0).get(2) == key)
                return true;
        }

        if (gameMatrix.get(0).get(0) == gameMatrix.get(0).get(1) && gameMatrix.get(0).get(1) == gameMatrix.get(0).get(2)) {
            if (gameMatrix.get(0).get(0) == key)
                return true;
        }

        if (gameMatrix.get(1).get(0) == gameMatrix.get(1).get(1) && gameMatrix.get(1).get(1) == gameMatrix.get(1).get(2)) {
            if (gameMatrix.get(1).get(0) == key)
                return true;
        }

        if (gameMatrix.get(2).get(0) == gameMatrix.get(2).get(1) && gameMatrix.get(2).get(1) == gameMatrix.get(2).get(2)) {
            if (gameMatrix.get(2).get(0) == key)
                return true;
        }

        if (gameMatrix.get(0).get(0) == gameMatrix.get(1).get(1) && gameMatrix.get(1).get(1) == gameMatrix.get(2).get(2)) {
            if (gameMatrix.get(0).get(0) == key)
                return true;
        }

        if (gameMatrix.get(0).get(2) == gameMatrix.get(1).get(1) && gameMatrix.get(1).get(1) == gameMatrix.get(2).get(0)) {
            if (gameMatrix.get(0).get(2) == key)
                return true;
        }

        return false;

    }

}