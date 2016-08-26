package ru.megains.game.multiblock

import org.joml.Vector3f
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{MultiBlockPos, BlockWorldPos}
import ru.megains.game.physics.AxisAlignedBB
import ru.megains.game.util.RayTraceResult
import ru.megains.game.world.World

import scala.collection.mutable.ArrayBuffer


abstract class AMultiBlock {



    def addCollisionList(blockPos: BlockWorldPos, aabbs: ArrayBuffer[AxisAlignedBB]):Unit

    def renderBlocks(world:World, blockPos:BlockWorldPos, renderPos:BlockWorldPos):Int

    def collisionRayTrace(world: World, blockPos: BlockWorldPos, start: Vector3f, stop: Vector3f):RayTraceResult

    def isOpaqueCube: Boolean

    def isFullBlock: Boolean

    def putBlock(pos: MultiBlockPos,block: Block ):Unit

    def isCanPut(pos: BlockWorldPos,block: Block ): Boolean

    def isEmpty(): Boolean
}
