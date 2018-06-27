package ru.megains.common.world.chunk


import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.multiblock.{AMultiBlock, MultiBlock}
import ru.megains.common.position.ChunkPosition
import ru.megains.common.register.{Blocks, MultiBlocks}
import ru.megains.common.world.World
import ru.megains.common.world.storage.ExtendedBlockStorage

import scala.util.Random


class Chunk(val world: World, val position: ChunkPosition) {



    var isSaved = true
    var isPopulated: Boolean = true
    var blockStorage: ExtendedBlockStorage = new ExtendedBlockStorage

    var updateLCG: Int = new Random().nextInt()

    def this(world: World, position: ChunkPosition, blockStorage: ExtendedBlockStorage) {
        this(world, position)
        this.blockStorage = blockStorage
    }

    def isAirBlockWorldCord(pos: BlockPos): Boolean = {
        getMultiBlockWorldCord(pos) == MultiBlocks.air
    }

    def getMultiBlockWorldCord(pos: BlockPos): AMultiBlock = getBlockChunkCord(position.blockPosWorldToLocal(pos))

    def getBlockWorldCord(pos: BlockPos): Block = getMultiBlockWorldCord(pos).getBlock(pos.multiPos)

    def getBlockChunkCord(pos: BlockPos): AMultiBlock = {
        val id = blockStorage.getBlockId(pos.worldX, pos.worldY, pos.worldZ)
        var multiBlock: AMultiBlock = null
        if (id == MultiBlock.id) {
            multiBlock = blockStorage.getMultiBlock(pos.worldX, pos.worldY, pos.worldZ)
        } else {
            multiBlock = MultiBlock.getMultiBlock(id)
        }
        multiBlock
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

    def setBlockWorldCord(pos: BlockPos, block: Block): Unit = setBlockChunkCord(position.blockPosWorldToLocal(pos), block)

    def setBlockChunkCord(pos: BlockPos, block: Block): Boolean = {

        isSaved = true
        val x = pos.worldX
        val y = pos.worldY
        val z = pos.worldZ

        if (block == Blocks.air) {
            if (blockStorage.isMultiBlock(x, y, z)) {
                val multi = blockStorage.getMultiBlock(x, y, z)
                multi.putBlock(pos.multiPos, block)
                if (multi.isEmpty) {
                    blockStorage.setBlock(x, y, z, block)
                    blockStorage.removeMultiBlock(x, y, z)
                }
            } else {
                blockStorage.setBlock(x, y, z, block)
            }
            true
        } else {
            if (block.format.isStandart) {
                blockStorage.setBlock(x, y, z, block)
                true
            } else {
                if (isAirBlockChunkCord(pos)) {
                    blockStorage.setMultiBlock(x, y, z)
                    blockStorage.setMultiBlock(x, y, z, new MultiBlock(block, pos.multiPos))
                    true
                } else {
                    val multi = blockStorage.getMultiBlock(x, y, z)
                    if (multi.isCanPut(pos, block)) {
                        multi.putBlock(pos.multiPos, block)
                        true
                    } else false
                }
            }
        }
    }

    def isAirBlockChunkCord(pos: BlockPos): Boolean = {
        getBlockChunkCord(pos) == MultiBlocks.air
    }

    def needsSaving(p_76601_1: Boolean): Boolean = {
        isSaved
    }

    def isVoid: Boolean = {
        val blockData = blockStorage.blocksId
        val airId = Blocks.getIdByBlock(Blocks.air)
        for (i <- 0 until 4096) {
            if (blockData(i) != airId) return false
        }
        true
    }

    def getBlockHp(pos: BlockPos): Int = {
        val posL = position.blockPosWorldToLocal(pos)
        if (blockStorage.isMultiBlock(posL.worldX, posL.worldY, posL.worldZ)) {
            blockStorage.getMultiBlock(posL.worldX, posL.worldY, posL.worldZ).getBlockHp(posL.multiPos)
        } else {
            blockStorage.getBlockHp(posL.worldX, posL.worldY, posL.worldZ)
        }
    }

    def getBlockMeta(pos: BlockPos): Int = {
        val posL = position.blockPosWorldToLocal(pos)
        if (blockStorage.isMultiBlock(posL.worldX, posL.worldY, posL.worldZ)) {
            blockStorage.getMultiBlock(posL.worldX, posL.worldY, posL.worldZ).getBlockMeta(posL.multiPos)
        } else {
            blockStorage.getBlockMeta(posL.worldX, posL.worldY, posL.worldZ)
        }
    }

    def setBlockMeta(pos: BlockPos, meta: Int): Unit = {
        val posL = position.blockPosWorldToLocal(pos)
        if (blockStorage.isMultiBlock(posL.worldX, posL.worldY, posL.worldZ)) {
            blockStorage.getMultiBlock(posL.worldX, posL.worldY, posL.worldZ).setBlockMeta(posL.multiPos, meta)
        } else {
            blockStorage.setBlockMeta(posL.worldX, posL.worldY, posL.worldZ, meta)
        }
    }
}

object Chunk {

    val CHUNK_SIZE = 16

    def getIndex(chunk: Chunk): Long = getIndex(chunk.position.chunkX, chunk.position.chunkY, chunk.position.chunkZ)

    def getIndex(position: ChunkPosition): Long = getIndex(position.chunkX, position.chunkY, position.chunkZ)

    def getIndex(x: Long, y: Long, z: Long): Long = (x & 16777215l) << 40 | (z & 16777215l) << 16 | (y & 65535L)
}
