package ru.megains.common.block.TEST

import ru.megains.common.block.MicroBlock
import ru.megains.common.block.blockdata.{BlockPos, BlockSize, MultiBlockPos}
import ru.megains.common.physics.{AxisAlignedBB, BlockAxisAlignedBB}


class BlockMicroTest(name: String, val size: Int) extends MicroBlock(name) {


    override val maxHp: Short = size * 50 toShort

    val boundingBox = Array(new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
        BlockSize.S5_16, BlockSize.S5_16, BlockSize.S5_16),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
            BlockSize.S4_16, BlockSize.S4_16, BlockSize.S4_16),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
            BlockSize.S6_16, BlockSize.S6_16, BlockSize.S6_16),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0,
            BlockSize.S7_16, BlockSize.S7_16, BlockSize.S7_16)
    )


    override def getPhysicsBody: BlockAxisAlignedBB = boundingBox(size)

    override def getSelectedBoundingBox(blockPos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = boundingBox(size).sum(offset.floatX, offset.floatY, offset.floatZ)


}
