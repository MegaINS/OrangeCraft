package ru.megains.engine;


import org.lwjgl.opengl.Display;
import ru.megains.engine.graph.RenderChunk;
import ru.megains.game.OrangeCraft;

public class GameEngine implements Runnable {


    private static int frames = 0;
    private static double MB = 1024 * 1024;
    private final Thread gameLoopThread;
    private float TARGET_FPS = 60;
    private OrangeCraft gameLogic;
    private Timer timer;


    public GameEngine(OrangeCraft gameLogic) {
        timer = new Timer(20);
        this.gameLogic = gameLogic;
        this.gameLoopThread = new Thread(this, "GAME_ENGINE");
    }

    private static void printMemoryUsage() {
        Runtime r = Runtime.getRuntime();
        System.out.printf("Memory usage: total=%4.2f MB, free=%4.2f MB, max=%4.2f MB\n",
                r.totalMemory() / MB, r.freeMemory() / MB, r.maxMemory() / MB);
    }

    public void start() {


        gameLoopThread.start();

    }

    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void gameLoop() {


        boolean running = true;
        long lastTime = System.currentTimeMillis();

        int tick = 0;
        timer.init();
        while (running) {
            if (Display.isCloseRequested() && Display.isCreated()) {
                running = false;
            }

            timer.update();
            for (int i = 0; i < timer.getTick(); ++i) {
                update();
                tick++;
            }


            render();
            ++frames;


            while (System.currentTimeMillis() >= lastTime + 1000L) {
                System.out.println(frames + " fps, " + tick + " tick, " + RenderChunk.chunkRender / (frames == 0 ? 1 : frames) + " chunkRender, " + RenderChunk.chunkUpdate + " chunkUpdate");
                RenderChunk.chunkRender = 0;
                RenderChunk.chunkUpdate = 0;
                lastTime += 1000L;
                gameLogic.frames = frames;

                frames = 0;
                tick = 0;
                printMemoryUsage();
            }
            sync();
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    private void init() throws Exception {

        gameLogic.init();

    }

    private void update() {

        gameLogic.input();
        gameLogic.update();
    }

    private void render() {

        gameLogic.render();


    }

    private void cleanup() {
        gameLogic.cleanup();
    }


}
