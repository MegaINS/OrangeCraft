package ru.megains.server


import ru.megains.common.item.ItemStack
import ru.megains.common.network.ServerStatusResponse
import ru.megains.common.register.{Bootstrap, Items}
import ru.megains.common.utils.{IThreadListener, Logger}
import ru.megains.common.world.storage.AnvilSaveFormat
import ru.megains.server.network.NetworkSystem
import ru.megains.server.world.{ServerWorldEventHandler, WorldServer}

import scala.collection.mutable
import scala.reflect.io.Directory

class OrangeCraftServer(serverDir: Directory) extends Runnable with Logger[OrangeCraftServer] with IThreadListener {


    var serverRunning = true
    val networkSystem = new NetworkSystem(this)
    private val statusResponse: ServerStatusResponse = new ServerStatusResponse

    var saveLoader: AnvilSaveFormat = _
    var world: WorldServer = _
    var serverThread: Thread = Thread.currentThread

    var playerList: PlayerList = _

    var timeOfLastWarning: Long = 0
    val futureTaskQueue: mutable.Queue[() => Unit] = new mutable.Queue[() => Unit]

    def startServer(): Boolean = {
        log.info("Starting OrangeCraft server  version 0.1.2")
        Bootstrap.init()


        networkSystem.startLan(null, 20000)
        // networkSystem.startLocal()


        saveLoader = new AnvilSaveFormat(serverDir)
        loadAllWorlds()
        new ItemStack(Items.stick, 10)

        true
    }


    override def run(): Unit = {

        if (startServer()) {

            var var1: Long = getCurrentTimeMillis
            var var50: Long = 0L


            while (serverRunning) {

                val var5: Long = getCurrentTimeMillis
                var var7: Long = var5 - var1


                if (var7 > 2000L && var1 - timeOfLastWarning >= 15000L) {
                    log.warn("Can\'t keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", var7, var7 / 50L)
                    var7 = 2000L
                    timeOfLastWarning = var1
                }

                if (var7 < 0L) {
                    log.warn("Time ran backwards! Did the system time change?")
                    var7 = 0L
                }



                var50 += var7
                var1 = var5


                while (var50 > 50L) {
                    var50 -= 50L
                    tick()

                }


                Thread.sleep(Math.max(1L, 50L - var50))

            }



            stopServer()


        }
    }

    def saveAllWorlds(dontLog: Boolean) {

        if (!dontLog) log.info("Saving chunks for level \'{}\'/{}")
        world.saveAllChunks(true)
        //        for (worldserver <- this.worldServers) {
        //            if (worldserver != null) {
        //                if (!dontLog) log.info("Saving chunks for level \'{}\'/{}", Array[AnyRef](worldserver.getWorldInfo.getWorldName, worldserver.provider.getDimensionType.getName))
        //                try
        //                    worldserver.saveAllChunks(true, null.asInstanceOf[IProgressUpdate])
        //
        //                catch {
        //                    case minecraftexception: MinecraftException => {
        //                        LOG.warn(minecraftexception.getMessage)
        //                    }
        //                }
        //            }
        //        }
    }

    def stopServer(): Unit = {
        log.info("Stopping server")
        if (this.playerList != null) {
            log.info("Saving players")
            // playerList.saveAllPlayerData()
            //  playerList.removeAllPlayers()
        }

        if (world != null) {
            log.info("Saving worlds")
            //            for (worldserver <- this.worldServers) {
            //                if (worldserver != null) worldserver.disableLevelSaving = false
            //            }
            saveAllWorlds(false)
            world.flush()
            //            for (worldserver1 <- this.worldServers) {
            //                if (worldserver1 != null) {
            //                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new WorldEvent.Unload(worldserver1))
            //                    worldserver1.flush()
            //                }
            //            }
            //            val tmp: Array[WorldServer] = worldServers
            //            for (world <- tmp) {
            //                net.minecraftforge.common.DimensionManager.setWorld(world.provider.getDimension, null, this)
            //            }
        }
    }

    def tick(): Unit = {
        futureTaskQueue synchronized {
            while (futureTaskQueue.nonEmpty) futureTaskQueue.dequeue()()
        }



        networkSystem.networkTick()
        world.update()

    }

    def loadAllWorlds() = {
        val saveHandler = saveLoader.getSaveLoader("world")
        world = new WorldServer(this, saveHandler)
        world.addEventListener(new ServerWorldEventHandler(this, world))
        playerList = new PlayerList(this)

        initialWorldChunkLoad()
    }

    def initialWorldChunkLoad() = {

    }

    def getCurrentTimeMillis: Long = System.currentTimeMillis

    def getServerStatusResponse: ServerStatusResponse = statusResponse

    def callFromMainThread[V](callable: () => Unit): Unit = {

        if (!isCallingFromMinecraftThread && serverRunning) {

            futureTaskQueue synchronized {
                futureTaskQueue += callable

            }
        }
        else try
            callable()

        catch {
            case exception: Exception => {
                println("addScheduledTask")
                exception.printStackTrace()
            }
        }
    }


    override def addScheduledTask(runnableToSchedule: () => Unit): Unit = callFromMainThread[AnyRef](runnableToSchedule)

    override def isCallingFromMinecraftThread: Boolean = Thread.currentThread eq serverThread
}

object OrangeCraftServer {
    def getCurrentTimeMillis: Long = System.currentTimeMillis
}
