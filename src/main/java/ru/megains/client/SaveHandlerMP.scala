package ru.megains.client

import ru.megains.game.world.storage.{ChunkLoader, ISaveHandler}

class SaveHandlerMP extends ISaveHandler {

    override def getChunkLoader: ChunkLoader = null
}
