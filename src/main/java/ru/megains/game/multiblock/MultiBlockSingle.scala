package ru.megains.game.multiblock

import org.joml.Vector3f
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockSize, BlockWorldPos, MultiBlockPos}
import ru.megains.game.physics.AxisAlignedBB
import ru.megains.game.register.{Blocks, GameRegister}
import ru.megains.game.util.RayTraceResult
import ru.megains.game.world.World

import scala.collection.mutable.ArrayBuffer


class MultiBlockSingle(val block: Block) extends AMultiBlock {


    override def isFullBlock: Boolean = true

    override def isOpaqueCube: Boolean = block.isOpaqueCube

    override def collisionRayTrace(world: World, blockPos: BlockWorldPos, start: Vector3f, stop: Vector3f): RayTraceResult = {
        if (block.getSelectedBoundingBox(blockPos, MultiBlockPos.default) != BlockSize.NULL_AABB) {
            block.collisionRayTrace(world, blockPos, start, stop, MultiBlockPos.default)
        } else {
            null
        }
    }

    override def renderBlocks(world: World, blockPos: BlockWorldPos, renderPos: BlockWorldPos): Int = {
        if (block.isAir) {
            return 0
        }
        GameRegister.getBlockRender(block).render(block, world, blockPos, renderPos)
        1
    }

    override def addCollisionList(blockPos: BlockWorldPos, aabbs: ArrayBuffer[AxisAlignedBB]): Unit = {

        aabbs += block.getBoundingBox(blockPos, MultiBlockPos.default)
    }

    override def putBlock(pos: MultiBlockPos, block: Block): Unit = {}

    override def isCanPut(pos: BlockWorldPos, block: Block): Boolean = block.isAir

    override def isEmpty(): Boolean = false
}

object MultiBlockSingle {

    def initMultiBlockSingle(): Unit = {
        GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.air))
        GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.stone))
        GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.dirt))
        GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.grass))
        GameRegister.registerMultiBlockSingle(new MultiBlockSingle(Blocks.glass))
    }
}
