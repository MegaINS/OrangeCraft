package ru.megains.server


import ru.megains.common.network.play.server.{SPacketBlockChange, SPacketChunkData, SPacketMultiBlockChange, SPacketUnloadChunk}
import ru.megains.common.network.{INetHandler, Packet}
import ru.megains.game.blockdata.BlockPos
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
    val changedBlocks: ArrayBuffer[BlockPos] = ArrayBuffer[BlockPos]()



    def hasPlayerMatching(function: (EntityPlayerMP) => Boolean): Boolean = {
        players.forall(function)
    }

    def updateChunkInhabitedTime() = {

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

            isSentToPlayers = true
            val packet = new SPacketChunkData(chunk)
            players.foreach(
                (player) => {
                    player.connection.sendPacket(packet)
                    playerChunkMap.worldServer.entityTracker.sendLeashedEntitiesInChunk(player, chunk)
                }
            )

            true
        }
    }

    def blockChanged(pos: BlockPos) {
        if (isSentToPlayers) {
            if (changes == 0) playerChunkMap.addEntry(this)
            for (i <- changedBlocks.indices) {
                if (changedBlocks(i) eq pos) return
            }
            changes += 1
            changedBlocks += pos

        }
    }

    def update() {
        if (isSentToPlayers && chunk != null) if (changes != 0) {
            if (changes == 1) {
                val blockPos: BlockPos = changedBlocks(0)
                sendPacket(new SPacketBlockChange(playerChunkMap.worldServer, blockPos))
                //  val state: IBlockState = playerChunkMap.getWorldServer.getBlockState(blockpos)
                //  if (state.getBlock.hasTileEntity(state)) sendBlockEntity(playerChunkMap.getWorldServer.getTileEntity(blockpos))
            }
            else if (changes >= 64) {
                sendPacket(new SPacketChunkData(chunk))

            }
            else {
                sendPacket(new SPacketMultiBlockChange(changedBlocks, chunk))

                //                var l: Int = 0
                //                while (l < changes) {
                //                    {
                //                        val i1: Int = (changedBlocks(l) >> 12 & 15) + pos.chunkXPos * 16
                //                        val j1: Int = changedBlocks(l) & 255
                //                        val k1: Int = (changedBlocks(l) >> 8 & 15) + pos.chunkZPos * 16
                //                        val blockpos1: BlockPos = new BlockPos(i1, j1, k1)
                //                  //      val state: IBlockState = playerChunkMap.getWorldServer.getBlockState(blockpos1)
                //                       // if (state.getBlock.hasTileEntity(state)) sendBlockEntity(playerChunkMap.getWorldServer.getTileEntity(blockpos1))
                //                    }
                //                    {
                //                        l += 1; l
                //                    }
                //                }
            }
            changes = 0
            changedBlocks.clear()

        }
    }

    def sendPacket(packetIn: Packet[_ <: INetHandler]) {
        if (isSentToPlayers) {

            for (i <- players.indices) {
                players(i).connection.sendPacket(packetIn)
            }

        }
    }

    def sendNearbySpecialEntities(player: EntityPlayerMP) {
        if (isSentToPlayers) {
            player.connection.sendPacket(new SPacketChunkData(chunk))
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
