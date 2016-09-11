package ru.megains;

import ru.megains.engine.GameEngine;
import ru.megains.game.OrangeCraft;

public class Main {

    public static void main(String[] args) {

        System.out.println("OrangeCraft v0.1.14");
        System.out.println("5150 строк на 20.06.2016");
        System.out.println("7000 строк на 20.07.2016");
        System.out.println("8350 строк на 30.08.2016");


        OrangeCraft megaGame = new OrangeCraft();
        GameEngine gameEngine = new GameEngine(megaGame);
        gameEngine.start();


    }
}
