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

    val blockData: mutable.HashMap[MultiBlockPos, (Block, Int, Int)] = new mutable.HashMap[MultiBlockPos, (Block, Int, Int)]

    def this(block: Block, blockPos: MultiBlockPos) {
        this()
        putBlock(blockPos, block)
    }

    override def putBlock(pos: MultiBlockPos, block: Block): Unit = {

        if (block == Blocks.air) {
            var key: MultiBlockPos = null
            blockData.foreach {
                case (blockPos, _) =>
                    if (blockPos == pos) key = blockPos
            }
            if (key != null) {
                blockData.remove(key)
            }
        } else {
            blockData += pos -> {
                (block, block.maxHp, 0)
            }
        }
    }


    override def isFullBlock: Boolean = false

    override def isOpaqueCube: Boolean = false

    override def collisionRayTrace(world: World, blockPos: BlockPos, start: Vector3d, stop: Vector3d): RayTraceResult = {

        var rayTraceResult: RayTraceResult = null
        blockData.foreach {
            case (inBlockPos, block) =>
                rayTraceResult = block._1.collisionRayTrace(world, blockPos, start, stop, inBlockPos)
                if (rayTraceResult != null) return rayTraceResult
        }
        rayTraceResult
    }

    override def renderBlocks(world: World, blockPos: BlockPos, renderPos: BlockPos): Int = {
        var renders = 0

        blockData.foreach(
            (bd: (MultiBlockPos, (Block, Int, Int))) => {
                val block = bd._2._1
                if (!block.isAir) {
                    GameRegister.getBlockRender(block).render(block, world, blockPos, renderPos, bd._1)
                    renders += 1
                }
            }
        )
        renders
    }

    override def addCollisionList(blockPos: BlockPos, aabbs: ArrayBuffer[AxisAlignedBB]): Unit = {

        blockData.foreach {
            case (multiBlockPos, (block, _, _)) =>
                aabbs += block.getBoundingBox(blockPos, multiBlockPos)
        }
    }

    override def isCanPut(pos: BlockPos, block: Block): Boolean = {
        val aabb = block.getPhysicsBody.getCopy.move(pos.multiPos.floatX, pos.multiPos.floatY, pos.multiPos.floatZ)

        blockData.forall((bd: (MultiBlockPos, (Block, Int, Int))) => {
            !aabb.checkCollision(bd._2._1.getPhysicsBody.getCopy.move(bd._1.floatX, bd._1.floatY, bd._1.floatZ))
        })

    }

    override def isEmpty: Boolean = blockData.isEmpty

    override def getBlock(multiPos: MultiBlockPos): Block = {
        val bd = blockData.find((bd: (MultiBlockPos, (Block, Int, Int))) => {
            bd._1 == multiPos
        })
        if (bd.isEmpty) {
            Blocks.air
        } else {
            bd.get._2._1
        }
    }

    def getBlockHp(pos: MultiBlockPos): Int = {
        val bd = blockData.find((bd: (MultiBlockPos, (Block, Int, Int))) => {
            bd._1 == pos
        })
        if (bd.isEmpty) {
            -1
        } else {
            bd.get._2._2
        }
    }

    def getBlockMeta(pos: MultiBlockPos): Int = {

        val bd = blockData.find { case (blockPos, _) => blockPos == pos }

        if (bd.isEmpty) {
            -1
        } else {
            bd.get._2._3
        }
    }

    override def setBlockMeta(pos: MultiBlockPos, meta: Int): Unit = {
        val bd = blockData.find { case (blockPos, _) => blockPos == pos }
        if (bd.nonEmpty) {
            bd.get._2 match {
                case (block, hp, _) => blockData.update(pos, (block, hp, meta))
            }
        }
    }
}

object MultiBlock {
    val id = 0

    def getId(block: Block): Int = GameRegister.getMultiBlockId(block)

    def getMultiBlock(id: Int): AMultiBlock = GameRegister.getMultiBlock(id)
}

