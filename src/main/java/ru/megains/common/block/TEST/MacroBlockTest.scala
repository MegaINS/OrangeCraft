package ru.megains.common.block.TEST

import ru.megains.common.block.MacroBlock
import ru.megains.common.block.blockdata.{BlockPos, BlockSize, MultiBlockPos}
import ru.megains.common.physics.{AxisAlignedBB, BlockAxisAlignedBB}

class MacroBlockTest(name: String, val size: Int) extends MacroBlock(name) {

    override val maxHp: Short = size * 500 toShort
    val S2 = BlockSize(2)
    val S3 = BlockSize(3)
    val S4 = BlockSize(4)
    val boundingBox = Array(new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0, S2, S2, S2),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0, BlockSize.S1, S2, BlockSize.S1),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0, BlockSize.S1, BlockSize.S1, S2),
        new BlockAxisAlignedBB(BlockSize.S0, BlockSize.S0, BlockSize.S0, BlockSize.S1, BlockSize.S1, S2)
    )

    override def getPhysicsBody: BlockAxisAlignedBB = boundingBox(size)

    override def getSelectedBoundingBox(blockPos: BlockPos, offset: MultiBlockPos): AxisAlignedBB = boundingBox(size).sum(offset.floatX, offset.floatY, offset.floatZ)
}
