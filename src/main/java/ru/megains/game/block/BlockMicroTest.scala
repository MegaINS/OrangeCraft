package ru.megains.game.block

import ru.megains.game.blockdata.{BlockPos, BlockSize, MultiBlockPos}
import ru.megains.game.physics.BlockAxisAlignedBB


class BlockMicroTest(name: String, val size: Int) extends Block(name) {


    val boundingBox = Array(new BlockAxisAlignedBB(BlockSize.Zero, BlockSize.Zero, BlockSize.Zero,
        BlockSize.FourSixteenth, BlockSize.FourSixteenth, BlockSize.FourSixteenth),
        new BlockAxisAlignedBB(BlockSize.Zero, BlockSize.Zero, BlockSize.Zero,
            BlockSize.EightSixteenth, BlockSize.EightSixteenth, BlockSize.EightSixteenth),
        new BlockAxisAlignedBB(BlockSize.Zero, BlockSize.Zero, BlockSize.Zero,
            BlockSize.TwelveSixteenth, BlockSize.TwelveSixteenth, BlockSize.TwelveSixteenth)
    )


    override def getPhysicsBody: BlockAxisAlignedBB = boundingBox(size)

    override def getSelectedBoundingBox(blockPos: BlockPos, offset: MultiBlockPos) = boundingBox(size).sum(offset.floatX, offset.floatY, offset.floatZ)

    override def isOpaqueCube: Boolean = false

    override def isFullBlock: Boolean = false

}
