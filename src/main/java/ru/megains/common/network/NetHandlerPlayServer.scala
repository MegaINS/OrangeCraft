package ru.megains.common.network


import com.sun.javafx.geom.Vec3d
import ru.megains.common.block.blockdata.{BlockDirection, BlockPos}
import ru.megains.common.entity.player.InventoryPlayer
import ru.megains.common.item.ItemStack
import ru.megains.common.network.play.INetHandlerPlayServer
import ru.megains.common.network.play.client.CPacketPlayerDigging.Action._
import ru.megains.common.network.play.client._
import ru.megains.common.network.play.server.{SPacketBlockChange, SPacketPlayerPosLook}
import ru.megains.common.utils.{Logger, PacketThreadUtil}
import ru.megains.server.OrangeCraftServer
import ru.megains.server.entity.EntityPlayerMP
import ru.megains.server.world.WorldServer

import scala.collection.immutable.HashSet

class NetHandlerPlayServer(server: OrangeCraftServer, val networkManager: NetworkManager, playerEntity: EntityPlayerMP) extends INetHandlerPlayServer with Logger[NetHandlerPlayServer] {

    networkManager.setNetHandler(this)

    playerEntity.connection = this


    var targetPos: Vec3d = _
    private var lastGoodX: Double = .0
    private var lastGoodY: Double = .0
    private var lastGoodZ: Double = .0
    private var firstGoodX: Double = .0
    private var firstGoodY: Double = .0
    private var firstGoodZ: Double = .0
    // PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getServerWorld)

    def sendPacket(packetIn: Packet[_ <: INetHandler]) {

        try
            networkManager.sendPacket(packetIn)

        catch {
            case throwable: Throwable =>
                log.error("sendPacket", throwable)
        }
    }

    def setPlayerLocation(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
        setPlayerLocation(x, y, z, yaw, pitch, new HashSet[SPacketPlayerPosLook.EnumFlags.EnumFlags])
    }

    var teleportId: Int = 0


    def setPlayerLocation(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, relativeSet: HashSet[SPacketPlayerPosLook.EnumFlags.EnumFlags]) {


        val d0: Double = if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X)) playerEntity.posX
        else 0.0D
        val d1: Double = if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y)) playerEntity.posY
        else 0.0D
        val d2: Double = if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Z)) playerEntity.posZ
        else 0.0D
        targetPos = new Vec3d(x + d0, y + d1, z + d2)
        var f: Float = yaw
        var f1: Float = pitch
        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) f = yaw + playerEntity.rotationYaw
        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) f1 = pitch + playerEntity.rotationPitch
        if ( {
            teleportId += 1; teleportId
        } == Integer.MAX_VALUE) teleportId = 0
        //lastPositionUpdate = networkTickCount
        playerEntity.setPositionAndRotation(targetPos.x, targetPos.y, targetPos.z, f, f1)
        playerEntity.connection.sendPacket(new SPacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet, teleportId))
    }


    override def onDisconnect(msg: String): Unit = {

        log.info("{} lost connection: {}", Array[AnyRef](playerEntity.name, msg))
        //  this.serverController.refreshStatusNextTick()
        //  val textcomponenttranslation: TextComponentTranslation = new TextComponentTranslation("multiplayer.player.left", Array[AnyRef](this.playerEntity.getDisplayName))
        //   textcomponenttranslation.getStyle.setColor(TextFormatting.YELLOW)
        // this.serverController.getPlayerList.sendChatMsg(textcomponenttranslation)
        //  this.playerEntity.mountEntityAndWakeUp()
        server.playerList.playerLoggedOut(playerEntity)

        //        if (this.serverController.isSinglePlayer && this.playerEntity.getName == this.serverController.getServerOwner) {
        //            LOGGER.info("Stopping singleplayer server as player logged out")
        //            this.serverController.initiateShutdown()
        //        }
    }

    override def processPlayer(packetIn: CPacketPlayer): Unit = {


        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getWorldServer)

        //   if (isMovePlayerPacketInvalid(packetIn)) kickPlayerFromServer("Invalid move player packet received")
        //  else {
        val worldserver: WorldServer = server.world // serverController.worldServerForDimension(playerEntity.dimension)
        //  if (!playerEntity.playerConqueredTheEnd) {
        //  if (networkTickCount == 0) captureCurrentPosition()
        //                if (targetPos != null) if (networkTickCount - lastPositionUpdate > 20) {
        //                  //  lastPositionUpdate = networkTickCount
        //                    setPlayerLocation(targetPos.x, targetPos.y, targetPos.z, playerEntity.rotationYaw, playerEntity.rotationPitch)
        //                }
        //                else {
        //                   // lastPositionUpdate = networkTickCount
        //                    if (playerEntity.isRiding) {
        //                        playerEntity.setPositionAndRotation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, packetIn.getYaw(this.playerEntity.rotationYaw), packetIn.getPitch(this.playerEntity.rotationPitch))
        //                        server.playerList.serverUpdateMountedMovingPlayer(playerEntity)
        //                    }
        //                    else {
        //  val d0: Double = playerEntity.posX
        //  val d1: Double = playerEntity.posY
        //   val d2: Double = playerEntity.posZ
        //  val d3: Double = playerEntity.posY
        val d4: Double = packetIn.getX(playerEntity.posX)
        val d5: Double = packetIn.getY(playerEntity.posY)
        val d6: Double = packetIn.getZ(playerEntity.posZ)
        val f: Float = packetIn.getYaw(playerEntity.rotationYaw)
        val f1: Float = packetIn.getPitch(playerEntity.rotationPitch)
        var d7: Double = d4 - firstGoodX
        var d8: Double = d5 - firstGoodY
        var d9: Double = d6 - firstGoodZ
        //  val d10: Double = playerEntity.motionX * playerEntity.motionX + playerEntity.motionY * playerEntity.motionY + this.playerEntity.motionZ * this.playerEntity.motionZ
        //  var d11: Double = d7 * d7 + d8 * d8 + d9 * d9
        //   movePacketCounter += 1
        //                        var i: Int = movePacketCounter - lastMovePacketCounter
        //                        if (i > 5) {
        //                            log.debug("{} is sending move packets too frequently ({} packets since last tick)", Array[AnyRef](this.playerEntity.getName, Integer.valueOf(i)))
        //                            i = 1
        //                        }
        //                        if (!this.playerEntity.isInvulnerableDimensionChange && (!playerEntity.getServerWorld.getGameRules.getBoolean("disableElytraMovementCheck") || !this.playerEntity.isElytraFlying)) {
        //                            val f2: Float = if (playerEntity.isElytraFlying) 300.0F
        //                            else 100.0F
        //                            if (d11 - d10 > (f2 * i.toFloat).toDouble && (!serverController.isSinglePlayer || !serverController.getServerOwner == this.playerEntity.getName)) {
        //                                log.warn("{} moved too quickly! {},{},{}", Array[AnyRef](playerEntity.getName, Double.valueOf(d7), Double.valueOf(d8), Double.valueOf(d9)))
        //                                setPlayerLocation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, this.playerEntity.rotationPitch)
        //                                return
        //                            }
        //                        }
        //   val flag2: Boolean = worldserver.getCollisionBoxes(playerEntity, playerEntity.getEntityBoundingBox.contract(0.0625D)).isEmpty
        d7 = d4 - lastGoodX
        d8 = d5 - lastGoodY
        d9 = d6 - lastGoodZ
        //   if (playerEntity.onGround && !packetIn.isOnGround && d8 > 0.0D) playerEntity.jump()
        playerEntity.move(d7, d8, d9)
        //    playerEntity.moveEntity(d7, d8, d9)
        //  playerEntity.onGround = packetIn.isOnGround
        //  val d12: Double = d8
        d7 = d4 - playerEntity.posX
        d8 = d5 - playerEntity.posY
        if (d8 > -0.5D || d8 < 0.5D) d8 = 0.0D
        d9 = d6 - playerEntity.posZ
        //   d11 = d7 * d7 + d8 * d8 + d9 * d9
        //  var flag: Boolean = false
        //                        if (!playerEntity.isInvulnerableDimensionChange && d11 > 0.0625D && !playerEntity.isPlayerSleeping && !this.playerEntity.interactionManager.isCreative && (this.playerEntity.interactionManager.getGameType ne GameType.SPECTATOR)) {
        //                            flag = true
        //                            log.warn("{} moved wrongly!", Array[AnyRef](playerEntity.name))
        //                        }
        playerEntity.setPositionAndRotation(d4, d5, d6, f, f1)
        //                        playerEntity.addMovementStat(playerEntity.posX - d0, playerEntity.posY - d1, playerEntity.posZ - d2)
        //                        if (!playerEntity.noClip && !playerEntity.isPlayerSleeping) {
        //                            val flag1: Boolean = worldserver.getCollisionBoxes(playerEntity, playerEntity.getEntityBoundingBox.contract(0.0625D)).isEmpty
        //                            if (flag2 && (flag || !flag1)) {
        //                                setPlayerLocation(d0, d1, d2, f, f1)
        //                                return
        //                            }
        //                        }
        //   floating = d12 >= -0.03125D
        //   floating &= !serverController.isFlightAllowed && !playerEntity.capabilities.allowFlying
        //  floating &= !playerEntity.isPotionActive(MobEffects.LEVITATION) && !playerEntity.isElytraFlying && !worldserver.checkBlockCollision(this.playerEntity.getEntityBoundingBox.expandXyz(0.0625D).addCoord(0.0D, -0.55D, 0.0D))
        //   playerEntity.onGround = packetIn.isOnGround
        server.playerList.serverUpdateMountedMovingPlayer(playerEntity)
        //  playerEntity.handleFalling(playerEntity.posY - d3, packetIn.isOnGround)
        lastGoodX = playerEntity.posX
        lastGoodY = playerEntity.posY
        lastGoodZ = playerEntity.posZ
        // }
        //  }
        //      }
        //  }
    }

    override def processPlayerDigging(packetIn: CPacketPlayerDigging): Unit = {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getWorldServer)
        val worldserver: WorldServer = server.world
        val blockpos: BlockPos = packetIn.position
        playerEntity.markPlayerActive()
        packetIn.action match {
            //            case SWAP_HELD_ITEMS =>
            //                if (!this.playerEntity.isSpectator) {
            //                    val itemstack1: ItemStack = this.playerEntity.getHeldItem(EnumHand.OFF_HAND)
            //                    this.playerEntity.setHeldItem(EnumHand.OFF_HAND, this.playerEntity.getHeldItem(EnumHand.MAIN_HAND))
            //                    this.playerEntity.setHeldItem(EnumHand.MAIN_HAND, itemstack1)
            //                }
            //            case DROP_ITEM =>
            //                if (!this.playerEntity.isSpectator) this.playerEntity.dropItem(false)
            //            case DROP_ALL_ITEMS =>
            //                if (!this.playerEntity.isSpectator) this.playerEntity.dropItem(true)
            //            case RELEASE_USE_ITEM =>
            //                this.playerEntity.stopActiveHand()
            //                val itemstack: ItemStack = this.playerEntity.getHeldItemMainhand
            //                if (itemstack != null && itemstack.stackSize == 0) this.playerEntity.setHeldItem(EnumHand.MAIN_HAND, null.asInstanceOf[ItemStack])

            case START_DESTROY_BLOCK | ABORT_DESTROY_BLOCK | STOP_DESTROY_BLOCK =>

                val d0: Double = playerEntity.posX - (blockpos.worldX.toDouble + 0.5D)
                val d1: Double = playerEntity.posY - (blockpos.worldY.toDouble + 0.5D) + 1.5D
                val d2: Double = playerEntity.posZ - (blockpos.worldZ.toDouble + 0.5D)
                val d3: Double = d0 * d0 + d1 * d1 + d2 * d2
                var dist: Double = playerEntity.interactionManager.getBlockReachDistance + 1
                dist *= dist
                if (d3 > dist) {

                    //  } else if (blockpos.getY >= serverController.getBuildLimit){

                } else {
                    if (packetIn.action eq START_DESTROY_BLOCK) /* if (!serverController.isBlockProtected(worldserver, blockpos, playerEntity) && worldserver.getWorldBorder.contains(blockpos))*/ playerEntity.interactionManager.onBlockClicked(blockpos, packetIn.facing)
                    //  else playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos))
                    else {
                        if (packetIn.action eq STOP_DESTROY_BLOCK) playerEntity.interactionManager.blockRemoving(blockpos)
                        else if (packetIn.action eq ABORT_DESTROY_BLOCK) playerEntity.interactionManager.cancelDestroyingBlock()
                        if (worldserver.isAirBlock(blockpos)) playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos))
                    }
                }
            case _ =>
                throw new IllegalArgumentException("Invalid player action")
        }
    }

    override def processHeldItemChange(packetIn: CPacketHeldItemChange) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getWorldServer)
        if (packetIn.slotId >= 0 && packetIn.slotId < InventoryPlayer.hotBarSize) {
            playerEntity.inventory.stackSelect = packetIn.slotId
            playerEntity.markPlayerActive()
        }
        else log.warn("{} tried to set an invalid carried item", Array[AnyRef](playerEntity.name))
    }

    def processPlayerBlockPlacement(packetIn: CPacketPlayerTryUseItem) {

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getWorldServer)
        val worldserver: WorldServer = server.world

        var itemstack: ItemStack = this.playerEntity.getHeldItem
        playerEntity.markPlayerActive()
        if (itemstack != null) {
            playerEntity.interactionManager.processRightClick(playerEntity, worldserver, itemstack)
            itemstack = this.playerEntity.getHeldItem
            if (itemstack != null && itemstack.stackSize == 0) {
                playerEntity.setHeldItem(null)
                itemstack = null
            }
        }
    }

    def processRightClickBlock(packetIn: CPacketPlayerTryUseItemOnBlock) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.playerEntity.getWorldServer)
        val worldserver: WorldServer = server.world

        var itemstack: ItemStack = this.playerEntity.getHeldItem
        val posMouseOver: BlockPos = packetIn.posMouseOver
        val posBlockSet: BlockPos = packetIn.posBlockSet
        val enumfacing: BlockDirection = packetIn.placedBlockDirection
        playerEntity.markPlayerActive()
        //  if (/*blockpos.getY < this.serverController.getBuildLimit - 1 ||*/ /*(enumfacing ne EnumFacing.UP) &&*/ blockpos.getY < this.serverController.getBuildLimit) {
        var dist: Double = playerEntity.interactionManager.getBlockReachDistance + 3
        dist *= dist
        //  if (targetPos == null && playerEntity.getDistanceSq(blockpos.getX.toDouble + 0.5D, blockpos.getY.toDouble + 0.5D, blockpos.getZ.toDouble + 0.5D) < dist && !this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity) && worldserver.getWorldBorder.contains(blockpos))
        playerEntity.interactionManager.processRightClickBlock(this.playerEntity, worldserver, itemstack, posMouseOver, posBlockSet, enumfacing, packetIn.facingX, packetIn.facingY, packetIn.facingZ)
        // }
        // else {
        //  val textcomponenttranslation: TextComponentTranslation = new TextComponentTranslation("build.tooHigh", Array[AnyRef](Integer.valueOf(this.serverController.getBuildLimit)))
        //  textcomponenttranslation.getStyle.setColor(TextFormatting.RED)
        //  this.playerEntity.connection.sendPacket(new SPacketChat(textcomponenttranslation))
        //  }
        playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, posMouseOver))
        playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, posBlockSet))
        itemstack = this.playerEntity.getHeldItem
        if (itemstack != null && itemstack.stackSize == 0) {
            playerEntity.setHeldItem(null)

        }
    }

    override def processClickWindow(packetIn: CPacketClickWindow): Unit = {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getWorldServer)
        playerEntity.openContainer.mouseClicked(packetIn.mouseX, packetIn.mouseY, packetIn.button, playerEntity)
        playerEntity.updateHeldItem()
    }
}
