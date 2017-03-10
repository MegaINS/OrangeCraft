package ru.megains.common.world.storage

import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.item.ItemStack
import ru.megains.common.register.{Blocks, Items}
import ru.megains.common.utils.Logger
import ru.megains.nbt.NBTData
import ru.megains.nbt.tag.NBTCompound
import ru.megains.server.entity.EntityPlayerMP

import scala.reflect.io.{Directory, Path}
import scala.util.Random

class AnvilSaveHandler(savesDirectory: Directory, worldName: String) extends ISaveHandler with Logger[AnvilSaveHandler] {


    def flush(): Unit = {}


    val worldDirectory: Directory = savesDirectory / Path(worldName).toDirectory
    val playersDirectory: Directory = worldDirectory / Path("playerData").toDirectory
    val chunkLoader: ChunkLoader = new ChunkLoader(worldDirectory)

    def getChunkLoader: ChunkLoader = {
        chunkLoader
    }


    def readPlayerData(player: EntityPlayer): NBTCompound = {
        var compound: NBTCompound = null
        try {
            compound = NBTData.readOfFile(playersDirectory, player.name).getCompound
        } catch {
            case _: Exception =>
                log.warn("Failed to load player data for {}", Array[AnyRef](player.name))
        }
        if (compound != null) {
            player.readFromNBT(compound)
        }
        else {
            val inventory = player.inventory
            inventory.addItemStackToInventory(new ItemStack(Items.stick, 10))

            val rand: Int = Random.nextInt(15) + 1
            for (id <- 1 to rand) {
                inventory.addItemStackToInventory(new ItemStack(Blocks.getBlockById(2 + Random.nextInt(10)), id))
            }
        }

        compound
    }

    def writePlayerData(playerIn: EntityPlayerMP): Unit = {
        val compound: NBTCompound = NBTData.createCompound()
        playerIn.writeToNBT(compound)
        NBTData.writeToFile(compound, playersDirectory, playerIn.name)

    }


}
