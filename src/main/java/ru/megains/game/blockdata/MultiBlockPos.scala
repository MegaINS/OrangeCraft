package ru.megains.game.blockdata


class MultiBlockPos(val x: BlockSize, val y: BlockSize, val z: BlockSize) {

    val floatX = x.value
    val floatY = y.value
    val floatZ = z.value


    def ==(pos: MultiBlockPos): Boolean = if (pos.x == x && pos.y == y && pos.z == z) true else false


}

object MultiBlockPos {
    val default = new MultiBlockPos(BlockSize.Zero, BlockSize.Zero, BlockSize.Zero)
}
