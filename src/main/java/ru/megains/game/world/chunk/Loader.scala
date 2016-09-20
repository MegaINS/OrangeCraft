package ru.megains.game.world.chunk

import ru.megains.game.world.World

class Loader(world: World, x: Int, y: Int, z: Int) extends Runnable {


    override def run(): Unit = {
        val chunk: Chunk = ChunkLoader.load(world, x, y, z)
        world.addChunk(Chunk.getIndex(chunk), chunk)
    }
}
