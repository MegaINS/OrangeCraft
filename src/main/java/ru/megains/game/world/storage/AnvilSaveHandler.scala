package ru.megains.game.world.storage

import scala.reflect.io.{Directory, Path}

class AnvilSaveHandler(savesDirectory: Directory, worldName: String) extends ISaveHandler {
    def flush(): Unit = {}


    val worldDirectory: Directory = savesDirectory / Path(worldName).toDirectory
    val chunkLoader: ChunkLoader = new ChunkLoader(worldDirectory / Path("chunks").toDirectory)

    def getChunkLoader: ChunkLoader = {
        chunkLoader
    }


}