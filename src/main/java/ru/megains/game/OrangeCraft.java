package ru.megains.game;

import org.joml.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import ru.megains.engine.IGameLogic;
import ru.megains.engine.graph.Camera;
import ru.megains.engine.graph.Renderer;
import ru.megains.engine.graph.WorldRenderer;
import ru.megains.game.block.Block;
import ru.megains.game.blockdata.BlockDirection;
import ru.megains.game.blockdata.BlockSize;
import ru.megains.game.blockdata.BlockWorldPos;
import ru.megains.game.entity.player.EntityPlayer;
import ru.megains.game.item.ItemStack;
import ru.megains.game.multiblock.MultiBlockSingle$;
import ru.megains.game.util.BlockAndPos;
import ru.megains.game.util.RayTraceResult;
import ru.megains.game.world.World;
import ru.megains.managers.GuiManager;
import ru.megains.managers.TextureManager;
import ru.megains.renderer.FontRender;
import ru.megains.renderer.RenderItem;
import ru.megains.renderer.gui.GuiInGameMenu;
import ru.megains.renderer.gui.GuiPlayerInventory;


public class OrangeCraft implements IGameLogic {

    public static OrangeCraft orangeCraft;
    public World world;
    public Renderer renderer;
    public RenderItem itemRender;
    public EntityPlayer player;
    public TextureManager textureManager;
    public GuiManager guiManager;
    public RayTraceResult result;
    public BlockAndPos blockAndPos;
    private Camera camera;
    private Vector3f cameraInc;
    private WorldRenderer worldRenderer;
    public FontRender fontRender;
    public int frames;
    // private Hud hud;


    public OrangeCraft() {
        orangeCraft = this;

        world = new World(64, 64, 64);

        renderer = new Renderer(this);
        camera = new Camera();
        cameraInc = new Vector3f();

        guiManager = new GuiManager(this);


    }

    @Override
    public void init() throws Exception {
        try {
            //  PixelFormat as = new PixelFormat(0,0,8,8,0,0,0,0,false,false);
            Display.setDisplayMode(new DisplayMode(800, 600));
            //   Display.create(as);
            Display.create();
            GL11.glClearColor(0.5f, 0.6f, 0.7f, 0.0F);


        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1000);
        }


        Block.initBlocks();
        MultiBlockSingle$.MODULE$.initMultiBlockSingle();
        textureManager = new TextureManager();
        worldRenderer = new WorldRenderer(world, textureManager);
        renderer.init(textureManager, worldRenderer);
        textureManager.loadTexture(TextureManager.locationBlockTexture(), textureManager.textureMapBlock());

        itemRender = new RenderItem(this);
        fontRender = new FontRender();

        world.init();
        worldRenderer.init();

        //   hud = new Hud(this);
        guiManager.init();


        player = new EntityPlayer(world);

        grabMouseCursor();

    }

    public void input() {
        cameraInc.set(0, 0, 0);

        if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_W)) {
            cameraInc.z = -1;
        } else if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_S)) {
            cameraInc.z = 1;
        }
        if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_A)) {
            cameraInc.x = -1;
        } else if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_D)) {
            cameraInc.x = 1;
        }
        if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LSHIFT)) {
            cameraInc.y = -1;
        } else if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_SPACE)) {
            cameraInc.y = 1;
        }

//
//        if (window.isKeyPressed(GLFW_KEY_O)) {
//            iws++;
//            if (iws == 20) {
//                iws = 0;
//                Random rand = new Random();
//                for (int i = 0; i < 100; i++) {
//                    EntityItem entityItem = new EntityItem(world, GameRegister.getItemById(rand.nextInt(4) + 2));
//                    entityItem.setWorld(world);
//                    entityItem.setPosition(rand.nextInt(128) - 64, 10, rand.nextInt(128) - 64);
//                    world.spawnEntityInWorld(entityItem);
//                }
//                iw4s += 100;
//                System.out.println(iw4s);
//            }
//        }


    }

    public void update() {

        if (guiManager.isGuiScreen()) {
            guiManager.handleInput();
        } else {
            runTickKeyboard();
            runTickMouse();
        }


        world.update();

        if (!guiManager.isGuiScreen()) {
            player.turn(Mouse.getDX(), Mouse.getDY());
        }

        player.update(cameraInc.x, cameraInc.y, cameraInc.z);

        camera.setPosition(player.posX(), player.posY() + player.levelView(), player.posZ());
        camera.setRotation(player.xRot(), player.yRot(), 0);
        player.inventory().changeStackSelect(Mouse.getDWheel() * -1);


        result = player.rayTrace(5, 0.1f);


        //  hud.update();
        guiManager.tick();


        if (result != null) {
            ItemStack stack = player.inventory().getStackSelect();
            if (stack != null) {
                isSetBlock(Block.getBlockFromItem(stack.item()));
            } else {
                isBreakBlock();
            }

        } else {
            blockAndPos = null;
        }

        if (blockAndPos != null) {
            worldRenderer.updateBlockBounds(blockAndPos);
        }


        renderer.tick();
    }

    private void runTickMouse() {
        while (Mouse.next()) {
            int button = Mouse.getEventButton();
            boolean buttonState = Mouse.getEventButtonState();
            if (button == 1 && buttonState && blockAndPos != null) {
                ItemStack stack = player.inventory().getStackSelect();
                if (stack != null) {
                    world.setBlock(blockAndPos.pos(), blockAndPos.block());
                }
            }
        }
    }

    private void runTickKeyboard() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_E:
                        guiManager.setGuiScreen(new GuiPlayerInventory(player));
                        break;
                    case Keyboard.KEY_ESCAPE:
                        guiManager.setGuiScreen(new GuiInGameMenu());
                        break;

                }
            }
        }
    }

    public void grabMouseCursor() {

        Mouse.setGrabbed(true);

    }

    /**
     * Ungrabs the mouse cursor so it can be moved and set it to the center of the screen
     */
    public void ungrabMouseCursor() {
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    private void isSetBlock(Block block) {


        BlockWorldPos posTarget = result.getBlockWorldPos();
        BlockWorldPos posSet;
        if (block.isFullBlock()) {
            posSet = posTarget.sum(result.sideHit);
        } else {
            posSet = new BlockWorldPos(posTarget.sum(result.sideHit), BlockSize.get(result.hitVec.x), BlockSize.get(result.hitVec.y), BlockSize.get(result.hitVec.z));
        }


        if (block.isFullBlock()) {
            if (world.isAirBlock(posSet)) {
                blockAndPos = new BlockAndPos(block, posSet);
            } else {
                blockAndPos = null;
            }
        } else {
            float x = posSet.blockX().value();
            float y = posSet.blockY().value();
            float z = posSet.blockZ().value();

            BlockDirection bd = result.sideHit;

            if (bd == BlockDirection.UP()) {
                y = 0;
            } else if (bd == BlockDirection.EAST()) {
                x = 0;
            } else if (bd == BlockDirection.SOUTH()) {
                z = 0;
            } else if (bd == BlockDirection.DOWN()) {
                y = 1;
            } else if (bd == BlockDirection.WEST()) {
                x = 1;
            } else if (bd == BlockDirection.NORTH()) {
                z = 1;
            }

            if (x + block.getPhysicsBody().getMaxX() > 1) {
                x = 1 - block.getPhysicsBody().getMaxX();
            }
            if (y + block.getPhysicsBody().getMaxY() > 1) {
                y = 1 - block.getPhysicsBody().getMaxY();
            }
            if (z + block.getPhysicsBody().getMaxZ() > 1) {
                z = 1 - block.getPhysicsBody().getMaxZ();
            }
            posSet = new BlockWorldPos(posSet, BlockSize.get(x), BlockSize.get(y), BlockSize.get(z));


            if (world.getBlock(posTarget).isFullBlock()) {

                if (world.isAirBlock(posSet)) {
                    blockAndPos = new BlockAndPos(block, posSet);
                } else {
                    if (world.getBlock(posSet).isCanPut(posSet, block)) {
                        blockAndPos = new BlockAndPos(block, posSet);
                    } else {
                        blockAndPos = null;
                    }
                }
            } else {
                posTarget = new BlockWorldPos(posTarget, BlockSize.get(result.hitVec.x), BlockSize.get(result.hitVec.y), BlockSize.get(result.hitVec.z));


                float x1 = posTarget.blockX().value();
                float y1 = posTarget.blockY().value();
                float z1 = posTarget.blockZ().value();

                if (bd == BlockDirection.UP()) {
                    if (y1 + block.getPhysicsBody().getMaxY() > 1) {
                        y1 = 1 - block.getPhysicsBody().getMaxY();
                    }
                } else if (bd == BlockDirection.DOWN()) {

                    if (y1 - block.getPhysicsBody().getMaxY() <= 0) {
                        y1 = 0;
                    } else {
                        y1 = y1 - block.getPhysicsBody().getMaxY();
                    }
                } else if (bd == BlockDirection.NORTH()) {
                    if (z1 - block.getPhysicsBody().getMaxZ() <= 0) {
                        z1 = 0;
                    } else {
                        z1 = z1 - block.getPhysicsBody().getMaxZ();
                    }
                } else if (bd == BlockDirection.SOUTH()) {
                    if (z1 + block.getPhysicsBody().getMaxZ() > 1) {
                        z1 = 1 - block.getPhysicsBody().getMaxZ();
                    }
                } else if (bd == BlockDirection.WEST()) {
                    if (x1 - block.getPhysicsBody().getMaxX() <= 0) {
                        x1 = 0;
                    } else {
                        x1 = x1 - block.getPhysicsBody().getMaxX();
                    }
                } else if (bd == BlockDirection.EAST()) {
                    if (x1 + block.getPhysicsBody().getMaxX() > 1) {
                        x1 = 1 - block.getPhysicsBody().getMaxX();
                    }
                }

                if (x1 + block.getPhysicsBody().getMaxX() > 1) {
                    x1 = 1 - block.getPhysicsBody().getMaxX();
                }
                if (y1 + block.getPhysicsBody().getMaxY() > 1) {
                    y1 = 1 - block.getPhysicsBody().getMaxY();
                }
                if (z1 + block.getPhysicsBody().getMaxZ() > 1) {
                    z1 = 1 - block.getPhysicsBody().getMaxZ();
                }

                posTarget = new BlockWorldPos(posTarget, BlockSize.get(x1), BlockSize.get(y1), BlockSize.get(z1));


                if (world.getBlock(posTarget).isCanPut(posTarget, block)) {

                    blockAndPos = new BlockAndPos(block, posTarget);
                } else {


                    if (world.isAirBlock(posSet)) {
                        blockAndPos = new BlockAndPos(block, posSet);
                    } else {
                        if (world.getBlock(posSet).isCanPut(posSet, block)) {
                            blockAndPos = new BlockAndPos(block, posSet);
                        } else {
                            blockAndPos = null;
                        }
                    }
                }
            }
        }

//        if (blockAndPos != null) {
//
////            if (mouseInput.isRightButtonPressed() && !clickP) {
////                world.setBlock(blockAndPos.pos(), blockAndPos.block());
////                clickP = true;
////            } else if (!mouseInput.isRightButtonPressed()) {
////                clickP = false;
////            }
//        }
    }

    private void isBreakBlock() {

        if (result.block.isFullBlock()) {
            blockAndPos = new BlockAndPos(result.block, new BlockWorldPos(result.getBlockWorldPos()));
        } else {
            blockAndPos = new BlockAndPos(result.block, result.getBlockWorldPos());
        }

//        if (blockAndPos.pos() != null) {
//            if ( && !clickL) {
//                world.setAirBlock(blockAndPos.pos());
//                clickL = true;
//            } else if () {
//                clickL = false;
//            }
//        }

    }

    @Override
    public void render() {

        renderer.render(camera, worldRenderer);


    }


    @Override
    public void cleanup() {

        if (world != null) {
            world.save();
        }
        if (renderer != null) {
            renderer.cleanup();
        }
        if (worldRenderer != null) {
            worldRenderer.cleanUp();
        }
        Display.destroy();
    }
}
