package ru.megains.common.world.storage

import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.utils.Logger
import ru.megains.nbt.NBTData
import ru.megains.nbt.tag.NBTCompound

import scala.reflect.io.{Directory, Path}

class AnvilSaveHandler(savesDirectory: Directory, worldName: String) extends ISaveHandler with Logger[AnvilSaveHandler] {
    def flush(): Unit = {}


    val worldDirectory: Directory = savesDirectory / Path(worldName).toDirectory
    val playersDirectory: Directory = worldDirectory / Path("playerData").toDirectory
    val chunkLoader: ChunkLoader = new ChunkLoader(worldDirectory)

    def getChunkLoader: ChunkLoader = {
        chunkLoader
    }


    def readPlayerData(player: EntityPlayer): NBTCompound = {
        var nbttagcompound: NBTCompound = null
        try {
            nbttagcompound = NBTData.readOfFile(playersDirectory, player.name).getCompound
        } catch {
            case var4: Exception => {
                log.warn("Failed to load player data for {}", Array[AnyRef](player.name))
            }
        }
        if (nbttagcompound != null) player.readFromNBT(nbttagcompound)

        nbttagcompound
    }


}
