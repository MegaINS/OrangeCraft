package ru.megains.common.block.blockdata


class MultiBlockPos(val x: BlockSize, val y: BlockSize, val z: BlockSize) {

    val floatX: Float = x.value
    val floatY: Float = y.value
    val floatZ: Float = z.value


    def ==(pos: MultiBlockPos): Boolean = if (pos.x == x && pos.y == y && pos.z == z) true else false

    def eg(pos: MultiBlockPos): Boolean = if (pos.x == x && pos.y == y && pos.z == z) true else false

    def getIndex: Int = {
        x.id << 10 | y.id << 5 | z.id
    }



}

object MultiBlockPos {
    val default = new MultiBlockPos(BlockSize.S0, BlockSize.S0, BlockSize.S0)

    def getForIndex(index: Int): MultiBlockPos = {
        new MultiBlockPos(BlockSize.getForId(index >> 10 & 31), BlockSize.getForId(index >> 5 & 31), BlockSize.getForId(index & 31))
    }
}
