package ru.megains.client.renderer.world

import ru.megains.common.world.storage.{ChunkLoader, ISaveHandler}

class SaveHandlerMP extends ISaveHandler {

    override def getChunkLoader: ChunkLoader = null
}
