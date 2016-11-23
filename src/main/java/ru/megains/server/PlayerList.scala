package ru.megains.server

import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.network.play.server.{SPacketHeldItemChange, SPacketJoinGame, SPacketSpawnPosition}
import ru.megains.common.network.{NetHandlerPlayServer, NetworkManager}
import ru.megains.common.world.World
import ru.megains.common.world.storage.AnvilSaveHandler
import ru.megains.nbt.tag.NBTCompound
import ru.megains.server.entity.EntityPlayerMP
import ru.megains.server.world.WorldServer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class PlayerList(server: OrangeCraftServer) {
    var playerNBTManager: AnvilSaveHandler = server.world.saveHandler
    val playerEntityList: ArrayBuffer[EntityPlayerMP] = new ArrayBuffer[EntityPlayerMP]()


    val idToPlayerMap: mutable.HashMap[Int, EntityPlayerMP] = new mutable.HashMap[Int, EntityPlayerMP]()
    val nameToPlayerMap: mutable.HashMap[String, EntityPlayerMP] = new mutable.HashMap[String, EntityPlayerMP]()


    def createPlayerForUser(name: String): EntityPlayerMP = {
        //   val uuid: UUID = EntityPlayer.getUUID(profile)
        //   val list: util.List[EntityPlayerMP] = Lists.newArrayList[EntityPlayerMP]
        //        var i: Int = 0
        //        while (i < this.playerEntityList.size) {
        //            {
        //                val entityplayermp: EntityPlayerMP = this.playerEntityList.get(i).asInstanceOf[EntityPlayerMP]
        //                if (entityplayermp.getUniqueID == uuid) list.add(entityplayermp)
        //            }
        //            {
        //                i += 1; i
        //            }
        //        }
        //        val entityplayermp2: EntityPlayerMP = this.uuidToPlayerMap.get(profile.getId).asInstanceOf[EntityPlayerMP]
        //        if (entityplayermp2 != null && !list.contains(entityplayermp2)) list.add(entityplayermp2)
        //        import scala.collection.JavaConversions._
        //        for (entityplayermp1 <- list) {
        //            entityplayermp1.connection.kickPlayerFromServer("You logged in from another location")
        //        }
        //        var playerinteractionmanager: PlayerInteractionManager = null
        //        if (this.mcServer.isDemo) playerinteractionmanager = new DemoWorldManager(this.mcServer.worldServerForDimension(0))
        //        else playerinteractionmanager = new PlayerInteractionManager(this.mcServer.worldServerForDimension(0))
        val playerinteractionmanager: PlayerInteractionManager = new PlayerInteractionManager(server.world)
        new EntityPlayerMP(name, server.world, playerinteractionmanager)
    }


    def initializeConnectionToPlayer(netManager: NetworkManager, playerIn: EntityPlayerMP) {


        val nethandlerplayserver: NetHandlerPlayServer = new NetHandlerPlayServer(server, netManager, playerIn)



        //  val gameprofile: GameProfile = playerIn.getGameProfile
        //  val playerprofilecache: PlayerProfileCache = this.mcServer.getPlayerProfileCache
        //   val gameprofile1: GameProfile = playerprofilecache.getProfileByUUID(gameprofile.getId)
        //  val s: String = if (gameprofile1 == null) gameprofile.getName
        //  else gameprofile1.getName
        //   playerprofilecache.addEntry(gameprofile)
        val nbttagcompound: NBTCompound = readPlayerDataFromFile(playerIn)
        //  playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension))
        //        var playerWorld: World = this.mcServer.worldServerForDimension(playerIn.dimension)
        //        if (playerWorld == null) {
        //            playerIn.dimension = 0
        //            playerWorld = this.mcServer.worldServerForDimension(0)
        //            val spawnPoint: BlockPos = playerWorld.provider.getRandomizedSpawnPoint
        //            playerIn.setPosition(spawnPoint.getX, spawnPoint.getY, spawnPoint.getZ)
        //        }
        //  playerIn.setWorld(playerWorld)


        //  playerIn.interactionManager.setWorld(playerIn.worldObj.asInstanceOf[WorldServer])


        //  var s1: String = "local"
        //  if (netManager.getRemoteAddress != null) s1 = netManager.getRemoteAddress.toString
        //  LOG.info("{}[{}] logged in with entity id {} at ({}, {}, {})", Array[AnyRef](playerIn.getName, s1, Integer.valueOf(playerIn.getEntityId), Double.valueOf(playerIn.posX), Double.valueOf(playerIn.posY), Double.valueOf(playerIn.posZ)))
        val worldserver: World = server.world
        //  val worldinfo: WorldInfo = worldserver.getWorldInfo
        val blockpos: BlockPos = worldserver.spawnPoint
        //  setPlayerGameTypeBasedOnOther(playerIn, null.asInstanceOf[EntityPlayerMP], worldserver)


        playerIn.connection = nethandlerplayserver
        nethandlerplayserver.sendPacket(new SPacketJoinGame())
        //  nethandlerplayserver.sendPacket(new SPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer).writeString(this.getServerInstance.getServerModName)))
        // nethandlerplayserver.sendPacket(new SPacketServerDifficulty(worldinfo.getDifficulty, worldinfo.isDifficultyLocked))
        nethandlerplayserver.sendPacket(new SPacketSpawnPosition(blockpos))
        //  nethandlerplayserver.sendPacket(new SPacketPlayerAbilities(playerIn.capabilities))
        nethandlerplayserver.sendPacket(new SPacketHeldItemChange(playerIn.inventory.stackSelect))


        // updatePermissionLevel(playerIn)
        //  playerIn.getStatFile.markAllDirty()
        //   playerIn.getStatFile.sendAchievements(playerIn)
        // sendScoreboard(worldserver.getScoreboard.asInstanceOf[ServerScoreboard], playerIn)
        //  mcServer.refreshStatusNextTick()
        //  var textcomponenttranslation: TextComponentTranslation = null
        // if (playerIn.getName.equalsIgnoreCase(s)) textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined", Array[AnyRef](playerIn.getDisplayName))
        // else textcomponenttranslation = new TextComponentTranslation("multiplayer.player.joined.renamed", Array[AnyRef](playerIn.getDisplayName, s))
        //  textcomponenttranslation.getStyle.setColor(TextFormatting.YELLOW)
        //this.sendChatMsg(textcomponenttranslation)
        playerLoggedIn(playerIn)
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch)
        //  updateTimeAndWeatherForPlayer(playerIn, worldserver)
        // if (!this.mcServer.getResourcePackUrl.isEmpty) playerIn.loadResourcePack(this.mcServer.getResourcePackUrl, this.mcServer.getResourcePackHash)
        //        for (potioneffect <- playerIn.getActivePotionEffects) {
        //            nethandlerplayserver.sendPacket(new SPacketEntityEffect(playerIn.getEntityId, potioneffect))
        //        }
        //        if (nbttagcompound != null) if (nbttagcompound.hasKey("RootVehicle", 10)) {
        //            val nbttagcompound1: NBTTagCompound = nbttagcompound.getCompoundTag("RootVehicle")
        //            val entity2: Entity = AnvilChunkLoader.readWorldEntity(nbttagcompound1.getCompoundTag("Entity"), worldserver, true)
        //            if (entity2 != null) {
        //                val uuid: UUID = nbttagcompound1.getUniqueId("Attach")
        //                if (entity2.getUniqueID == uuid) playerIn.startRiding(entity2, true)
        //                else {
        //                    import scala.collection.JavaConversions._
        //                    for (entity <- entity2.getRecursivePassengers) {
        //                        if (entity.getUniqueID == uuid) {
        //                            playerIn.startRiding(entity, true)
        //                            break //todo: break is not supported
        //                        }
        //                    }
        //                }
        //                if (!playerIn.isRiding) {
        //                    LOG.warn("Couldn\'t reattach entity to player")
        //                    worldserver.removeEntityDangerously(entity2)
        //                    import scala.collection.JavaConversions._
        //                    for (entity3 <- entity2.getRecursivePassengers) {
        //                        worldserver.removeEntityDangerously(entity3)
        //                    }
        //                }
        //            }
        //        }
        //        else if (nbttagcompound.hasKey("Riding", 10)) {
        //            val entity1: Entity = AnvilChunkLoader.readWorldEntity(nbttagcompound.getCompoundTag("Riding"), worldserver, true)
        //            if (entity1 != null) playerIn.startRiding(entity1, true)
        //        }
        // playerIn.addSelfToInternalCraftingInventory()
    }

    def readPlayerDataFromFile(playerIn: EntityPlayerMP): NBTCompound = {
        //  val nbttagcompound: NBTCompound = mcServer.worldServers(0).getWorldInfo.getPlayerNBTTagCompound
        var nbttagcompound1: NBTCompound = null
        //        if (playerIn.getName == this.mcServer.getServerOwner && nbttagcompound != null) {
        //            nbttagcompound1 = this.mcServer.getDataFixer.process(FixTypes.PLAYER, nbttagcompound)
        //            playerIn.readFromNBT(nbttagcompound1)
        //            log.debug("loading single player")
        //            net.minecraftforge.event.ForgeEventFactory.firePlayerLoadingEvent(playerIn, this.playerNBTManagerObj, playerIn.getUniqueID.toString)
        //        }
        //        else
        nbttagcompound1 = playerNBTManager.readPlayerData(playerIn)
        nbttagcompound1
    }

    def playerLoggedIn(playerIn: EntityPlayerMP) {
        playerEntityList += playerIn
        nameToPlayerMap += playerIn.name -> playerIn
        //  sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, Array[EntityPlayerMP](playerIn)))
        val worldserver: WorldServer = server.world
        var i: Int = 0


        for (i <- playerEntityList.indices) {
            // playerIn.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, Array[EntityPlayerMP](this.playerEntityList.get(i).asInstanceOf[EntityPlayerMP])))
        }
        //        while (i < this.playerEntityList.size) {
        //            {
        //                playerIn.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, Array[EntityPlayerMP](this.playerEntityList.get(i).asInstanceOf[EntityPlayerMP])))
        //            }
        //            {
        //                i += 1; i
        //            }
        // }
        worldserver.spawnEntityInWorld(playerIn)
        preparePlayer(playerIn, null)
    }

    def preparePlayer(playerIn: EntityPlayerMP, worldIn: WorldServer) {
        val worldserver: WorldServer = playerIn.getWorldServer
        if (worldIn != null) worldIn.playerManager.removePlayer(playerIn)
        worldserver.playerManager.addPlayer(playerIn)
        worldserver.chunkProvider.provideChunk(playerIn.posX.toInt >> 4, playerIn.posY.toInt >> 4, playerIn.posZ.toInt >> 4)
    }

    def playerLoggedOut(playerIn: EntityPlayerMP) {

        val worldserver: WorldServer = playerIn.getWorldServer
        //   playerIn.addStat(StatList.LEAVE_GAME)
        //  this.writePlayerData(playerIn)
        //        if (playerIn.isRiding) {
        //            val entity: Entity = playerIn.getLowestRidingEntity
        //            if (entity.getRecursivePassengersByType(classOf[EntityPlayerMP]).size == 1) {
        //                LOG.debug("Removing player mount")
        //                playerIn.dismountRidingEntity()
        //                worldserver.removeEntityDangerously(entity)
        //                import scala.collection.JavaConversions._
        //                for (entity1 <- entity.getRecursivePassengers) {
        //                    worldserver.removeEntityDangerously(entity1)
        //                }
        //                worldserver.getChunkFromChunkCoords(playerIn.chunkCoordX, playerIn.chunkCoordZ).setChunkModified()
        //            }
        //        }
        // worldserver.removeEntity(playerIn)
        worldserver.playerManager.removePlayer(playerIn)
        playerEntityList -= playerIn

        val player = nameToPlayerMap(playerIn.name)
        if (playerIn eq player) {
            nameToPlayerMap -= playerIn.name
        }
        // val uuid: UUID = playerIn.getUniqueID
        //  val entityplayermp: EntityPlayerMP = this.uuidToPlayerMap.get(uuid).asInstanceOf[EntityPlayerMP]
        //        if (entityplayermp eq playerIn) {
        //            this.uuidToPlayerMap.remove(uuid)
        //            this.playerStatFiles.remove(uuid)
        //        }

        //  this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, Array[EntityPlayerMP](playerIn)))
    }

    def serverUpdateMountedMovingPlayer(player: EntityPlayerMP) {
        player.getWorldServer.playerManager.updateMountedMovingPlayer(player)
    }


    def getPlayerByID(playerID: Int): EntityPlayer = idToPlayerMap.getOrElse(playerID, null)

    def getPlayerByName(player: String): EntityPlayerMP = nameToPlayerMap.getOrElse(player, null)


}
