package ru.megains.common.world

import ru.megains.common.position.ChunkPosition
import ru.megains.common.world.chunk.Chunk

import scala.collection.mutable

abstract class IChunkProvider {


    val chunkMap: mutable.HashMap[Long, Chunk]

    def unload(chunk: Chunk): Unit


    def loadChunk(pos: ChunkPosition): Chunk = {
        loadChunk(pos.chunkX, pos.chunkY, pos.chunkZ)
    }

    def loadChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk

    def provideChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk


}
