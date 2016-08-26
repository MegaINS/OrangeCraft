package ru.megains.engine.graph.text;


import java.util.HashMap;

public interface IHud {

    HashMap<String, Text> getGameItems();

    default void cleanup() {
        HashMap<String,Text> gameItems = getGameItems();
        for (Text gameItem : gameItems.values()) {
            gameItem.getMesh().cleanUp();
        }
    }
}
