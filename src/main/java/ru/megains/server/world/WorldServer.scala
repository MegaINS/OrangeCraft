package ru.megains.server.world

import com.google.common.util.concurrent.ListenableFuture
import ru.megains.common.entity.EntityTracker
import ru.megains.common.utils.IThreadListener
import ru.megains.common.world.gen.ChunkProviderOverworld
import ru.megains.common.world.storage.AnvilSaveHandler
import ru.megains.common.world.{ChunkProviderServer, IChunkProvider, World}
import ru.megains.server.{OrangeCraftServer, PlayerChunkMap}

class WorldServer(server: OrangeCraftServer, val saveHandler: AnvilSaveHandler) extends World(saveHandler) with IThreadListener {


    override var isRemote: Boolean = false
    val playerManager: PlayerChunkMap = new PlayerChunkMap(this)
    val entityTracker: EntityTracker = new EntityTracker(this)
    override val chunkProvider: IChunkProvider = new ChunkProviderServer(this, chunkLoader, new ChunkProviderOverworld(this, 342342, false, ""))
    val chunkProviderServer: ChunkProviderServer = chunkProvider.asInstanceOf[ChunkProviderServer]
    var disableLevelSaving: Boolean = false

    override def update(): Unit = {
        super.update()

        playerManager.tick()
    }


    def saveAllChunks(p_73044_1: Boolean /*, progressCallback: IProgressUpdate*/) {

        if (chunkProviderServer.canSave) {
            //   if (progressCallback != null) progressCallback.displaySavingString("Saving level")
            saveLevel()
            // if (progressCallback != null) progressCallback.displayLoadingString("Saving chunks")
            chunkProviderServer.saveChunks(p_73044_1)

            chunkProviderServer.getLoadedChunks.foreach(
                (chunk) => {
                    val pos = chunk.position
                    if (chunk != null && !playerManager.contains(pos.chunkX, pos.chunkY, pos.chunkZ)) chunkProviderServer.unload(chunk)
                }
            )
        }
    }


    protected def saveLevel() {
        //        this.checkSessionLock()
        //        for (worldserver <- this.mcServer.worldServers) {
        //            if (worldserver.isInstanceOf[WorldServerMulti]) worldserver.asInstanceOf[WorldServerMulti].saveAdditionalData()
        //        }
        //        this.worldInfo.setBorderSize(this.getWorldBorder.getDiameter)
        //        this.worldInfo.getBorderCenterX(this.getWorldBorder.getCenterX)
        //        this.worldInfo.getBorderCenterZ(this.getWorldBorder.getCenterZ)
        //        this.worldInfo.setBorderSafeZone(this.getWorldBorder.getDamageBuffer)
        //        this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder.getDamageAmount)
        //        this.worldInfo.setBorderWarningDistance(this.getWorldBorder.getWarningDistance)
        //        this.worldInfo.setBorderWarningTime(this.getWorldBorder.getWarningTime)
        //        this.worldInfo.setBorderLerpTarget(this.getWorldBorder.getTargetSize)
        //        this.worldInfo.setBorderLerpTime(this.getWorldBorder.getTimeUntilTarget)
        //        this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getPlayerList.getHostPlayerData)
        //        this.mapStorage.saveAllData()
        //        this.perWorldStorage.saveAllData()
    }

    override def addScheduledTask(runnableToSchedule: Runnable): ListenableFuture[AnyRef] = server.addScheduledTask(runnableToSchedule)

    override def isCallingFromMinecraftThread: Boolean = server.isCallingFromMinecraftThread

    def flush() {
        saveHandler.flush()
    }


}
