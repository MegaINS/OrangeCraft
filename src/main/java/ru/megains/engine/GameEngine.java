package ru.megains.engine;


import ru.megains.engine.graph.RenderChunk;
import ru.megains.engine.graph.text.Hud;

public class GameEngine implements Runnable {


    private IGameLogic gameLogic;
    private Timer timer;
    private Window window;
    private MouseInput mouseInput;

    private final Thread gameLoopThread;


    public GameEngine(IGameLogic gameLogic, Window window) {
        this.window = window;
        timer = new Timer(20);
        mouseInput = new MouseInput();
        this.gameLogic = gameLogic;
        this.gameLoopThread = new Thread(this, "GAME_ENGINE");
    }


    public void start() {

        gameLoopThread.start();

    }


    @Override
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

    private static int frames = 0;

    private void gameLoop() {


        boolean running = true;
        long lastTime = System.currentTimeMillis();

        int tick = 0;
        timer.init();
        while (running && !window.windowShouldClose()) {


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
                Hud.frames = frames;
                frames = 0;
                tick = 0;
                printMemoryUsage();
            }
        }
    }

    private static double MB = 1024 * 1024;

    private static void printMemoryUsage() {
        Runtime r = Runtime.getRuntime();
        System.out.printf("Memory usage: total=%4.2f MB, free=%4.2f MB, max=%4.2f MB\n",
                r.totalMemory() / MB, r.freeMemory() / MB, r.maxMemory() / MB);
    }

    private void init() throws Exception {
        window.init();
        mouseInput.init(window);
        gameLogic.init();
    }

    private void update() {
        mouseInput.input(window);
        gameLogic.input();
        gameLogic.update(mouseInput);
    }

    private void render() {

        gameLogic.render();

        window.update();

    }

    private void cleanup() {
        gameLogic.cleanup();
    }


}
