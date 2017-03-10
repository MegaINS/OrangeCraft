package ru.megains.common.block.blockdata

import ru.megains.common.physics.BlockAxisAlignedBB

import scala.collection.mutable.ArrayBuffer


class BlockSize private(val id: Int, val value: Float) {

}

object BlockSize {

    private var id = 0
    private val array: ArrayBuffer[BlockSize] = ArrayBuffer[BlockSize]()
    private val min: Float = 0.0625f

    def apply(value: Float): BlockSize = {
        if (value % min != 0) {
            println(s"BlockSize($value % $min == 0)")
            System.exit(-1)
            null
        } else {
            val bs = new BlockSize(id, value)
            id += 1
            array += bs
            bs
        }
    }

    val S0 = BlockSize(0)
    val S1_16 = BlockSize(1.0f * min)
    val S2_16 = BlockSize(2.0f * min)
    val S3_16 = BlockSize(3.0f * min)
    val S4_16 = BlockSize(4.0f * min)
    val S5_16 = BlockSize(5.0f * min)
    val S6_16 = BlockSize(6.0f * min)
    val S7_16 = BlockSize(7.0f * min)
    val S8_16 = BlockSize(8.0f * min)
    val S9_16 = BlockSize(9.0f * min)
    val S10_16 = BlockSize(10.0f * min)
    val S11_16 = BlockSize(11.0f * min)
    val S12_16 = BlockSize(12.0f * min)
    val S13_16 = BlockSize(13.0f * min)
    val S14_16 = BlockSize(14.0f * min)
    val S15_16 = BlockSize(15.0f * min)
    val S1 = BlockSize(1.0f)

    val FULL_AABB = new BlockAxisAlignedBB(S0, S0, S0, S1, S1, S1)
    val NULL_AABB = new BlockAxisAlignedBB(S0, S0, S0, S0, S0, S0)

    def getForId(id: Int): BlockSize = array(id)

    def get(value: Float): BlockSize = {
        array.find(_.value == ((value / min).round * min)).get
    }

}