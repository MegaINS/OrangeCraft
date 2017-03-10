package ru.megains.common.world.storage


import ru.megains.common.block.Block
import ru.megains.common.multiblock.{AMultiBlock, MultiBlock}
import ru.megains.common.register.{Blocks, MultiBlocks}

import scala.collection.mutable


class ExtendedBlockStorage {


    var blocksId: Array[Short] = new Array[Short](4096)
    var blocksHp: Array[Int] = new Array[Int](4096)
    val multiBlockStorage: mutable.HashMap[Int, MultiBlock] = new mutable.HashMap[Int, MultiBlock]

    def getBlockId(x: Int, y: Int, z: Int): Short = blocksId(getIndex(x, y, z))

    def getBlock(x: Int, y: Int, z: Int): Block = Blocks.getBlockById(blocksId(getIndex(x, y, z)))

    def getIndex(x: Int, y: Int, z: Int): Int = x << 8 | y << 4 | z

    def setBlock(x: Int, y: Int, z: Int, value: Block): Unit = {
        val index = getIndex(x, y, z)
        blocksId(index) = Blocks.getIdByBlock(value).toShort
        blocksHp(index) = value.maxHp
    }

    def setMultiBlock(x: Int, y: Int, z: Int): Unit = {
        val index = getIndex(x, y, z)
        blocksId(index) = MultiBlock.id.toShort
        blocksHp(index) = -1

    }

    def setMultiBlock(x: Int, y: Int, z: Int, value: MultiBlock): Unit = {
        multiBlockStorage += getIndex(x, y, z) -> value

    }

    def isMultiBlock(x: Int, y: Int, z: Int): Boolean = {
        blocksId(getIndex(x, y, z)) == MultiBlock.id
    }

    def removeMultiBlock(x: Int, y: Int, z: Int): Unit = {
        multiBlockStorage.remove(getIndex(x, y, z))
    }

    def getMultiBlock(x: Int, y: Int, z: Int): AMultiBlock = multiBlockStorage.getOrElse(
        getIndex(x, y, z),
        default = {
            //  println("Error not multiBlock " + x + " " + y + " " + z)
            MultiBlocks.air
        }
    )

    def getBlockHp(x: Int, y: Int, z: Int): Int = {
        blocksHp(getIndex(x, y, z))
    }

}
