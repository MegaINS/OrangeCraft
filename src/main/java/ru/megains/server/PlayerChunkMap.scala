package ru.megains.server


import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.position.ChunkPosition
import ru.megains.common.world.chunk.Chunk
import ru.megains.server.entity.EntityPlayerMP
import ru.megains.server.world.WorldServer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class PlayerChunkMap(val worldServer: WorldServer) {


    val players: ArrayBuffer[EntityPlayerMP] = new ArrayBuffer[EntityPlayerMP]

    var pendingSendToPlayers: ArrayBuffer[PlayerChunkMapEntry] = new ArrayBuffer[PlayerChunkMapEntry]
    val playerInstanceList: ArrayBuffer[PlayerChunkMapEntry] = new ArrayBuffer[PlayerChunkMapEntry]
    var playersNeedingChunks: ArrayBuffer[PlayerChunkMapEntry] = new ArrayBuffer[PlayerChunkMapEntry]

    val playerInstances: mutable.HashMap[Long, PlayerChunkMapEntry] = new mutable.HashMap[Long, PlayerChunkMapEntry]()

    val playerInstancesToUpdate: mutable.HashSet[PlayerChunkMapEntry] = new mutable.HashSet[PlayerChunkMapEntry]()

    val playerViewRadius: Int = 6
    var previousTotalWorldTime: Long = 0

    val CAN_GENERATE_CHUNKS = (player: EntityPlayerMP) => player != null


    def tick() {
        val i: Long = System.currentTimeMillis()
        if (i - previousTotalWorldTime > 8000L) {
            previousTotalWorldTime = i

            playerInstanceList.foreach(
                (player) => {
                    player.update()
                    player.updateChunkInhabitedTime()
                }
            )
        }

        playerInstancesToUpdate.foreach(_.update())
        playerInstancesToUpdate.clear()




        if (playersNeedingChunks.nonEmpty) {
            val l: Long = System.nanoTime + 50000000L
            var k: Int = 40

            playersNeedingChunks = playersNeedingChunks.sortBy((chunk) => {
                chunk.getClosestPlayerDistance
            })



            playersNeedingChunks = playersNeedingChunks.flatMap(
                (player) => {
                    if (!(k < 0 || System.nanoTime > l) && (player.chunk eq null)) {
                        val flag: Boolean = player.hasPlayerMatching(CAN_GENERATE_CHUNKS)
                        if (player.providePlayerChunk(flag)) {
                            if (player.sentToPlayers()) pendingSendToPlayers -= player
                            k -= 1
                            None
                        } else {
                            Some(player)
                        }
                    } else {
                        Some(player)
                    }
                }

            )

        }

        if (pendingSendToPlayers.nonEmpty) {
            var i1: Int = 60

            pendingSendToPlayers = pendingSendToPlayers.sortBy((chunk) => {
                chunk.getClosestPlayerDistance
            })
            pendingSendToPlayers = pendingSendToPlayers.flatMap(
                (player) => {
                    if (!(i1 < 0)) {
                        if (player.sentToPlayers()) {
                            i1 -= 1
                            None
                        } else {
                            Some(player)
                        }
                    } else {
                        Some(player)
                    }
                }
            )
        }



        if (players.isEmpty) {
            //   val worldprovider: WorldProvider = worldServer.provider
            //   if (!worldprovider.canRespawnHere) worldServer.chunkProvider.unloadAllChunks()
        }
    }

    def addPlayer(player: EntityPlayerMP) {
        val x: Int = player.posX.toInt >> 4
        val y: Int = player.posY.toInt >> 4
        val z: Int = player.posZ.toInt >> 4
        player.managedPosX = player.posX
        player.managedPosY = player.posY
        player.managedPosZ = player.posZ


        for (x1 <- x - playerViewRadius to x + playerViewRadius) {
            for (y1 <- y - playerViewRadius to y + playerViewRadius) {
                for (z1 <- z - playerViewRadius to z + playerViewRadius) {
                    getOrCreateEntry(x1, y1, z1).addPlayer(player)
                }
            }
        }

        players += player
        //  markSortPending()
    }

    def removePlayer(player: EntityPlayerMP) {


        val x: Int = player.managedPosX.toInt >> 4
        val y: Int = player.managedPosY.toInt >> 4
        val z: Int = player.managedPosZ.toInt >> 4

        for (x1 <- x - playerViewRadius to x + playerViewRadius) {
            for (y1 <- y - playerViewRadius to y + playerViewRadius) {
                for (z1 <- z - playerViewRadius to z + playerViewRadius) {
                    getEntry(x, y, z).foreach(_.removePlayer(player))
                }
            }
        }


        players -= player
        // markSortPending()
    }

    def getOrCreateEntry(chunkX: Int, chunkY: Int, chunkZ: Int): PlayerChunkMapEntry = {

        getEntry(chunkX, chunkY, chunkZ).getOrElse(
            default = {
                val playerchunkmapentry: PlayerChunkMapEntry = new PlayerChunkMapEntry(this, chunkX, chunkY, chunkZ)
                playerInstances += Chunk.getIndex(chunkX, chunkY, chunkZ) -> playerchunkmapentry
                playerInstanceList += playerchunkmapentry
                if (playerchunkmapentry.chunk == null) playersNeedingChunks += playerchunkmapentry
                if (!playerchunkmapentry.sentToPlayers) pendingSendToPlayers += playerchunkmapentry
                playerchunkmapentry
            }
        )
    }

    def updateMountedMovingPlayer(player: EntityPlayerMP): Unit = {

        val x: Int = player.posX.toInt >> 4
        val y: Int = player.posY.toInt >> 4
        val z: Int = player.posZ.toInt >> 4
        val d0: Double = player.managedPosX - player.posX
        val d1: Double = player.managedPosY - player.posY
        val d2: Double = player.managedPosZ - player.posZ
        val d3: Double = d0 * d0 + d2 * d2 + d1 * d1

        if (d3 >= 64.0D) {
            val mX: Int = player.managedPosX.toInt >> 4
            val mY: Int = player.managedPosX.toInt >> 4
            val mZ: Int = player.managedPosZ.toInt >> 4
            val xmX: Int = x - mX
            val ymY: Int = y - mY
            val zmZ: Int = z - mZ

            if (xmX != 0 || ymY != 0 || zmZ != 0) {
                for (x1 <- x - playerViewRadius to x + playerViewRadius;
                     y1 <- y - playerViewRadius to y + playerViewRadius;
                     z1 <- z - playerViewRadius to z + playerViewRadius) {
                    if (!overlaps(x1, y1, z1, mX, mY, mZ, playerViewRadius)) getOrCreateEntry(x1, y1, z1).addPlayer(player)
                    if (!overlaps(x1 - xmX, y1 - ymY, z1 - zmZ, x, y, z, playerViewRadius)) {
                        val playerchunkmapentry: PlayerChunkMapEntry = getEntry(x1 - xmX, y1 - ymY, z1 - zmZ).orNull
                        if (playerchunkmapentry != null) playerchunkmapentry.removePlayer(player)
                    }


                }

                player.managedPosX = player.posX
                player.managedPosY = player.posY
                player.managedPosZ = player.posZ
                // markSortPending()
            }
        }
    }

    private def overlaps(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int, radius: Int): Boolean = {
        val x: Int = x1 - x2
        val y: Int = y1 - y2
        val z: Int = z1 - z2

        x >= -radius && x <= radius && y >= -radius && y <= radius && z >= -radius && z <= radius
    }

    def markBlockForUpdate(pos: BlockPos) {
        val x: Int = pos.worldX >> 4
        val y: Int = pos.worldY >> 4
        val z: Int = pos.worldZ >> 4
        val playerchunkmapentry: PlayerChunkMapEntry = getEntry(x, y, z).get
        if (playerchunkmapentry != null) playerchunkmapentry.blockChanged(pos)
    }

    def removeEntry(entry: PlayerChunkMapEntry) = {
        val chunkpos: ChunkPosition = entry.pos
        val i: Long = Chunk.getIndex(chunkpos.chunkX, chunkpos.chunkY, chunkpos.chunkZ)
        entry.updateChunkInhabitedTime()
        playerInstances -= i
        playerInstanceList -= entry
        playerInstancesToUpdate -= entry
        pendingSendToPlayers -= entry
        playersNeedingChunks -= entry
        val chunk: Chunk = entry.chunk

        if (chunk != null) worldServer.chunkProvider.unload(chunk)
    }

    def contains(chunkX: Int, chunkY: Int, chunkZ: Int): Boolean = {
        val i: Long = Chunk.getIndex(chunkX, chunkY, chunkZ)
        playerInstances.get(i) != null
    }

    def addEntry(entry: PlayerChunkMapEntry) {
        playerInstancesToUpdate.add(entry)
    }


    def getEntry(x: Int, y: Int, z: Int): Option[PlayerChunkMapEntry] = playerInstances.get(Chunk.getIndex(x, y, z))
}
