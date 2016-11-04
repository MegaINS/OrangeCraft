package ru.megains.common.world


import ru.megains.common.position.ChunkPosition
import ru.megains.common.world.chunk.{Chunk, ChunkVoid}

import scala.collection.mutable

class ChunkProviderClient(world: World) extends IChunkProvider {

    val blankChunk = new ChunkVoid(world, new ChunkPosition(0, 0, 0))
    val chunkMapping: mutable.HashMap[Long, Chunk] = new mutable.HashMap[Long, Chunk]

    override def loadChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk = {
        val key = Chunk.getIndex(chunkX, chunkY, chunkZ)
        if (chunkMapping.contains(key)) chunkMapping(key)
        else {
            val chunk: Chunk = new Chunk(world, new ChunkPosition(chunkX, chunkY, chunkZ))
            chunkMapping += key -> chunk
            chunk
        }
    }

    override def provideChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk = {
        chunkMapping.getOrElse(Chunk.getIndex(chunkX, chunkY, chunkZ), blankChunk)
    }

    def getLoadedChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk = chunkMapping(Chunk.getIndex(chunkX, chunkY, chunkZ))

    override def unload(chunk: Chunk): Unit = {

    }
}
