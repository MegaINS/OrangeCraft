package ru.megains.game

import org.joml.Vector3f
import org.lwjgl.LWJGLException
import org.lwjgl.input.{Keyboard, Mouse}
import org.lwjgl.opengl.{Display, DisplayMode, GL11, PixelFormat}
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockSize, BlockWorldPos}
import ru.megains.game.entity.player.EntityPlayer
import ru.megains.game.item.{Item, ItemBlock, ItemStack}
import ru.megains.game.managers.{GuiManager, TextureManager}
import ru.megains.game.multiblock.MultiBlockSingle
import ru.megains.game.util.{BlockAndPos, RayTraceResult, Timer}
import ru.megains.game.world.World
import ru.megains.game.world.storage.AnvilSaveFormat
import ru.megains.renderer.graph.Camera
import ru.megains.renderer.gui.{GuiInGameMenu, GuiMainMenu, GuiPlayerInventory}
import ru.megains.renderer.world.{RenderChunk, WorldRenderer}
import ru.megains.renderer.{EntityRenderer, FontRender, RenderItem}
import ru.megains.utils.Logger

import scala.reflect.io.Path


class OrangeCraft(ocDataDir: String) extends Logger[OrangeCraft] {



    var frames: Int = 0
    var lastFrames: Int = 0
    val MB: Double = 1024 * 1024
    val TARGET_FPS: Float = 60
    val timer: Timer = new Timer(20)
    var tick: Int = 0
    var running: Boolean = true


    val orangeCraft: OrangeCraft = this
    var world: World = _
    var renderer: EntityRenderer = _
    var itemRender: RenderItem = _
    var player: EntityPlayer = _
    var textureManager: TextureManager = _
    var guiManager: GuiManager = _
    var result: RayTraceResult = _
    var blockAndPos: BlockAndPos = _
    private var camera: Camera = _
    private var cameraInc: Vector3f = _
    private var worldRenderer: WorldRenderer = _
    var fontRender: FontRender = _
    var saveLoader: AnvilSaveFormat = _


    def startGame(): Unit = {
        log.info("Start Game")
        log.info("OrangeCraft v0.1.2")
        try {
            log.info("Display creating...")
            Display.setDisplayMode(new DisplayMode(800, 600))
            Display.create((new PixelFormat).withDepthBits(24))
            GL11.glClearColor(0.5f, 0.6f, 0.7f, 0.0F)
            log.info("Display create successful")
        } catch {
            case e: LWJGLException =>
                e.printStackTrace()
                System.exit(-1000)
        }


        saveLoader = new AnvilSaveFormat(Path(ocDataDir + "saves").toDirectory)

        log.info("Renderer creating...")
        renderer = new EntityRenderer(this)
        log.info("FontRender creating...")
        fontRender = new FontRender
        log.info("Camera creating...")
        camera = new Camera
        cameraInc = new Vector3f()
        log.info("GuiManager creating...")
        guiManager = new GuiManager(this)
        log.info("Blocks init...")
        Block.initBlocks()
        log.info("Items init...")
        Item.initItems()
        log.info("MultiBlockSingle init...")
        MultiBlockSingle.initMultiBlockSingle()
        log.info("TextureManager creating...")
        textureManager = new TextureManager

        renderer.init(textureManager)
        log.info("TextureManager loadTexture...")
        textureManager.loadTexture(TextureManager.locationBlockTexture, textureManager.textureMapBlock)

        log.info("RenderItem creating...")
        itemRender = new RenderItem(this)


        log.info("GuiManager init...")
        guiManager.init()
        log.info("EntityPlayer creating...")
        player = new EntityPlayer(world)


        guiManager.setGuiScreen(new GuiMainMenu())


    }

    def runGameLoop(): Unit = {

        if (Display.isCloseRequested && Display.isCreated) running = false


        timer.update()

        for (i <- 0 until timer.getTick) {
            update()
            tick += 1
        }


        render()
        frames += 1

    }

    def run(): Unit = {


        try {
            startGame()
        } catch {
            case e: Exception => log.fatal(e.printStackTrace())
                running = false

        }

        var lastTime: Long = System.currentTimeMillis
        timer.init()
        try {
            while (running) {


                runGameLoop()

                while (System.currentTimeMillis >= lastTime + 1000L) {
                    log.info(s"$frames fps, $tick tick, ${RenderChunk.chunkRender / (if (frames == 0) 1 else frames)} chunkRender, ${RenderChunk.chunkUpdate} chunkUpdate")

                    RenderChunk.chunkRender = 0
                    RenderChunk.chunkUpdate = 0
                    lastTime += 1000L
                    lastFrames = frames
                    frames = 0
                    tick = 0
                    //  printMemoryUsage()
                }
            }
        } catch {
            case e: Exception => log.fatal("Crash", e)
        } finally {
            cleanup()
        }
    }

    def setWorld(newWorld: World): Unit = {


        guiManager.setGuiScreen(null)
        if (world ne null) {
            world.save()
            worldRenderer.cleanUp()
        }

        if (newWorld ne null) {
            worldRenderer = new WorldRenderer(newWorld, textureManager)
            renderer.worldRenderer = worldRenderer
            player.setWorld(newWorld)
        }

        world = newWorld
    }



    private def printMemoryUsage(): Unit = {
        val r: Runtime = Runtime.getRuntime
        System.out.printf("Memory usage: total=" + r.totalMemory / MB + " MB, free=" + r.freeMemory / MB + " MB, max=" + r.maxMemory / MB + "f MB")
    }

    def grabMouseCursor(): Unit = {
        Mouse.setGrabbed(true)
    }

    def ungrabMouseCursor(): Unit = {
        Mouse.setCursorPosition(Display.getWidth / 2, Display.getHeight / 2)
        Mouse.setGrabbed(false)
    }

    private def sync(): Unit = {
        val loopSlot: Float = 1f / TARGET_FPS
        val endTime: Double = timer.getLastLoopTime + loopSlot
        while (timer.getTime < endTime) try
            Thread.sleep(1)

        catch {
            case ie: InterruptedException => {
            }
        }
    }

    private def update(): Unit = {

        cameraInc.set(0, 0, 0)

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) cameraInc.z = -1
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) cameraInc.z = 1
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) cameraInc.x = -1
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) cameraInc.x = 1
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) cameraInc.y = -1
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) cameraInc.y = 1


        if (guiManager.isGuiScreen) guiManager.handleInput()
        else {
            runTickKeyboard()
            runTickMouse()
        }

        if (world ne null) {
            world.update()
            if (!guiManager.isGuiScreen) player.turn(Mouse.getDX, Mouse.getDY)

            player.update(cameraInc.x, cameraInc.y, cameraInc.z)

            camera.setPosition(player.posX, player.posY + player.levelView, player.posZ)
            camera.setRotation(player.xRot, player.yRot, 0)
            player.inventory.changeStackSelect(Mouse.getDWheel * -1)


            result = player.rayTrace(5, 0.1f)


            guiManager.tick()


            if (result != null) {
                val stack: ItemStack = player.inventory.getStackSelect
                if (stack != null && stack.item.isInstanceOf[ItemBlock]) setBlock(Block.getBlockFromItem(stack.item))
                else breakBlock()
            }
            else blockAndPos = null

            if (blockAndPos != null) {
                worldRenderer.updateBlockBounds(blockAndPos)
            }

        }


    }

    private def render(): Unit = {

        log.debug("RENDER START")
        renderer.render(camera)
        log.debug("RENDER STOP")
    }

    private def setBlock(block: Block) {
        var posTarget: BlockWorldPos = result.getBlockWorldPos
        var posSet: BlockWorldPos = null
        if (block.isFullBlock) posSet = posTarget.sum(result.sideHit)
        else posSet = new BlockWorldPos(posTarget.sum(result.sideHit), BlockSize.get(result.hitVec.x), BlockSize.get(result.hitVec.y), BlockSize.get(result.hitVec.z))
        if (block.isFullBlock) if (world.isAirBlock(posSet)) blockAndPos = new BlockAndPos(block, posSet)
        else blockAndPos = null
        else {
            var x: Float = posSet.blockX.value
            var y: Float = posSet.blockY.value
            var z: Float = posSet.blockZ.value
            val bd: BlockDirection = result.sideHit
            if (bd eq BlockDirection.UP) y = 0
            else if (bd eq BlockDirection.EAST) x = 0
            else if (bd eq BlockDirection.SOUTH) z = 0
            else if (bd eq BlockDirection.DOWN) y = 1
            else if (bd eq BlockDirection.WEST) x = 1
            else if (bd eq BlockDirection.NORTH) z = 1
            if (x + block.getPhysicsBody.getMaxX > 1) x = 1 - block.getPhysicsBody.getMaxX
            if (y + block.getPhysicsBody.getMaxY > 1) y = 1 - block.getPhysicsBody.getMaxY
            if (z + block.getPhysicsBody.getMaxZ > 1) z = 1 - block.getPhysicsBody.getMaxZ
            posSet = new BlockWorldPos(posSet, BlockSize.get(x), BlockSize.get(y), BlockSize.get(z))
            if (world.getBlock(posTarget).isFullBlock) if (world.isAirBlock(posSet)) blockAndPos = new BlockAndPos(block, posSet)
            else if (world.getBlock(posSet).isCanPut(posSet, block)) blockAndPos = new BlockAndPos(block, posSet)
            else blockAndPos = null
            else {
                posTarget = new BlockWorldPos(posTarget, BlockSize.get(result.hitVec.x), BlockSize.get(result.hitVec.y), BlockSize.get(result.hitVec.z))
                var x1: Float = posTarget.blockX.value
                var y1: Float = posTarget.blockY.value
                var z1: Float = posTarget.blockZ.value
                if (bd eq BlockDirection.UP) if (y1 + block.getPhysicsBody.getMaxY > 1) y1 = 1 - block.getPhysicsBody.getMaxY
                else if (bd eq BlockDirection.DOWN) if (y1 - block.getPhysicsBody.getMaxY <= 0) y1 = 0
                else y1 = y1 - block.getPhysicsBody.getMaxY
                else if (bd eq BlockDirection.NORTH) if (z1 - block.getPhysicsBody.getMaxZ <= 0) z1 = 0
                else z1 = z1 - block.getPhysicsBody.getMaxZ
                else if (bd eq BlockDirection.SOUTH) if (z1 + block.getPhysicsBody.getMaxZ > 1) z1 = 1 - block.getPhysicsBody.getMaxZ
                else if (bd eq BlockDirection.WEST) if (x1 - block.getPhysicsBody.getMaxX <= 0) x1 = 0
                else x1 = x1 - block.getPhysicsBody.getMaxX
                else if (bd eq BlockDirection.EAST) if (x1 + block.getPhysicsBody.getMaxX > 1) x1 = 1 - block.getPhysicsBody.getMaxX
                if (x1 + block.getPhysicsBody.getMaxX > 1) x1 = 1 - block.getPhysicsBody.getMaxX
                if (y1 + block.getPhysicsBody.getMaxY > 1) y1 = 1 - block.getPhysicsBody.getMaxY
                if (z1 + block.getPhysicsBody.getMaxZ > 1) z1 = 1 - block.getPhysicsBody.getMaxZ
                posTarget = new BlockWorldPos(posTarget, BlockSize.get(x1), BlockSize.get(y1), BlockSize.get(z1))
                if (world.getBlock(posTarget).isCanPut(posTarget, block)) blockAndPos = new BlockAndPos(block, posTarget)
                else if (world.isAirBlock(posSet)) blockAndPos = new BlockAndPos(block, posSet)
                else if (world.getBlock(posSet).isCanPut(posSet, block)) blockAndPos = new BlockAndPos(block, posSet)
                else blockAndPos = null
            }
        }
    }

    private def breakBlock(): Unit = {
        if (result.block.isFullBlock) blockAndPos = new BlockAndPos(result.block, new BlockWorldPos(result.getBlockWorldPos))
        else blockAndPos = new BlockAndPos(result.block, result.getBlockWorldPos)
    }


    private def runTickMouse(): Unit = {
        while (Mouse.next) {
            val button: Int = Mouse.getEventButton
            val buttonState: Boolean = Mouse.getEventButtonState
            if (button == 1 && buttonState && blockAndPos != null) {
                val stack: ItemStack = player.inventory.getStackSelect
                if (stack != null) world.setBlock(blockAndPos.pos, blockAndPos.block)
            }
            if (button == 0 && buttonState && blockAndPos != null) {
                val stack: ItemStack = player.inventory.getStackSelect
                if (stack == null || !stack.item.isInstanceOf[ItemBlock]) world.setAirBlock(blockAndPos.pos)
            }
        }
    }

    private def runTickKeyboard(): Unit = {
        while (Keyboard.next) {
            if (Keyboard.getEventKeyState) {
                Keyboard.getEventKey match {
                    case Keyboard.KEY_E => guiManager.setGuiScreen(new GuiPlayerInventory(player))
                    case Keyboard.KEY_ESCAPE => guiManager.setGuiScreen(new GuiInGameMenu())
                    case _ =>
                }
            }
        }
    }

    private def cleanup(): Unit = {
        log.info("Game stopped...")
        running = false

        if (world != null) world.save()
        if (renderer != null) renderer.cleanup()
        if (worldRenderer != null) worldRenderer.cleanUp()
    }
}
