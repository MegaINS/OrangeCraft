package ru.megains.common.world.storage

import ru.megains.common.block.blockdata.MultiBlockPos
import ru.megains.common.multiblock.MultiBlock
import ru.megains.common.position.ChunkPosition
import ru.megains.common.register.Blocks
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
        val multiBlockStorage = blockStorage.multiBlockStorage

        blockStorage.blocksId = compound.getArrayShort("blocksId")
        blockStorage.blocksHp = compound.getArrayShort("blocksHp")
        blockStorage.blocksMeta = compound.getArrayShort("blocksMeta")
        blockStorage.blocksMeta.foreach((meta) => if (meta > 0) println(meta))
        val posList = compound.getList("multiBlockPosition")
        val multiBlockList = compound.getList("multiBlockData")

        for (i <- 0 until posList.getLength) {
            val posMultiBlock = posList.getInt(i)
            val multiBlock = new MultiBlock()
            val blockData = multiBlock.blockData
            val blockList = multiBlockList.getList(i)
            val indexList = blockList.getList(0)
            val blocksId = blockList.getList(1)
            val blocksHp = blockList.getList(2)
            val blocksMeta = blockList.getList(3)

            for (j <- 0 until indexList.getLength) {
                blockData += MultiBlockPos.getForIndex(indexList.getInt(j)) -> (Blocks.getBlockById(blocksId.getInt(j)), blocksHp.getInt(j), blocksMeta.getInt(j))
            }
            multiBlockStorage.put(posMultiBlock, multiBlock)
        }
        blockStorage
    }

    def writeChunk(compound: NBTCompound, blockStorage: ExtendedBlockStorage): Unit = {


        val multiBlockStorage = blockStorage.multiBlockStorage

        compound.setValue("blocksId", blockStorage.blocksId)
        compound.setValue("blocksHp", blockStorage.blocksHp)
        compound.setValue("blocksMeta", blockStorage.blocksMeta)
        val posList = compound.createList("multiBlockPosition", EnumNBTInt)
        val multiBlockList = compound.createList("multiBlockData", EnumNBTList)


        multiBlockStorage.foreach {
            case (index, multiBlock) =>
                posList.setValue(index)
                val blockList = multiBlockList.createList(EnumNBTList)
                val indexList = blockList.createList(EnumNBTInt)
                val blocksId = blockList.createList(EnumNBTInt)
                val blocksHp = blockList.createList(EnumNBTInt)
                val blocksMeta = blockList.createList(EnumNBTInt)
                multiBlock.blockData.foreach {
                    case (blockPos, (block, hp, meta)) =>
                        indexList.setValue(blockPos.getIndex)
                        blocksId.setValue(Blocks.getIdByBlock(block))
                        blocksHp.setValue(hp)
                        blocksMeta.setValue(meta)

                }
        }
    }

    def saveExtraChunkData(worldIn: World, chunkIn: Chunk) {
    }


}
