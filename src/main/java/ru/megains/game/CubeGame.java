package ru.megains.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import ru.megains.engine.IGameLogic;
import ru.megains.engine.MouseInput;
import ru.megains.engine.Window;
import ru.megains.engine.graph.Camera;
import ru.megains.engine.graph.Renderer;
import ru.megains.engine.graph.renderer.gui.GuiInventory;
import ru.megains.engine.graph.renderer.gui.GuiScreen;
import ru.megains.engine.graph.renderer.texture.TextureManager;
import ru.megains.engine.graph.text.Hud;
import ru.megains.game.block.Block;
import ru.megains.game.blockdata.BlockDirection;
import ru.megains.game.blockdata.BlockSize;
import ru.megains.game.blockdata.BlockWorldPos;
import ru.megains.game.entity.player.EntityPlayer;
import ru.megains.game.multiblock.MultiBlockSingle$;
import ru.megains.game.util.BlockAndPos;
import ru.megains.game.util.RayTraceResult;
import ru.megains.game.world.World;
import ru.megains.game.world.WorldRenderer;

import static org.lwjgl.glfw.GLFW.*;


public class CubeGame implements IGameLogic {

    public World world;
    private Renderer renderer;
    public Window window;
    private Camera camera;
    private Vector3f cameraInc;
    private WorldRenderer worldRenderer;
    public EntityPlayer player;
    private Hud hud;
    public static CubeGame megaGame;

    public TextureManager textureManager;

    public GuiScreen guiScreen;

    public int blockSelect = 2;


    public CubeGame(Window window){
        megaGame = this;
        this.window = window;
        world = new World(64,64,64);

        renderer =new Renderer(this);
        camera =new Camera();
        cameraInc = new Vector3f();
        player = new EntityPlayer();
        player.setWorld(world);


    }


    @Override
    public void init() throws Exception {


        Block.initBlocks();
        MultiBlockSingle$.MODULE$.initMultiBlockSingle();
        textureManager = new TextureManager();
        textureManager.loadTexture(TextureManager.locationBlockTexture(),textureManager.textureMapBlock());
        worldRenderer = new WorldRenderer(world,textureManager);

        world.init();
        worldRenderer.init();
        renderer.init(window,textureManager);
        hud = new Hud(this);
        setGuiScreen(new GuiInventory());

    }


    public void setGuiScreen(GuiScreen screen) {

        if(guiScreen!=null){
            guiScreen.cleanup();
        }
        if(screen!=null){
            screen.init();
        }
        guiScreen = screen;
    }

    public void input(){
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

        if(window.isKeyPressed(GLFW_KEY_1)){
            blockSelect = 2;
        }
        if(window.isKeyPressed(GLFW_KEY_2)){
            blockSelect = 3;
        }
        if(window.isKeyPressed(GLFW_KEY_3)){
            blockSelect = 4;
        }
        if(window.isKeyPressed(GLFW_KEY_4)){
            blockSelect = 5;
        }
        if(window.isKeyPressed(GLFW_KEY_5)){
            blockSelect = 6;
        }
        if(window.isKeyPressed(GLFW_KEY_6)){
            blockSelect = 7;
        }
        if(window.isKeyPressed(GLFW_KEY_7)){
            blockSelect = 8;
        }

        if(window.isKeyPressed(GLFW_KEY_Q)){
            is++;
            if(is ==10){
                isSet = !isSet;
                is=0;
            }
        }

    }
    public int is =0;

    public RayTraceResult result;

    public BlockAndPos blockAndPos ;
    @Override
    public void update(MouseInput mouseInput) {


        world.update();

        Vector2f rotVec = mouseInput.getDisplVec();
        player.turn(rotVec.x, rotVec.y);
        player.update(cameraInc.x, cameraInc.y, cameraInc.z);

        camera.setPosition(player.posX(), player.posY() + player.levelView(), player.posZ());
        camera.setRotation(player.xRot(), player.yRot(), 0);


        if(mouseInput.scroll<0){
            blockSelect--;
            if(blockSelect<2){
                blockSelect = 8;
            }
        }else if(mouseInput.scroll>0){
            blockSelect++;
            if(blockSelect>8){
                blockSelect = 2;
            }
        }

        mouseInput.scroll =0;

        result = player.rayTrace(5, 0.1f);


        hud.update();


        if(result!=null){
            if(isSet){
                isSetBlock(mouseInput);
            }else {
                isBreakBlock(mouseInput);
            }
        }else{
            blockAndPos = null;
        }

        if(blockAndPos!=null){
            worldRenderer.updateBlockBounds(blockAndPos);
        }
      //  worldRenderer.update();



    }


    public boolean isSet = true;

    public void isSetBlock(MouseInput mouseInput){

        Block selectBlock =  Block.getBlockById(blockSelect);
        BlockWorldPos posTarget = result.getBlockWorldPos();
        BlockWorldPos posSet;
        if(selectBlock.isFullBlock()){
            posSet = posTarget.sum(result.sideHit);
        }else {
            posSet = new BlockWorldPos(posTarget.sum(result.sideHit), BlockSize.get(result.hitVec.x),BlockSize.get(result.hitVec.y),BlockSize.get(result.hitVec.z)) ;
        }


        if(selectBlock.isFullBlock()){
            if(world.isAirBlock(posSet)){
                blockAndPos = new BlockAndPos(selectBlock,posSet);
            }else{
                blockAndPos = null;
            }
        }else{
            float x = posSet.blockX().value();
            float y = posSet.blockY().value();
            float z = posSet.blockZ().value();

            BlockDirection bd = result.sideHit;

            if(bd==BlockDirection.UP()){
                y=0;
            }else if(bd==BlockDirection.EAST()){
                x=0;
            }else if(bd==BlockDirection.SOUTH()){
                z=0;
            }else if(bd==BlockDirection.DOWN()){
               y=1;
            }else if(bd==BlockDirection.WEST()){
                x=1;
            }else if(bd==BlockDirection.NORTH()){
                z=1;
            }

            if(x+selectBlock.getPhysicsBody().getMaxX()>1){
                x = 1-selectBlock.getPhysicsBody().getMaxX();
            }
            if(y+selectBlock.getPhysicsBody().getMaxY()>1){
                y =1-selectBlock.getPhysicsBody().getMaxY();
            }
            if(z+selectBlock.getPhysicsBody().getMaxZ()>1){
                z = 1-selectBlock.getPhysicsBody().getMaxZ();
            }
            posSet = new BlockWorldPos(posSet,BlockSize.get(x),BlockSize.get(y),BlockSize.get(z));



            if(world.getBlock(posTarget).isFullBlock()){

                if(world.isAirBlock(posSet)){
                    blockAndPos = new BlockAndPos(selectBlock,posSet);
                }else{
                    if(world.getBlock(posSet).isCanPut(posSet,selectBlock))  {
                        blockAndPos = new BlockAndPos(selectBlock,posSet);
                    }else {
                        blockAndPos =null;
                    }
                }
            }else {
                posTarget = new BlockWorldPos(posTarget, BlockSize.get(result.hitVec.x),BlockSize.get(result.hitVec.y),BlockSize.get(result.hitVec.z)) ;


                float x1 = posTarget.blockX().value();
                float y1 = posTarget.blockY().value();
                float z1 = posTarget.blockZ().value();

                if(bd ==BlockDirection.UP()){
                    if(y1+selectBlock.getPhysicsBody().getMaxY()>1){
                        y1 = 1-selectBlock.getPhysicsBody().getMaxY();
                    }
                }else if(bd == BlockDirection.DOWN()){

                    if(y1-selectBlock.getPhysicsBody().getMaxY()<=0){
                        y1 = 0;
                    }else {
                        y1 = y1-selectBlock.getPhysicsBody().getMaxY();
                    }
                }else if(bd == BlockDirection.NORTH()){
                    if(z1-selectBlock.getPhysicsBody().getMaxZ()<=0){
                        z1 = 0;
                    }else {
                        z1 = z1 - selectBlock.getPhysicsBody().getMaxZ();
                    }
                }else if(bd == BlockDirection.SOUTH()){
                    if(z1+selectBlock.getPhysicsBody().getMaxZ()>1){
                        z1 = 1-selectBlock.getPhysicsBody().getMaxZ();
                    }
                }else if(bd == BlockDirection.WEST()){
                    if(x1-selectBlock.getPhysicsBody().getMaxX()<=0){
                        x1 = 0;
                    }else {
                        x1 = x1-selectBlock.getPhysicsBody().getMaxX();
                    }
                }else if(bd == BlockDirection.EAST()){
                    if(x1+selectBlock.getPhysicsBody().getMaxX()>1){
                        x1 = 1-selectBlock.getPhysicsBody().getMaxX();
                    }
                }

                if(x1+selectBlock.getPhysicsBody().getMaxX()>1){
                    x1 = 1-selectBlock.getPhysicsBody().getMaxX();
                }
                if(y1+selectBlock.getPhysicsBody().getMaxY()>1){
                    y1 =1-selectBlock.getPhysicsBody().getMaxY();
                }
                if(z1+selectBlock.getPhysicsBody().getMaxZ()>1){
                    z1 = 1-selectBlock.getPhysicsBody().getMaxZ();
                }

                posTarget = new BlockWorldPos(posTarget,BlockSize.get(x1),BlockSize.get(y1),BlockSize.get(z1));


                if(world.getBlock(posTarget).isCanPut(posTarget, selectBlock)){

                    blockAndPos = new BlockAndPos(selectBlock,posTarget);
                }else {


                    if(world.isAirBlock(posSet)){
                        blockAndPos = new BlockAndPos(selectBlock,posSet);
                    }else{
                        if(world.getBlock(posSet).isCanPut(posSet,selectBlock))  {
                            blockAndPos = new BlockAndPos(selectBlock,posSet);
                        }else {
                            blockAndPos =null;
                        }
                    }
                }
            }
        }

        if(blockAndPos!=null) {

            if (mouseInput.isRightButtonPressed() && !clickP) {
                world.setBlock(blockAndPos.pos(), blockAndPos.block());
                clickP = true;
            } else if (!mouseInput.isRightButtonPressed()) {
                clickP = false;
            }
        }
    }
    public void isBreakBlock(MouseInput mouseInput){

        if(result.block.isFullBlock()){
            blockAndPos = new BlockAndPos(result.block,new BlockWorldPos(result.getBlockWorldPos()) );
        }else {
            blockAndPos = new BlockAndPos(result.block,result.getBlockWorldPos());
        }

        if(blockAndPos.pos()!=null) {
            if (mouseInput.isLeftButtonPressed() && !clickL) {
                world.setAirBlock(blockAndPos.pos());
                clickL = true;
            } else if (!mouseInput.isLeftButtonPressed()) {
                clickL = false;
            }
        }

    }




    boolean clickL = false;
    boolean clickP = false;

    @Override
    public void render() {

        renderer.render(window, camera,hud, worldRenderer);


    }

    @Override
    public void cleanup() {
        world.save();
        renderer.cleanup();
        worldRenderer.cleanUp();
    }
}
