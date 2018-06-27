package ru.megains.common.multiblock

import org.joml.Vector3d
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockPos, MultiBlockPos}
import ru.megains.common.physics.AxisAlignedBB
import ru.megains.common.util.RayTraceResult
import ru.megains.common.world.World

import scala.collection.mutable.ArrayBuffer


abstract class AMultiBlock {

    def setBlockMeta(multiPos: MultiBlockPos, meta: Int): Unit

    def getBlockMeta(pos: MultiBlockPos): Int

    def getBlockHp(pos: MultiBlockPos): Int

    def getBlock(multiPos: MultiBlockPos): Block

    def addCollisionList(blockPos: BlockPos, aabbs: ArrayBuffer[AxisAlignedBB]): Unit

    def renderBlocks(world: World, blockPos: BlockPos, renderPos: BlockPos): Int

    def collisionRayTrace(world: World, blockPos: BlockPos, start: Vector3d, stop: Vector3d): RayTraceResult

    def isOpaqueCube: Boolean

    def isFullBlock: Boolean

    def putBlock(pos: MultiBlockPos, block: Block): Unit

    def isCanPut(pos: BlockPos, block: Block): Boolean

    def isEmpty: Boolean
}
