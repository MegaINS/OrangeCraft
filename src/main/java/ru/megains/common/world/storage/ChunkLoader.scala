package ru.megains.common.world.storage

import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.MultiBlockPos
import ru.megains.common.multiblock.MultiBlock
import ru.megains.common.position.ChunkPosition
import ru.megains.common.world.World
import ru.megains.common.world.chunk.{Chunk, ChunkVoid}
import ru.megains.nbt.NBTData
import ru.megains.nbt.NBTType._
import ru.megains.nbt.tag.NBTCompound
import ru.megains.server.world.region.RegionLoader

import scala.reflect.io.Directory

class ChunkLoader(worldDirectory: Directory) {

    val regionLoader = new RegionLoader(worldDirectory)

    def loadChunk(world: World, x: Int, y: Int, z: Int): Chunk = {
        var chunk: Chunk = null
        val input = regionLoader.getChunkInputStream(x, y, z)
        if (input != null) {
            val nbt = NBTData.readOfStream(input)
            if (nbt.isCompound) {
                try {
                    chunk = new Chunk(world, new ChunkPosition(x, y, z), readChunk(nbt.getCompound))
                }
                catch {
                    case var3: Exception =>
                        println("Error load chunk")
                        var3.printStackTrace()
                }
            }
        }
        chunk
    }

    def saveChunk(chunk: Chunk): Unit = {
        if (!chunk.isInstanceOf[ChunkVoid] && chunk.isSaved) {
            val chunkNBT = NBTData.createCompound()

            writeChunk(chunkNBT, chunk.blockStorage)
            val pos = chunk.position
            val output = regionLoader.getChunkOutputStream(pos.chunkX, pos.chunkY, pos.chunkZ)
            NBTData.writeToStream(chunkNBT, output)
            output.close()
        }
    }

    def readChunk(compound: NBTCompound): ExtendedBlockStorage = {

        val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
        val data = blockStorage.data
        val multiBlockStorage = blockStorage.multiBlockStorage
        val blockArray = compound.getArrayShort("blockArray")

        for (i <- 0 until 4096) {
            data.set(i, blockArray(i))
        }
        val posList = compound.getList("multiBlockPosition")
        val multiBlockList = compound.getList("multiBlockData")

        for (i <- 0 until posList.getLength) {
            val posMultiBlock = posList.getInt(i)
            val multiBlock = new MultiBlock()
            val blockList = multiBlockList.getList(i)
            val indexList = blockList.getList(0)
            val blockIdList = blockList.getList(1)

            for (j <- 0 until indexList.getLength) {
                multiBlock.putBlock(MultiBlockPos.getForIndex(indexList.getInt(j)), Block.getBlockById(blockIdList.getInt(j)))
            }
            multiBlockStorage.put(posMultiBlock, multiBlock)
        }
        blockStorage
    }

    def writeChunk(compound: NBTCompound, blockStorage: ExtendedBlockStorage): Unit = {

        val blockData = blockStorage.data
        val multiBlockStorage = blockStorage.multiBlockStorage
        val blockArray = new Array[Short](4096)

        for (i <- 0 until 4096) {
            blockArray(i) = blockData.get(i).toShort
        }

        compound.setValue("blockArray", blockArray)
        val posList = compound.createList("multiBlockPosition", EnumNBTInt)
        val multiBlockList = compound.createList("multiBlockData", EnumNBTList)

        multiBlockStorage.foreach(
            (data: (Int, MultiBlock)) => {
                posList.setValue(data._1)
                val blockList = multiBlockList.createList(EnumNBTList)
                val indexList = blockList.createList(EnumNBTInt)
                val blockIdList = blockList.createList(EnumNBTInt)

                data._2.blockData.foreach(
                    (data2: (MultiBlockPos, Block)) => {
                        indexList.setValue(data2._1.getIndex)
                        blockIdList.setValue(Block.getIdByBlock(data2._2))
                    }
                )
            }
        )
    }

    def saveExtraChunkData(worldIn: World, chunkIn: Chunk) {
    }


}
