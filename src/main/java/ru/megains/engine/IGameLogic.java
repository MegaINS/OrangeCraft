package ru.megains.engine;


public interface IGameLogic {


    void init() throws Exception;

    void input();

    void update();

    void render();

    void cleanup();


}
