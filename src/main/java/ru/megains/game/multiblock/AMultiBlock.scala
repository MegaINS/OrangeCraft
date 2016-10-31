package ru.megains.game.multiblock

import org.joml.Vector3d
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockPos, MultiBlockPos}
import ru.megains.game.physics.AxisAlignedBB
import ru.megains.game.util.RayTraceResult
import ru.megains.game.world.World

import scala.collection.mutable.ArrayBuffer


abstract class AMultiBlock {


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
