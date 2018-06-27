package ru.megains.common.block

import ru.megains.common.block.blockdata.{BlockFormat, BlockPos}
import ru.megains.common.physics.BlockAxisAlignedBB
import ru.megains.common.util.RayTraceResult
import ru.megains.common.world.World

class MacroBlock(name: String) extends Block(name) {

    var blockSize: BlockAxisAlignedBB = _
    override val format: BlockFormat = BlockFormat.MACRO

    override def isOpaqueCube: Boolean = false

    override def getSelectPosition(worldIn: World, objectMouseOver: RayTraceResult): BlockPos = {
        val posTarget: BlockPos = objectMouseOver.getBlockWorldPos
        val posSet: BlockPos = posTarget.sum(objectMouseOver.sideHit)
        if (worldIn.isAirBlock(posSet)) posSet
        else null
    }
}
