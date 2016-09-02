package ru.megains.game.world.chunk.storage

import ru.megains.game.block.Block
import ru.megains.game.multiblock.{AMultiBlock, MultiBlock}
import ru.megains.game.register.Blocks

import scala.collection.mutable


class ExtendedBlockStorage {


    val data: BitArray = new BitArray(4096, 16)
    val multiBlockStorage: mutable.HashMap[Int, MultiBlock] = new mutable.HashMap[Int, MultiBlock]

    def getIndex(x: Int, y: Int, z: Int): Int = x << 8 | y << 4 | z

    def getBlockId(x: Int, y: Int, z: Int) = data.get(getIndex(x, y, z))

    def getBlock(x: Int, y: Int, z: Int) = Block.getBlockById(data.get(getIndex(x, y, z)))

    def setBlockId(x: Int, y: Int, z: Int, value: Int) = data.set(getIndex(x, y, z), value)

    def setMultiBlock(x: Int, y: Int, z: Int, value: MultiBlock): Unit = {
        multiBlockStorage += getIndex(x, y, z) -> value

    }

    def removeMultiBlock(x: Int, y: Int, z: Int): Unit = {
        multiBlockStorage.remove(getIndex(x, y, z))
    }

    def getMultiBlock(x: Int, y: Int, z: Int): AMultiBlock = multiBlockStorage.getOrElse(
        getIndex(x, y, z),
        default = {
            println("Error not multiBlock " + x + " " + y + " " + z)
            Blocks.multiAir
        }
    )

}
