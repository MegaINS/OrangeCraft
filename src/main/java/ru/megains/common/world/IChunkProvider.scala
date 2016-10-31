package ru.megains.common.world

import ru.megains.game.position.ChunkPosition
import ru.megains.game.world.chunk.Chunk

abstract class IChunkProvider {
    def unload(chunk: Chunk): Unit


    def loadChunk(pos: ChunkPosition): Chunk = {
        loadChunk(pos.chunkX, pos.chunkY, pos.chunkZ)
    }

    def loadChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk

    def provideChunk(chunkX: Int, chunkY: Int, chunkZ: Int): Chunk


}
