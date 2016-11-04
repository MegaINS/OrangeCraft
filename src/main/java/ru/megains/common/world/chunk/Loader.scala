package ru.megains.common.world.chunk

import ru.megains.common.world.World
import ru.megains.common.world.storage.ChunkLoader

class Loader(world: World, chunkLoader: ChunkLoader, x: Int, y: Int, z: Int) extends Runnable {


    override def run(): Unit = {
        val chunk: Chunk = chunkLoader.load(world, x, y, z)
        world.addChunk(Chunk.getIndex(chunk), chunk)
    }
}
