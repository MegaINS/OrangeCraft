package ru.megains.game.world.chunk

import ru.megains.game.world.World
import ru.megains.game.world.storage.ChunkLoader

class Loader(world: World, chunkLoader: ChunkLoader, x: Int, y: Int, z: Int) extends Runnable {


    override def run(): Unit = {
        val chunk: Chunk = chunkLoader.load(world, x, y, z)
        world.addChunk(Chunk.getIndex(chunk), chunk)
    }
}
