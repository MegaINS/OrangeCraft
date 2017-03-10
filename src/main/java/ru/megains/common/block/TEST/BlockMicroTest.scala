package ru.megains.common.block.TEST

import ru.megains.common.block.MicroBlock
import ru.megains.common.block.blockdata.{BlockPos, BlockSize, MultiBlockPos}
import ru.megains.common.physics.{AxisAlignedBB, BlockAxisAlignedBB}


class BlockMicroTest(name: String, val size: Int) extends MicroBlock(name) {


    override val maxHp: Int = size * 50

    val boundingBox = Array(new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
        BlockSize.S4_16, BlockSize.S4_16, BlockSize.S4_16),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
            BlockSize.S8_16, BlockSize.S8_16, BlockSize.S8_16),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
            BlockSize.S12_16, BlockSize.S12_16, BlockSize.S12_16)
    )


    override def getPhysicsBody: BlockAxisAlignedBB = boundingBox(size)

    override def getSelectedBoundingBox(blockPos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = boundingBox(size).sum(offset.floatX, offset.floatY, offset.floatZ)


}
