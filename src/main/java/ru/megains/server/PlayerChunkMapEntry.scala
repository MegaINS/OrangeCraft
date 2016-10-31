package ru.megains.server


import ru.megains.common.network.play.server.{SPacketChunkData, SPacketUnloadChunk}
import ru.megains.game.position.ChunkPosition
import ru.megains.game.world.chunk.Chunk
import ru.megains.server.entity.EntityPlayerMP
import ru.megains.utils.Logger

import scala.collection.mutable.ArrayBuffer

class PlayerChunkMapEntry(playerChunkMap: PlayerChunkMap, chunkX: Int, chunkY: Int, chunkZ: Int) extends Logger[PlayerChunkMapEntry] {


    var pos: ChunkPosition = new ChunkPosition(chunkX, chunkY, chunkZ)
    val players: ArrayBuffer[EntityPlayerMP] = new ArrayBuffer[EntityPlayerMP]
    var isSentToPlayers: Boolean = false
    var loading: Boolean = false
    var chunk: Chunk = _
    var changes: Int = 0

    var changedSectionFilter: Int = 0


    def hasPlayerMatching(function: (EntityPlayerMP) => Boolean): Boolean = {
        players.forall(function)
    }

    def updateChunkInhabitedTime() = {

    }

    def update() = {

    }


    var lastUpdateInhabitedTime: Long = 0

    def addPlayer(player: EntityPlayerMP) {
        if (players.contains(player)) log.debug("Failed to add player. {} already is in chunk {}, {}", Array[Any](player, pos.chunkX, pos.chunkY, pos.chunkZ))
        else {
            if (players.isEmpty) lastUpdateInhabitedTime = System.currentTimeMillis()
            players += player
            if (isSentToPlayers) {
                sendNearbySpecialEntities(player)
            }
        }
    }


    def removePlayer(player: EntityPlayerMP) {
        if (players.contains(player)) {
            if (chunk == null) {
                players -= player
                if (players.isEmpty) {
                    playerChunkMap.removeEntry(this)
                }
                return
            }
            if (isSentToPlayers) player.connection.sendPacket(new SPacketUnloadChunk(pos.chunkX, pos.chunkY, pos.chunkZ))
            players -= player

            if (players.isEmpty) playerChunkMap.removeEntry(this)
        }
    }


    def sentToPlayers(): Boolean = {
        if (isSentToPlayers) true
        else if (chunk == null) false
        else if (!chunk.isPopulated) false
        else {
            changes = 0
            changedSectionFilter = 0
            isSentToPlayers = true
            val packet = new SPacketChunkData(chunk, 65535)
            players.foreach(
                (player) => {
                    player.connection.sendPacket(packet)
                    playerChunkMap.worldServer.entityTracker.sendLeashedEntitiesInChunk(player, chunk)
                }
            )

            true
        }
    }

    def sendNearbySpecialEntities(player: EntityPlayerMP) {
        if (isSentToPlayers) {
            player.connection.sendPacket(new SPacketChunkData(chunk, 65535))
            playerChunkMap.worldServer.entityTracker.sendLeashedEntitiesInChunk(player, chunk)
        }
    }


    def providePlayerChunk(canGenerate: Boolean): Boolean = {
        if (loading) return false
        if (chunk != null) true
        else {
            if (canGenerate) chunk = playerChunkMap.worldServer.chunkProvider.provideChunk(pos.chunkX, pos.chunkY, pos.chunkZ)
            else chunk = playerChunkMap.worldServer.chunkProvider.loadChunk(pos.chunkX, pos.chunkY, pos.chunkZ)
            chunk != null
        }
    }


}
