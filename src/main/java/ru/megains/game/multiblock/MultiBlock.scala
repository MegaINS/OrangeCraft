package ru.megains.game.multiblock

import org.joml.Vector3f
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockWorldPos, MultiBlockPos}
import ru.megains.game.physics.AxisAlignedBB
import ru.megains.game.register.{Blocks, GameRegister}
import ru.megains.game.util.RayTraceResult
import ru.megains.game.world.World

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class MultiBlock() extends AMultiBlock {

    val blockData: mutable.HashMap[MultiBlockPos, Block] = new mutable.HashMap[MultiBlockPos, Block]

    def this(block: Block, blockPos: MultiBlockPos) {
        this()
        putBlock(blockPos, block)
    }

    override def putBlock(pos: MultiBlockPos, block: Block) = {

        if (block == Blocks.air) {
            var key: MultiBlockPos = null
            blockData.foreach((bd: (MultiBlockPos, Block)) => {
                if (bd._1 == pos) key = bd._1
            })
            if (key != null) {
                blockData.remove(key)
            }
        } else {
            blockData += pos -> block
        }


    }

    override def isFullBlock: Boolean = false

    override def isOpaqueCube: Boolean = false

    override def collisionRayTrace(world: World, blockPos: BlockWorldPos, start: Vector3f, stop: Vector3f): RayTraceResult = {

        var rayTraceResult: RayTraceResult = null


        blockData.foreach((bd: (MultiBlockPos, Block)) => {
            rayTraceResult = bd._2.collisionRayTrace(world, blockPos, start, stop, bd._1)
            if (rayTraceResult != null) return rayTraceResult
        })
        null
    }

    override def renderBlocks(world: World, blockPos: BlockWorldPos, renderPos: BlockWorldPos): Int = {
        var renders = 0

        blockData.foreach((bd: (MultiBlockPos, Block)) => {
            if (!bd._2.isAir) {
                GameRegister.getBlockRender(bd._2).render(bd._2, world, blockPos, renderPos, bd._1)
                renders += 1
            }
        })
        renders
    }

    override def addCollisionList(blockPos: BlockWorldPos, aabbs: ArrayBuffer[AxisAlignedBB]): Unit = {

        blockData.foreach((bd: (MultiBlockPos, Block)) => {
            aabbs += bd._2.getBoundingBox(blockPos, bd._1)
        })

    }

    override def isCanPut(pos: BlockWorldPos, block: Block): Boolean = {
        val aabb = block.getPhysicsBody.getCopy.move(pos.blockX.value, pos.blockY.value, pos.blockZ.value)

        blockData.forall((bd: (MultiBlockPos, Block)) => {
            !aabb.checkCollision(bd._2.getPhysicsBody.getCopy.move(bd._1.floatX, bd._1.floatY, bd._1.floatZ))
        })

    }

    override def isEmpty: Boolean = blockData.isEmpty
}

object MultiBlock {
    val id = 0

    def getId(block: Block) = GameRegister.getMultiBlockId(block)

    def getMultiBlock(id: Int): AMultiBlock = GameRegister.getMultiBlock(id)
}

