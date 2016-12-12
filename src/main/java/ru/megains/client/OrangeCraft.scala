package ru.megains.client

import java.util.concurrent.{Callable, Executors, FutureTask}

import com.google.common.util.concurrent.{Futures, ListenableFuture, ListenableFutureTask}
import org.joml.Vector3f
import org.lwjgl.input.{Keyboard, Mouse}
import org.lwjgl.opengl.{Display, DisplayMode, GL11, PixelFormat}
import org.lwjgl.{LWJGLException, Sys}
import ru.megains.client.entity.EntityPlayerSP
import ru.megains.client.network.PlayerControllerMP
import ru.megains.client.renderer.graph.Camera
import ru.megains.client.renderer.gui.{GuiInGameMenu, GuiPlayerInventory, GuiPlayerSelect}
import ru.megains.client.renderer.world.{RenderChunk, WorldRenderer}
import ru.megains.client.renderer.{EntityRenderer, FontRender, RenderItem}
import ru.megains.client.world.WorldClient
import ru.megains.common.EnumActionResult
import ru.megains.common.EnumActionResult.EnumActionResult
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos, BlockSize}
import ru.megains.common.entity.Entity
import ru.megains.common.item.{ItemBlock, ItemStack}
import ru.megains.common.managers.{GuiManager, TextureManager}
import ru.megains.common.network.play.server.SPacketPlayerPosLook
import ru.megains.common.register.Bootstrap
import ru.megains.common.util.{RayTraceResult, Timer}
import ru.megains.common.utils.{IThreadListener, Logger, Util}
import ru.megains.common.world.storage.AnvilSaveFormat

import scala.collection.mutable
import scala.reflect.io.Path


class OrangeCraft(ocDataDir: String) extends Logger[OrangeCraft] with IThreadListener {



    var frames: Int = 0
    var lastFrames: Int = 0
    val MB: Double = 1024 * 1024
    val TARGET_FPS: Float = 60
    val timer: Timer = new Timer(20)
    var tick: Int = 0
    var running: Boolean = true
    val ocThread: Thread = Thread.currentThread
    val scheduledTasks: mutable.Queue[FutureTask[_]] = new mutable.Queue[FutureTask[_]]
    var playerController: PlayerControllerMP = _
    val orangeCraft: OrangeCraft = this
    var world: WorldClient = _
    var renderer: EntityRenderer = _
    var itemRender: RenderItem = _
    var player: EntityPlayerSP = _
    var textureManager: TextureManager = _
    var guiManager: GuiManager = _
    var objectMouseOver: RayTraceResult = _
    var blockSelectPosition: BlockPos = _
    var camera: Camera = _
    var cameraInc: Vector3f = _
    var worldRenderer: WorldRenderer = _
    var fontRender: FontRender = _
    var saveLoader: AnvilSaveFormat = _
    var renderViewEntity: Entity = _
    var rightClickDelayTimer = 0
    var playerName: String = "Null"


    def startGame(): Unit = {

        log.info(SPacketPlayerPosLook.EnumFlags.Z.id)

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


        Bootstrap.init()

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

        log.info("TextureManager creating...")
        textureManager = new TextureManager


        renderer.init(textureManager)
        log.info("TextureManager loadTexture...")
        textureManager.loadTexture(TextureManager.locationBlockTexture, textureManager.textureMapBlock)

        log.info("RenderItem creating...")
        itemRender = new RenderItem(this)


        log.info("GuiManager init...")
        guiManager.init()




        guiManager.setGuiScreen(new GuiPlayerSelect())


    }

    def runGameLoop(): Unit = {

        if (Display.isCloseRequested && Display.isCreated) running = false




        timer.update()


        scheduledTasks synchronized {
            while (scheduledTasks.nonEmpty) Util.runTask(scheduledTasks.dequeue(), log)
        }


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
                sync()
                while (System.currentTimeMillis >= lastTime + 1000L) {
                    // log.info(s"$frames fps, $tick tick, ${RenderChunk.chunkRender / (if (frames == 0) 1 else frames)} chunkRender, ${RenderChunk.chunkUpdate} chunkUpdate")

                    RenderChunk.chunkRender = 0
                    RenderChunk.chunkUpdate = 0
                    lastTime += 1000L
                    lastFrames = frames
                    frames = 0
                    tick = 0
                    // printMemoryUsage()
                }
            }
        } catch {
            case e: Exception => log.fatal("Crash", e)
        } finally {
            cleanup()
        }
    }


    def loadWorld(newWorld: WorldClient): Unit = {



        if (world ne null) {
            world.save()
            worldRenderer.cleanUp()
        }
        renderViewEntity = null
        if (newWorld ne null) {
            worldRenderer = new WorldRenderer(newWorld, textureManager)
            renderer.worldRenderer = worldRenderer

            if (player eq null) {
                player = playerController.createClientPlayer(newWorld)
                // playerController.flipPlayer(player)
            }
            renderViewEntity = player
            newWorld.spawnEntityInWorld(player)

        } else {
            if (player ne null) player.connection.netManager.closeChannel("exit")
            player = null
        }



        world = newWorld
    }



    private def printMemoryUsage(): Unit = {
        val r: Runtime = Runtime.getRuntime
        System.out.println("Memory usage: total=" + r.totalMemory / MB + " MB, free=" + r.freeMemory / MB + " MB, max=" + r.maxMemory / MB + "f MB")
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


        if (rightClickDelayTimer > 0) rightClickDelayTimer -= 1


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
        guiManager.tick()



        if (world ne null) {
            world.update()
            if (!guiManager.isGuiScreen) player.turn(Mouse.getDX, Mouse.getDY)

            player.update(cameraInc.x, cameraInc.y, cameraInc.z)

            camera.setPosition(player.posX toFloat, player.posY + player.levelView toFloat, player.posZ toFloat)
            camera.setRotation(player.rotationPitch, player.rotationYaw, 0)
            player.inventory.changeStackSelect(Mouse.getDWheel * -1)


            objectMouseOver = player.rayTrace(5, 0.1f)





            if (objectMouseOver != null) {
                worldRenderer.updateBlockMouseOver(objectMouseOver.blockPos, objectMouseOver.block)

                val stack: ItemStack = player.inventory.getStackSelect
                if (stack != null && stack.item.isInstanceOf[ItemBlock]) setBlock(Block.getBlockFromItem(stack.item))
                else blockSelectPosition = null

            }
            else blockSelectPosition = null



            if (blockSelectPosition != null) {
                worldRenderer.updateBlockSelect(blockSelectPosition, player.inventory.getStackSelect.item.asInstanceOf[ItemBlock].block)
            }

        }


    }

    private def render(): Unit = {

        log.debug("RENDER START")
        renderer.render(camera)
        log.debug("RENDER STOP")
    }

    private def setBlock(block: Block) {
        var posTarget: BlockPos = objectMouseOver.getBlockWorldPos
        var posSet: BlockPos = null
        if (block.isFullBlock) posSet = posTarget.sum(objectMouseOver.sideHit)
        else posSet = new BlockPos(posTarget.sum(objectMouseOver.sideHit), BlockSize.get(objectMouseOver.hitVec.x), BlockSize.get(objectMouseOver.hitVec.y), BlockSize.get(objectMouseOver.hitVec.z))
        if (block.isFullBlock) if (world.isAirBlock(posSet)) blockSelectPosition = posSet
        else blockSelectPosition = null
        else {
            var x: Float = posSet.blockX.value
            var y: Float = posSet.blockY.value
            var z: Float = posSet.blockZ.value
            val bd: BlockDirection = objectMouseOver.sideHit
            if (bd eq BlockDirection.UP) y = 0
            else if (bd eq BlockDirection.EAST) x = 0
            else if (bd eq BlockDirection.SOUTH) z = 0
            else if (bd eq BlockDirection.DOWN) y = 1
            else if (bd eq BlockDirection.WEST) x = 1
            else if (bd eq BlockDirection.NORTH) z = 1
            if (x + block.getPhysicsBody.getMaxX > 1) x = 1 - block.getPhysicsBody.getMaxX
            if (y + block.getPhysicsBody.getMaxY > 1) y = 1 - block.getPhysicsBody.getMaxY
            if (z + block.getPhysicsBody.getMaxZ > 1) z = 1 - block.getPhysicsBody.getMaxZ
            posSet = new BlockPos(posSet, BlockSize.get(x), BlockSize.get(y), BlockSize.get(z))
            if (world.getMultiBlock(posTarget).isFullBlock) if (world.isAirBlock(posSet)) blockSelectPosition = posSet
            else if (world.getMultiBlock(posSet).isCanPut(posSet, block)) blockSelectPosition = posSet
            else blockSelectPosition = null
            else {
                posTarget = new BlockPos(posTarget, BlockSize.get(objectMouseOver.hitVec.x), BlockSize.get(objectMouseOver.hitVec.y), BlockSize.get(objectMouseOver.hitVec.z))
                var x1: Float = posTarget.blockX.value
                var y1: Float = posTarget.blockY.value
                var z1: Float = posTarget.blockZ.value
                if (bd eq BlockDirection.UP) {
                    if (y1 + block.getPhysicsBody.getMaxY > 1) y1 = 1 - block.getPhysicsBody.getMaxY
                } else if (bd eq BlockDirection.DOWN) {
                    if (y1 - block.getPhysicsBody.getMaxY <= 0) y1 = 0
                    else y1 = y1 - block.getPhysicsBody.getMaxY
                } else if (bd eq BlockDirection.NORTH) {
                    if (z1 - block.getPhysicsBody.getMaxZ <= 0) z1 = 0
                    else z1 = z1 - block.getPhysicsBody.getMaxZ
                } else if (bd eq BlockDirection.SOUTH) {
                    if (z1 + block.getPhysicsBody.getMaxZ > 1) z1 = 1 - block.getPhysicsBody.getMaxZ
                } else if (bd eq BlockDirection.WEST) {
                    if (x1 - block.getPhysicsBody.getMaxX <= 0) x1 = 0
                    else x1 = x1 - block.getPhysicsBody.getMaxX
                } else if (bd eq BlockDirection.EAST) if (x1 + block.getPhysicsBody.getMaxX > 1) x1 = 1 - block.getPhysicsBody.getMaxX
                if (x1 + block.getPhysicsBody.getMaxX > 1) x1 = 1 - block.getPhysicsBody.getMaxX
                if (y1 + block.getPhysicsBody.getMaxY > 1) y1 = 1 - block.getPhysicsBody.getMaxY
                if (z1 + block.getPhysicsBody.getMaxZ > 1) z1 = 1 - block.getPhysicsBody.getMaxZ
                posTarget = new BlockPos(posTarget, BlockSize.get(x1), BlockSize.get(y1), BlockSize.get(z1))
                if (world.getMultiBlock(posTarget).isCanPut(posTarget, block)) blockSelectPosition = posTarget
                else if (world.isAirBlock(posSet)) blockSelectPosition = posSet
                else if (world.getMultiBlock(posSet).isCanPut(posSet, block)) blockSelectPosition = posSet
                else blockSelectPosition = null
            }
        }
    }




    private def runTickMouse(): Unit = {
        while (Mouse.next) {
            val button: Int = Mouse.getEventButton
            val buttonState: Boolean = Mouse.getEventButtonState

            if (button == 1 && buttonState) {
                rightClickMouse()
            }


            //            if (button == 1 && buttonState && blockAndPos != null) {
            //                val stack: ItemStack = player.inventory.getStackSelect
            //                if (stack != null) world.setBlock(blockAndPos.pos, blockAndPos.block,0)
            //            }
            if (button == 0 && buttonState && objectMouseOver != null) {
                // val stack: ItemStack = player.inventory.getStackSelect
                // if (stack == null || !stack.item.isInstanceOf[ItemBlock])
                playerController.clickBlock(objectMouseOver.blockPos, BlockDirection.DOWN)
                // if (stack == null || !stack.item.isInstanceOf[ItemBlock]) world.setAirBlock(blockAndPos.pos)
            }
        }
    }

    def runTickKeyboard(): Unit = {
        while (Keyboard.next) {
            if (Keyboard.getEventKeyState) {
                Keyboard.getEventKey match {
                    case Keyboard.KEY_E => guiManager.setGuiScreen(new GuiPlayerInventory(player))
                    case Keyboard.KEY_ESCAPE => guiManager.setGuiScreen(new GuiInGameMenu())
                    case Keyboard.KEY_R => worldRenderer.reRenderWorld()
                    case _ =>
                }
            }
        }
    }


    //    def runTickKeyboard(): Unit = {
    //        while (Keyboard.next) {
    //            val i: Int = if (Keyboard.getEventKey == 0) Keyboard.getEventCharacter + 256
    //            else Keyboard.getEventKey
    //            if (debugCrashKeyPressTime > 0L) {
    //                if (getSystemTime - debugCrashKeyPressTime >= 6000L) throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable))
    //                if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) debugCrashKeyPressTime = -1L
    //            }
    //            else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
    //                actionKeyF3 = true
    //                debugCrashKeyPressTime = getSystemTime
    //            }
    //            dispatchKeypresses()
    //            if (currentScreen != null) currentScreen.handleKeyboardInput()
    //            val flag: Boolean = Keyboard.getEventKeyState
    //            if (flag) {
    //                if (i == 62 && entityRenderer != null) entityRenderer.switchUseShader()
    //                var flag1: Boolean = false
    //                if (currentScreen == null) {
    //                    if (i == 1) displayInGameMenu()
    //                    flag1 = Keyboard.isKeyDown(61) && processKeyF3(i)
    //                    actionKeyF3 |= flag1
    //                    if (i == 59) gameSettings.hideGUI = !gameSettings.hideGUI
    //                }
    //                if (flag1) KeyBinding.setKeyBindState(i, false)
    //                else {
    //                    KeyBinding.setKeyBindState(i, true)
    //                    KeyBinding.onTick(i)
    //                }
    //                if (gameSettings.showDebugProfilerChart) {
    //                    if (i == 11) updateDebugProfilerName(0)
    //                    var j: Int = 0
    //                    while (j < 9) {
    //                        {
    //                            if (i == 2 + j) updateDebugProfilerName(j + 1)
    //                        }
    //                        {
    //                            j += 1; j
    //                        }
    //                    }
    //                }
    //            }
    //            else {
    //                KeyBinding.setKeyBindState(i, false)
    //                if (i == 61) if (actionKeyF3) actionKeyF3 = false
    //                else {
    //                    gameSettings.showDebugInfo = !gameSettings.showDebugInfo
    //                    gameSettings.showDebugProfilerChart = gameSettings.showDebugInfo && GuiScreen.isShiftKeyDown
    //                    gameSettings.showLagometer = gameSettings.showDebugInfo && GuiScreen.isAltKeyDown
    //                }
    //            }
    //            net.minecraftforge.fml.common.FMLCommonHandler.instance.fireKeyInput()
    //        }
    //        processKeyBinds()
    //    }

    def processKeyBinds() {
        //        while (gameSettings.keyBindTogglePerspective.isPressed) {
        //            {
        //                gameSettings.thirdPersonView += 1
        //                if (gameSettings.thirdPersonView > 2) gameSettings.thirdPersonView = 0
        //                if (gameSettings.thirdPersonView == 0) entityRenderer.loadEntityShader(getRenderViewEntity)
        //                else if (gameSettings.thirdPersonView == 1) entityRenderer.loadEntityShader(null.asInstanceOf[Entity])
        //            }
        //            renderGlobal.setDisplayListEntitiesDirty()
        //        }
        //        while (gameSettings.keyBindSmoothCamera.isPressed) gameSettings.smoothCamera = !gameSettings.smoothCamera
        //        var i: Int = 0
        //        while (i < 9) {
        //            {
        //                if (gameSettings.keyBindsHotbar(i).isPressed) if (player.isSpectator) ingameGUI.getSpectatorGui.onHotbarSelected(i)
        //                else player.inventory.currentItem = i
        //            }
        //            {
        //                i += 1; i
        //            }
        //        }
        //        while (gameSettings.keyBindInventory.isPressed) {
        //            getConnection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.OPEN_INVENTORY_ACHIEVEMENT))
        //            if (playerController.isRidingHorse) player.sendHorseInventory()
        //            else displayGuiScreen(new GuiInventory(player))
        //        }
        // while (gameSettings.keyBindSwapHands.isPressed) if (!player.isSpectator) getConnection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN))
        //  while (gameSettings.keyBindDrop.isPressed) if (!player.isSpectator) player.dropItem(GuiScreen.isCtrlKeyDown)
        //        val flag: Boolean = gameSettings.chatVisibility ne EntityPlayer.EnumChatVisibility.HIDDEN
        //        if (flag) {
        //            while (gameSettings.keyBindChat.isPressed) displayGuiScreen(new GuiChat)
        //            if (currentScreen == null && gameSettings.keyBindCommand.isPressed) displayGuiScreen(new GuiChat("/"))
        //        }
        //        if (player.isHandActive) {
        //            if (!gameSettings.keyBindUseItem.isKeyDown) playerController.onStoppedUsingItem(player)
        //            label472 //todo: labels is not supported
        //            while (true) if (!gameSettings.keyBindAttack.isPressed) {
        //                while (gameSettings.keyBindUseItem.isPressed) {
        //                }
        //                while (true) {
        //                    if (gameSettings.keyBindPickBlock.isPressed) continue //todo: continue is not supported
        //                    break label472 // todo: label break is not supported
        //                }
        //            }
        //        }
        //  else {
        //  while (gameSettings.keyBindAttack.isPressed) clickMouse()
        //   while (gameSettings.keyBindUseItem.isPressed) rightClickMouse()

        //  while (gameSettings.keyBindPickBlock.isPressed) middleClickMouse()
        // }
        //  if (gameSettings.keyBindUseItem.isKeyDown && rightClickDelayTimer == 0 && !player.isHandActive) rightClickMouse()
        //  sendClickBlockToController(currentScreen == null && gameSettings.keyBindAttack.isKeyDown && inGameHasFocus)
    }


    def rightClickMouse() {
        if (!playerController.isHittingBlock) {
            rightClickDelayTimer = 4
            val itemstack: ItemStack = player.getHeldItem
            if (blockSelectPosition == null) {
                // log.warn("Null returned as \'hitResult\', this shouldn\'t happen!")
            } else {
                objectMouseOver.typeOfHit match {
                    case RayTraceResult.Type.ENTITY =>
                    //                        if (playerController.interactWithEntity(player, objectMouseOver.entityHit, objectMouseOver, player.getHeldItem(enumhand), enumhand) eq EnumActionResult.SUCCESS) return
                    //                        if (playerController.interactWithEntity(player, objectMouseOver.entityHit, player.getHeldItem(enumhand), enumhand) eq EnumActionResult.SUCCESS) return

                    case RayTraceResult.Type.BLOCK =>
                        val blockpos: BlockPos = objectMouseOver.blockPos
                        if (!world.isAirBlock(blockpos)) {
                            val i: Int = if (itemstack != null) itemstack.stackSize
                            else 0
                            val enumactionresult: EnumActionResult = playerController.processRightClickBlock(player, world, itemstack, blockpos, objectMouseOver.sideHit, objectMouseOver.hitVec)
                            if (enumactionresult eq EnumActionResult.SUCCESS) {
                                //   player.swingArm()
                                if (itemstack != null) if (itemstack.stackSize == 0) player.setHeldItem(null)
                                //    else if (itemstack.stackSize != i || playerController.isInCreativeMode) entityRenderer.itemRenderer.resetEquippedProgress()
                                return
                            }
                        }
                    case _ =>
                }
            }
            val itemstack1: ItemStack = player.getHeldItem
            if (itemstack1 != null && (playerController.processRightClick(player, world, itemstack1) eq EnumActionResult.SUCCESS)) {
                //   entityRenderer.itemRenderer.resetEquippedProgress()
                return
            }

        }
    }





    private def cleanup(): Unit = {
        log.info("Game stopped...")
        running = false

        if (world != null) world.save()
        if (renderer != null) renderer.cleanup()
        if (guiManager ne null) guiManager.cleanup()
        if (worldRenderer != null) worldRenderer.cleanUp()

    }

    def addScheduledTask[V](callableToSchedule: Callable[V]): ListenableFuture[V] = {
        if (isCallingFromMinecraftThread) try
            Futures.immediateFuture[V](callableToSchedule.call)

        catch {
            case exception: Exception => {
                Futures.immediateFailedCheckedFuture(exception)
            }
        }
        else {
            val listenablefuturetask: ListenableFutureTask[V] = ListenableFutureTask.create[V](callableToSchedule)
            scheduledTasks synchronized {
                scheduledTasks += listenablefuturetask
                return listenablefuturetask
            }
        }
    }


    override def addScheduledTask(runnableToSchedule: Runnable): ListenableFuture[AnyRef] = {
        addScheduledTask[AnyRef](Executors.callable(runnableToSchedule))
    }

    override def isCallingFromMinecraftThread: Boolean = Thread.currentThread eq ocThread


}

object OrangeCraft {
    def getSystemTime(): Long = Sys.getTime * 1000L / Sys.getTimerResolution
}
