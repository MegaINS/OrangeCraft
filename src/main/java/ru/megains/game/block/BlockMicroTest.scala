package ru.megains.game.block

import ru.megains.game.blockdata.{MultiBlockPos, BlockSize, BlockWorldPos}
import ru.megains.game.physics.{BlockAxisAlignedBB, AxisAlignedBB}


class BlockMicroTest(name:String,val size:Int) extends Block(name){





    val boundingBox = Array(new BlockAxisAlignedBB(BlockSize.Zero,BlockSize.Zero,BlockSize.Zero,
                                                               BlockSize.FourSixteenth,BlockSize.FourSixteenth,BlockSize.FourSixteenth),
        new BlockAxisAlignedBB(BlockSize.Zero,BlockSize.Zero,BlockSize.Zero,
            BlockSize.EightSixteenth,BlockSize.EightSixteenth,BlockSize.EightSixteenth),
        new BlockAxisAlignedBB(BlockSize.Zero,BlockSize.Zero,BlockSize.Zero,
            BlockSize.TwelveSixteenth,BlockSize.TwelveSixteenth,BlockSize.TwelveSixteenth)
    )


    override def getPhysicsBody:BlockAxisAlignedBB = boundingBox(size)

    override def getSelectedBoundingBox ( blockPos:BlockWorldPos,offset:MultiBlockPos) = boundingBox(size).sum(offset.floatX,offset.floatY,offset.floatZ)

    override def isOpaqueCube: Boolean = false

    override def isFullBlock: Boolean = false

}
