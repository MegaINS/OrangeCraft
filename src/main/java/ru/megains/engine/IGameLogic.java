package ru.megains.engine;


public interface IGameLogic   {


    void init() throws Exception;
    void input();
    void update(MouseInput mouseInput);

    void render();
    void cleanup();



}
