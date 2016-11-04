package ru.megains.common.multiblock

import org.joml.Vector3d
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockPos, MultiBlockPos}
import ru.megains.common.physics.AxisAlignedBB
import ru.megains.common.register.{Blocks, GameRegister}
import ru.megains.common.util.RayTraceResult
import ru.megains.common.world.World

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

    override def collisionRayTrace(world: World, blockPos: BlockPos, start: Vector3d, stop: Vector3d): RayTraceResult = {

        var rayTraceResult: RayTraceResult = null


        blockData.foreach((bd: (MultiBlockPos, Block)) => {
            rayTraceResult = bd._2.collisionRayTrace(world, blockPos, start, stop, bd._1)
            if (rayTraceResult != null) return rayTraceResult
        })
        null
    }

    override def renderBlocks(world: World, blockPos: BlockPos, renderPos: BlockPos): Int = {
        var renders = 0

        blockData.foreach(
            (bd: (MultiBlockPos, Block)) => {
                if (!bd._2.isAir) {
                    GameRegister.getBlockRender(bd._2).render(bd._2, world, blockPos, renderPos, bd._1)
                    renders += 1
                }
            }
        )
        renders
    }

    override def addCollisionList(blockPos: BlockPos, aabbs: ArrayBuffer[AxisAlignedBB]): Unit = {

        blockData.foreach((bd: (MultiBlockPos, Block)) => {
            aabbs += bd._2.getBoundingBox(blockPos, bd._1)
        })

    }

    override def isCanPut(pos: BlockPos, block: Block): Boolean = {
        val aabb = block.getPhysicsBody.getCopy.move(pos.blockX.value, pos.blockY.value, pos.blockZ.value)

        blockData.forall((bd: (MultiBlockPos, Block)) => {
            !aabb.checkCollision(bd._2.getPhysicsBody.getCopy.move(bd._1.floatX, bd._1.floatY, bd._1.floatZ))
        })

    }

    override def isEmpty: Boolean = blockData.isEmpty

    override def getBlock(multiPos: MultiBlockPos): Block = {
        val bd = blockData.find((bd: (MultiBlockPos, Block)) => {
            bd._1 == multiPos
        })
        if (bd.isEmpty) {
            Blocks.air
        } else {
            bd.get._2
        }
    }
}

object MultiBlock {
    val id = 0

    def getId(block: Block) = GameRegister.getMultiBlockId(block)

    def getMultiBlock(id: Int): AMultiBlock = GameRegister.getMultiBlock(id)
}

