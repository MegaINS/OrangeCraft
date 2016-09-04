package ru.megains.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.megains.engine.IGameLogic;
import ru.megains.engine.MouseInput;
import ru.megains.engine.Window;
import ru.megains.engine.graph.Camera;
import ru.megains.engine.graph.Renderer;
import ru.megains.engine.graph.WorldRenderer;
import ru.megains.engine.graph.renderer.RenderItem;
import ru.megains.engine.graph.renderer.gui.GuiPlayerInventory;
import ru.megains.engine.graph.renderer.gui.GuiManager;
import ru.megains.engine.graph.renderer.texture.TextureManager;
import ru.megains.engine.graph.text.Hud;
import ru.megains.game.block.Block;
import ru.megains.game.blockdata.BlockDirection;
import ru.megains.game.blockdata.BlockSize;
import ru.megains.game.blockdata.BlockWorldPos;
import ru.megains.game.entity.item.EntityItem;
import ru.megains.game.entity.player.EntityPlayer;
import ru.megains.game.item.ItemStack;
import ru.megains.game.multiblock.MultiBlockSingle$;
import ru.megains.game.register.GameRegister;
import ru.megains.game.util.BlockAndPos;
import ru.megains.game.util.RayTraceResult;
import ru.megains.game.world.World;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;


public class OrangeCraft implements IGameLogic {

    public World world;
    public Renderer renderer;
    public RenderItem itemRender;
    public Window window;
    private Camera camera;
    private Vector3f cameraInc;
    private WorldRenderer worldRenderer;
    public EntityPlayer player;
    private Hud hud;
    public static OrangeCraft orangeCraft;

    public TextureManager textureManager;
    public GuiManager guiManager;


    public OrangeCraft(Window window) {
        orangeCraft = this;
        this.window = window;
        world = new World(64, 64, 64);

        renderer = new Renderer(this);
        camera = new Camera();
        cameraInc = new Vector3f();

        guiManager = new GuiManager(this);


    }


    @Override
    public void init() throws Exception {


        Block.initBlocks();
        MultiBlockSingle$.MODULE$.initMultiBlockSingle();
        textureManager = new TextureManager();
        textureManager.loadTexture(TextureManager.locationBlockTexture(), textureManager.textureMapBlock());
        worldRenderer = new WorldRenderer(world, textureManager);
        itemRender = new RenderItem(this);

        world.init();
        worldRenderer.init();
        renderer.init(window, textureManager);
        hud = new Hud(this);
        guiManager.init();


        player = new EntityPlayer(world);

        guiManager.setGuiScreen(new GuiPlayerInventory(player));

    }




    public void input() {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_E)) {
           if(guiManager.isGuiScreen()){
               guiManager.setGuiScreen(null);
           }else{
               guiManager.setGuiScreen(new GuiPlayerInventory(player));
           }
        }

        if (window.isKeyPressed(GLFW_KEY_O)) {
            iws++;
            if (iws == 20) {
                iws = 0;
                Random rand = new Random();
                for (int i = 0; i < 100; i++) {
                    EntityItem entityItem = new EntityItem(world, GameRegister.getItemById(rand.nextInt(4) + 2));
                    entityItem.setWorld(world);
                    entityItem.setPosition(rand.nextInt(128) - 64, 10, rand.nextInt(128) - 64);
                    world.spawnEntityInWorld(entityItem);
                }
                iw4s += 100;
                System.out.println(iw4s);
            }
        }


    }

    private int iw4s = 0;
    private int iws = 0;

    public RayTraceResult result;

    public BlockAndPos blockAndPos;

    @Override
    public void update(MouseInput mouseInput) {


        world.update();

        Vector2f rotVec = mouseInput.getDisplVec();
        player.turn(rotVec.x, rotVec.y);
        player.update(cameraInc.x, cameraInc.y, cameraInc.z);

        camera.setPosition(player.posX(), player.posY() + player.levelView(), player.posZ());
        camera.setRotation(player.xRot(), player.yRot(), 0);
        player.inventory().changeStackSelect(mouseInput.scroll);
        mouseInput.scroll = 0;


        result = player.rayTrace(5, 0.1f);


        hud.update();


        if (result != null) {
            ItemStack stack = player.inventory().getStackSelect();
            if (stack != null) {
                isSetBlock(mouseInput, Block.getBlockFromItem(stack.item()));
            } else {
                isBreakBlock(mouseInput);
            }

        } else {
            blockAndPos = null;
        }

        if (blockAndPos != null) {
            worldRenderer.updateBlockBounds(blockAndPos);
        }


    }


    private void isSetBlock(MouseInput mouseInput, Block block) {


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

        if (blockAndPos != null) {

            if (mouseInput.isRightButtonPressed() && !clickP) {
                world.setBlock(blockAndPos.pos(), blockAndPos.block());
                clickP = true;
            } else if (!mouseInput.isRightButtonPressed()) {
                clickP = false;
            }
        }
    }

    private void isBreakBlock(MouseInput mouseInput) {

        if (result.block.isFullBlock()) {
            blockAndPos = new BlockAndPos(result.block, new BlockWorldPos(result.getBlockWorldPos()));
        } else {
            blockAndPos = new BlockAndPos(result.block, result.getBlockWorldPos());
        }

        if (blockAndPos.pos() != null) {
            if (mouseInput.isLeftButtonPressed() && !clickL) {
                world.setAirBlock(blockAndPos.pos());
                clickL = true;
            } else if (!mouseInput.isLeftButtonPressed()) {
                clickL = false;
            }
        }

    }


    private boolean clickL = false;
    private boolean clickP = false;


    @Override
    public void render() {

        renderer.render(window, camera, hud, worldRenderer);


    }


    @Override
    public void cleanup() {
        world.save();
        renderer.cleanup();
        worldRenderer.cleanUp();
    }
}
