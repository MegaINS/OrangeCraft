package ru.megains.common.block

import ru.megains.common.block.blockdata.{BlockDirection, BlockFormat, BlockPos, BlockSize}
import ru.megains.common.util.RayTraceResult
import ru.megains.common.world.World

class MicroBlock(name: String) extends Block(name) {

    override val format: BlockFormat = BlockFormat.MICRO

    override def isOpaqueCube: Boolean = false

    override def getSelectPosition(worldIn: World, objectMouseOver: RayTraceResult): BlockPos = {

        var posTarget: BlockPos = objectMouseOver.getBlockWorldPos
        var posSet: BlockPos = null
        posSet = new BlockPos(posTarget.sum(objectMouseOver.sideHit), BlockSize.get(objectMouseOver.hitVec.x), BlockSize.get(objectMouseOver.hitVec.y), BlockSize.get(objectMouseOver.hitVec.z))
        var x: Float = posSet.blockX.value
        var y: Float = posSet.blockY.value
        var z: Float = posSet.blockZ.value
        val bd: BlockDirection = objectMouseOver.sideHit
        if (bd eq BlockDirection.UP) y = 0
        else if (bd eq BlockDirection.EAST) x = 0
        else if (bd eq BlockDirection.SOUTH) z = 0
        else if (bd eq BlockDirection.DOWN) y = 1
        else if (bd eq BlockDirection.WEST) x = 1
        else if (bd eq BlockDirection.NORTH) z = 1
        if (x + getPhysicsBody.getMaxX > 1) x = 1 - getPhysicsBody.getMaxX
        if (y + getPhysicsBody.getMaxY > 1) y = 1 - getPhysicsBody.getMaxY
        if (z + getPhysicsBody.getMaxZ > 1) z = 1 - getPhysicsBody.getMaxZ
        posSet = new BlockPos(posSet, BlockSize.get(x), BlockSize.get(y), BlockSize.get(z))
        if (worldIn.getMultiBlock(posTarget).isFullBlock) {
            if (worldIn.isAirBlock(posSet)) posSet
            else if (worldIn.getMultiBlock(posSet).isCanPut(posSet, this)) posSet
            else null
        } else {
            posTarget = new BlockPos(posTarget, BlockSize.get(objectMouseOver.hitVec.x), BlockSize.get(objectMouseOver.hitVec.y), BlockSize.get(objectMouseOver.hitVec.z))
            var x1: Float = posTarget.blockX.value
            var y1: Float = posTarget.blockY.value
            var z1: Float = posTarget.blockZ.value
            if (bd eq BlockDirection.UP) {
                if (y1 + getPhysicsBody.getMaxY > 1) y1 = 1 - getPhysicsBody.getMaxY
            } else if (bd eq BlockDirection.DOWN) {
                if (y1 - getPhysicsBody.getMaxY <= 0) y1 = 0
                else y1 = y1 - getPhysicsBody.getMaxY
            } else if (bd eq BlockDirection.NORTH) {
                if (z1 - getPhysicsBody.getMaxZ <= 0) z1 = 0
                else z1 = z1 - getPhysicsBody.getMaxZ
            } else if (bd eq BlockDirection.SOUTH) {
                if (z1 + getPhysicsBody.getMaxZ > 1) z1 = 1 - getPhysicsBody.getMaxZ
            } else if (bd eq BlockDirection.WEST) {
                if (x1 - getPhysicsBody.getMaxX <= 0) x1 = 0
                else x1 = x1 - getPhysicsBody.getMaxX
            } else if (bd eq BlockDirection.EAST) if (x1 + getPhysicsBody.getMaxX > 1) x1 = 1 - getPhysicsBody.getMaxX
            if (x1 + getPhysicsBody.getMaxX > 1) x1 = 1 - getPhysicsBody.getMaxX
            if (y1 + getPhysicsBody.getMaxY > 1) y1 = 1 - getPhysicsBody.getMaxY
            if (z1 + getPhysicsBody.getMaxZ > 1) z1 = 1 - getPhysicsBody.getMaxZ
            posTarget = new BlockPos(posTarget, BlockSize.get(x1), BlockSize.get(y1), BlockSize.get(z1))
            if (worldIn.getMultiBlock(posTarget).isCanPut(posTarget, this)) posTarget
            else if (worldIn.isAirBlock(posSet)) posSet
            else if (worldIn.getMultiBlock(posSet).isCanPut(posSet, this)) posSet
            else null
        }
    }
}
