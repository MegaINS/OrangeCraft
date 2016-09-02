package ru.megains.game.world.chunk


import ru.megains.game.block.Block
import ru.megains.game.blockdata.BlockWorldPos
import ru.megains.game.multiblock.{AMultiBlock, MultiBlock}
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.Blocks
import ru.megains.game.world.World
import ru.megains.game.world.chunk.storage.ExtendedBlockStorage

import scala.util.Random


class Chunk(val world: World, val position: ChunkPosition) {


    def this(world: World, position: ChunkPosition, data: Array[Byte]) {
        this(world, position)
        for (x <- 0 to 15; y <- 0 to 15; z <- 0 to 15) {
            blockStorage.setBlockId(x, y, z, data(blockStorage.getIndex(x, y, z)))
            // setBlockChunkCord(new BlockWorldPos(x,y,z) ,Block.getBlockById(data(blockStorage.getIndex(x,y,z))))
        }
    }


    val blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage
    var updateLCG: Int = new Random().nextInt()


    def isAirBlockWorldCord(pos: BlockWorldPos): Boolean = {
        getBlockWorldCord(pos) == Blocks.multiAir
    }

    def isAirBlockChunkCord(pos: BlockWorldPos): Boolean = {
        getBlockChunkCord(pos) == Blocks.multiAir
    }

    def updateRandomBlocks(rand: Random): Unit = {
        for (i <- 0 to 2) {
            updateLCG = updateLCG * 3 + 1013904223
            val i2: Int = updateLCG >> 2
            val x: Int = i2 & 15
            val y: Int = i2 >> 8 & 15
            val z: Int = i2 >> 16 & 15
            //  getBlockChunkCord(x, y, z).randomUpdate(world, position.blockPosLocalToWorld(new BlockWorldPos(x, y, z)), rand)
        }
    }

    def setBlockWorldCord(pos: BlockWorldPos, block: Block): Unit = setBlockChunkCord(position.blockPosWorldToLocal(pos), block)

    def setBlockChunkCord(pos: BlockWorldPos, block: Block): Boolean = {


        val id = MultiBlock.getId(block)
        val x = pos.worldX
        val y = pos.worldY
        val z = pos.worldZ


        if (block == Blocks.air) {
            val targetId = blockStorage.getBlockId(x, y, z)
            if (targetId == MultiBlock.id) {
                val multi = blockStorage.getMultiBlock(x, y, z)
                multi.putBlock(pos.multiPos, block)
                if (multi.isEmpty()) {
                    blockStorage.setBlockId(x, y, z, id)
                    blockStorage.removeMultiBlock(x, y, z)
                }
            } else {
                blockStorage.setBlockId(x, y, z, id)
            }
            true
        } else {
            if (id == MultiBlock.id) {
                if (isAirBlockChunkCord(pos)) {
                    blockStorage.setBlockId(x, y, z, id)
                    blockStorage.setMultiBlock(x, y, z, new MultiBlock(block, pos.multiPos))
                    true
                } else {
                    val multi = blockStorage.getMultiBlock(x, y, z)
                    if (multi.isCanPut(pos, block)) {
                        multi.putBlock(pos.multiPos, block)
                        true
                    } else false


                }
            } else {
                blockStorage.setBlockId(x, y, z, id)
                true
            }
        }
    }


    def getBlockChunkCord(pos: BlockWorldPos): AMultiBlock = {
        val id = blockStorage.getBlockId(pos.worldX, pos.worldY, pos.worldZ)
        var multiBlock: AMultiBlock = null
        if (id == MultiBlock.id) {
            multiBlock = blockStorage.getMultiBlock(pos.worldX, pos.worldY, pos.worldZ)
        } else {
            multiBlock = MultiBlock.getMultiBlock(id)
        }
        multiBlock
    }

    def getBlockWorldCord(pos: BlockWorldPos): AMultiBlock = getBlockChunkCord(position.blockPosWorldToLocal(pos))


    def save(): Unit = {

        val chunk: Array[Byte] = new Array[Byte](4096)
        for (x <- 0 to 15; y <- 0 to 15; z <- 0 to 15) {
            chunk(blockStorage.getIndex(x, y, z)) = blockStorage.getBlockId(x, y, z) toByte

        }
        ChunkLoader.save(Chunk.getIndex(position.x, position.y, position.z) toString, chunk)
    }

}

object Chunk {

    def getIndex(x: Long, y: Long, z: Long): Long = (x & 16777215l) << 40 | (z & 16777215l) << 16 | (y & 65535L)

    val CHUNK_SIZE = 16
}
