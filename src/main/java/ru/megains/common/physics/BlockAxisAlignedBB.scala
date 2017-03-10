package ru.megains.common.physics

import ru.megains.common.block.blockdata.BlockSize


class BlockAxisAlignedBB(val BSMinX: BlockSize,
                         val BSMinY: BlockSize,
                         val BSMinZ: BlockSize,
                         val BSMaxX: BlockSize,
                         val BSMaxY: BlockSize,
                         val BSMaxZ: BlockSize) extends AxisAlignedBB(
    BSMinX.value,
    BSMinY.value,
    BSMinZ.value,
    BSMaxX.value,
    BSMaxY.value,
    BSMaxZ.value) {


}

object BlockAxisAlignedBB {
    //    def round(blockAABB:BlockAxisAlignedBB): BlockAxisAlignedBB ={
    //
    //    }
}