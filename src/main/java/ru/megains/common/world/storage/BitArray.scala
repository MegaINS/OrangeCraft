package ru.megains.common.world.storage

import scala.collection.immutable.IndexedSeq


class BitArray(arraySize: Int, bitLength: Int) {


    val size: Int = 64 / bitLength
    val array = new Array[Long](arraySize / size)
    val mask: IndexedSeq[Long] = for (i <- 1 to size) yield Long.MaxValue - (1l << (if (bitLength * i == 64) 63 else bitLength * i)) + (1l << bitLength * (i - 1))

    def set(index: Int, value: Long): Unit = {

        val i = index & size - 1
        val bitIndex = index / size
        val va = value << i * bitLength | array(bitIndex) & mask(i)

        array(bitIndex) = va
    }

    def get(index: Int): Int = {
        val i = (index & size - 1) * bitLength
        val j = array(index / size) >> i & ((1l << bitLength) - 1) toInt;
        j
    }
}

