package ru.megains.common.network

import ru.megains.client.OrangeCraft
import ru.megains.client.network.PlayerControllerMP
import ru.megains.client.renderer.gui.{GuiDownloadTerrain, GuiScreen}
import ru.megains.client.world.WorldClient
import ru.megains.common.entity.player.{EntityPlayer, InventoryPlayer}
import ru.megains.common.network.play.client.CPacketPlayer
import ru.megains.common.network.play.server._
import ru.megains.common.position.ChunkPosition
import ru.megains.common.utils.{Logger, PacketThreadUtil}

class NetHandlerPlayClient(gameController: OrangeCraft, previousScreen: GuiScreen, val netManager: NetworkManager) extends INetHandlerPlayClient with Logger[NetHandlerPlayClient] {


    var clientWorldController: WorldClient = _
    var doneLoadingTerrain: Boolean = false


    def handleHeldItemChange(packetIn: SPacketHeldItemChange): Unit = {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)
        if (InventoryPlayer.isHotBar(packetIn.heldItemHotbarIndex)) this.gameController.player.inventory.stackSelect = packetIn.heldItemHotbarIndex
    }

    override def onDisconnect(msg: String): Unit = {

    }

    def sendPacket(packetIn: Packet[_ <: INetHandler]) {
        netManager.sendPacket(packetIn)
    }

    override def handleJoinGame(packetIn: SPacketJoinGame): Unit = {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)

        gameController.playerController = new PlayerControllerMP(gameController, this)
        clientWorldController = new WorldClient(this)
        gameController.loadWorld(clientWorldController)
        gameController.guiManager.setGuiScreen(new GuiDownloadTerrain(this))
        // gameController.player.setEntityId(packetIn.getPlayerId)
        //  currentServerMaxPlayers = packetIn.getMaxPlayers
        // gameController.player.setReducedDebug(packetIn.isReducedDebugInfo)

        //  gameController.gameSettings.sendSettingsToServer()
        //  netManager.sendPacket(new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer).writeString(ClientBrandRetriever.getClientModName)))
    }

    override def handlePlayerPosLook(packetIn: SPacketPlayerPosLook): Unit = {

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)
        val entityplayer: EntityPlayer = gameController.player
        var d0: Double = packetIn.x
        var d1: Double = packetIn.y
        var d2: Double = packetIn.z
        var f: Float = packetIn.yaw
        var f1: Float = packetIn.pitch
        if (packetIn.flags.contains(SPacketPlayerPosLook.EnumFlags.X)) d0 += entityplayer.posX
        else entityplayer.motionX = 0.0D
        if (packetIn.flags.contains(SPacketPlayerPosLook.EnumFlags.Y)) d1 += entityplayer.posY
        else entityplayer.motionY = 0.0D
        if (packetIn.flags.contains(SPacketPlayerPosLook.EnumFlags.Z)) d2 += entityplayer.posZ
        else entityplayer.motionZ = 0.0D
        if (packetIn.flags.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) f1 += entityplayer.rotationPitch
        if (packetIn.flags.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) f += entityplayer.rotationYaw
        entityplayer.setPositionAndRotation(d0, d1, d2, f, f1)
        // netManager.sendPacket(new CPacketConfirmTeleport(packetIn.getTeleportId))

        netManager.sendPacket(new CPacketPlayer.PositionRotation(entityplayer.posX, entityplayer.body.minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false))
        if (!doneLoadingTerrain) {
            gameController.player.prevPosX = gameController.player.posX
            gameController.player.prevPosY = gameController.player.posY
            gameController.player.prevPosZ = gameController.player.posZ
            doneLoadingTerrain = true
            gameController.guiManager.setGuiScreen(null)
        }
    }

    def handleChunkData(packetIn: SPacketChunkData): Unit = {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)


        val pos = new ChunkPosition(packetIn.chunkX, packetIn.chunkY, packetIn.chunkZ)

        clientWorldController.doPreChunk(pos, loadChunk = true)
        val chunk = clientWorldController.getChunk(pos)
        chunk.blockStorage = packetIn.blockStorage
        gameController.worldRenderer.reRender(chunk.position)

    }


    def handleBlockChange(packetIn: SPacketBlockChange) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)
        clientWorldController.invalidateRegionAndSetBlock(packetIn.blockPosition, packetIn.block)

    }

    def handleMultiBlockChange(packetIn: SPacketMultiBlockChange) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController)
        for (blockData <- packetIn.changedBlocks) {
            clientWorldController.invalidateRegionAndSetBlock(blockData.blockPosition, blockData.block)
        }
    }

    override def handleSetSlot(packetIn: SPacketSetSlot): Unit = {
        if (packetIn.slot == -1) {
            gameController.player.inventory.itemStack = packetIn.item
        } else {
            gameController.player.openContainer.putStackInSlot(packetIn.slot, packetIn.item)
        }

    }

    override def handleWindowItems(packetIn: SPacketWindowItems): Unit = {
        val openContainer = gameController.player.openContainer

        for (i <- packetIn.itemStacks.indices) {
            openContainer.putStackInSlot(i, packetIn.itemStacks(i))
        }
    }
}


