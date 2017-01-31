package ru.megains.common.multiblock

import org.joml.Vector3d
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockPos, BlockSize, MultiBlockPos}
import ru.megains.common.physics.AxisAlignedBB
import ru.megains.common.register.GameRegister
import ru.megains.common.util.RayTraceResult
import ru.megains.common.world.World

import scala.collection.mutable.ArrayBuffer


class MultiBlockSingle(val block: Block) extends AMultiBlock {

    override def getBlockHp(pos: MultiBlockPos): Int = -100
    override def isFullBlock: Boolean = true

    override def isOpaqueCube: Boolean = block.isOpaqueCube

    override def collisionRayTrace(world: World, blockPos: BlockPos, start: Vector3d, stop: Vector3d): RayTraceResult = {
        if (block.getSelectedBoundingBox(blockPos, MultiBlockPos.default) != BlockSize.NULL_AABB) {
            block.collisionRayTrace(world, blockPos, start, stop, MultiBlockPos.default)
        } else {
            null
        }
    }

    override def renderBlocks(world: World, blockPos: BlockPos, renderPos: BlockPos): Int = {
        if (block.isAir) return 0

        if (GameRegister.getBlockRender(block).render(block, world, blockPos, renderPos)) 1 else 0
    }

    override def addCollisionList(blockPos: BlockPos, aabbs: ArrayBuffer[AxisAlignedBB]): Unit = {

        aabbs += block.getBoundingBox(blockPos, MultiBlockPos.default)
    }

    override def putBlock(pos: MultiBlockPos, block: Block): Unit = {}

    override def isCanPut(pos: BlockPos, block: Block): Boolean = block.isAir

    override def isEmpty: Boolean = false

    override def getBlock(multiPos: MultiBlockPos): Block = block
}

object MultiBlockSingle {

    def initMultiBlockSingle(): Unit = {
        //  GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.air))
        //  GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.stone))
        //  GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.dirt))
        //  GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.grass))
        //  GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.glass))
    }
}
