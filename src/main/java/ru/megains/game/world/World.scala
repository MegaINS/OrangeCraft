package ru.megains.game.world


import org.joml.Vector3f
import ru.megains.game.block.Block
import ru.megains.game.blockdata.{BlockDirection, BlockWorldPos}
import ru.megains.game.entity.Entity
import ru.megains.game.entity.item.EntityItem
import ru.megains.game.multiblock.AMultiBlock
import ru.megains.game.physics.AxisAlignedBB
import ru.megains.game.position.ChunkPosition
import ru.megains.game.register.{Blocks, GameRegister}
import ru.megains.game.util.{MathHelper, RayTraceResult}
import ru.megains.game.world.chunk.{Chunk, ChunkVoid}
import ru.megains.game.world.storage.{AnvilSaveHandler, ChunkLoader}
import ru.megains.renderer.world.WorldRenderer
import ru.megains.utils.Logger

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class World(saveHandler: AnvilSaveHandler) extends Logger[World] {


    val chunkLoader: ChunkLoader = saveHandler.getChunkLoader


    val heightMap: WorldHeightMap = new WorldHeightMap(15561165)
    // The length of the world from -8000000 to 8000000
    val length: Int = 8000000
    // The width of the world from -8000000 to 8000000
    val width: Int = 8000000
    // The height of the world from -30000 to 30000
    val height: Int = 30000

    val chunks: mutable.HashMap[Long, Chunk] = new mutable.HashMap[Long, Chunk]
    val rand: Random = new Random()
    val entities: ArrayBuffer[Entity] = new ArrayBuffer[Entity]()
    var worldRenderer: WorldRenderer = _

    def init() {


        for (i <- 1 to 10) {
            val entity = new EntityItem(this, GameRegister.getItemById(rand.nextInt(4) + 2))
            entity.setWorld(this)
            entity.setPosition(rand.nextInt(i) - i / 2, 5, rand.nextInt(i) - i / 2)
            entities += entity
        }


    }

    def update() {
        entities.foreach(_.update())

        chunks.values.foreach(_.updateRandomBlocks(rand))


    }

    def setAirBlock(pos: BlockWorldPos) {
        setBlock(pos, Blocks.air)
    }

    def setBlock(pos: BlockWorldPos, block: Block) {
        if (!validBlockPos(pos)) {
            return;
        }


        getChunk(pos).setBlockWorldCord(pos, block)
        worldRenderer.reRender(pos)
    }

    def spawnEntityInWorld(entity: Entity): Unit = {
        entities += entity
    }

    def isAirBlock(blockPos: BlockWorldPos): Boolean = if (validBlockPos(blockPos)) getChunk(blockPos).isAirBlockWorldCord(blockPos) else true

    def isOpaqueCube(blockPos: BlockWorldPos): Boolean = getBlock(blockPos).isOpaqueCube

    def getBlock(pos: BlockWorldPos): AMultiBlock = if (!validBlockPos(pos)) Blocks.multiAir else getChunk(pos).getBlockWorldCord(pos)

    def getChunk(blockPos: BlockWorldPos): Chunk = getChunk(blockPos.worldX >> 4, blockPos.worldY >> 4, blockPos.worldZ >> 4)

    def getChunk(x: Int, y: Int, z: Int): Chunk = {
        try {
            val index = Chunk.getIndex(x, y, z)
            if (chunks.contains(index)) {
                chunks(index)
            } else {
                chunkLoader.loadChunk(this, x, y, z)
                // ChunkLoader.loadChunk(this, x, y, z)
                val chunk = new ChunkVoid(this, new ChunkPosition(x, y, z))
                addChunk(index, chunk)
                chunk
            }
        } catch {
            case e: NoSuchElementException =>
                println(x + " " + y + " " + z)
                null
        }

    }

    def validBlockPos(pos: BlockWorldPos): Boolean = !(pos.worldZ < -width || pos.worldY < -height || pos.worldX < -length) && !(pos.worldZ > width - 1 || pos.worldY > height - 1 || pos.worldX > length - 1)

    def addBlocksInList(aabb: AxisAlignedBB): mutable.ArrayBuffer[AxisAlignedBB] = {
        var x0: Int = Math.floor(aabb.getMinX).toInt
        var y0: Int = Math.floor(aabb.getMinY).toInt
        var z0: Int = Math.floor(aabb.getMinZ).toInt
        var x1: Int = Math.ceil(aabb.getMaxX).toInt
        var y1: Int = Math.ceil(aabb.getMaxY).toInt
        var z1: Int = Math.ceil(aabb.getMaxZ).toInt
        if (x0 < -length) {
            x0 = -length
        }
        if (y0 < -height) {
            y0 = -height
        }
        if (z0 < -width) {
            z0 = -width
        }
        if (x1 > length) {
            x1 = length
        }
        if (y1 > height) {
            y1 = height
        }
        if (z1 > width) {
            z1 = width
        }
        var blockPos: BlockWorldPos = null
        val aabbs = mutable.ArrayBuffer[AxisAlignedBB]()

        for (x <- x0 to x1; y <- y0 to y1; z <- z0 to z1) {

            blockPos = new BlockWorldPos(x, y, z)
            if (!isAirBlock(blockPos)) {
                getBlock(blockPos).addCollisionList(blockPos, aabbs)
            }
        }
        aabbs
    }

    def save(): Unit = {
        log.info("World saved...")
        chunks.values.foreach(chunkLoader.save)
        log.info("World saved completed")
    }

    def rayTraceBlocks(vec1: Vector3f, vec32: Vector3f, stopOnLiquid: Boolean, ignoreBlockWithoutBoundingBox: Boolean, returnLastUncollidableBlock: Boolean): RayTraceResult = {

        var vec31: Vector3f = vec1
        val i: Int = MathHelper.floor_double(vec32.x)
        val j: Int = MathHelper.floor_double(vec32.y)
        val k: Int = MathHelper.floor_double(vec32.z)
        var l: Int = MathHelper.floor_double(vec31.x)
        var i1: Int = MathHelper.floor_double(vec31.y)
        var j1: Int = MathHelper.floor_double(vec31.z)
        var blockpos: BlockWorldPos = null
        val raytraceresult2: RayTraceResult = null


        var k1: Int = 200
        while (k1 >= 0) {
            k1 -= 1
            if (l == i && i1 == j && j1 == k) {
                return if (returnLastUncollidableBlock) raytraceresult2 else null
            }
            var flag2: Boolean = true
            var flag: Boolean = true
            var flag1: Boolean = true
            var d0: Double = 999.0D
            var d1: Double = 999.0D
            var d2: Double = 999.0D
            if (i > l) {
                d0 = l + 1.0D
            } else if (i < l) {
                d0 = l + 0.0D
            } else {
                flag2 = false
            }
            if (j > i1) {
                d1 = i1 + 1.0D
            } else if (j < i1) {
                d1 = i1 + 0.0D
            } else {
                flag = false
            }
            if (k > j1) {
                d2 = j1 + 1.0D
            } else if (k < j1) {
                d2 = j1 + 0.0D
            } else {
                flag1 = false
            }
            var d3: Double = 999.0D
            var d4: Double = 999.0D
            var d5: Double = 999.0D
            val d6: Double = vec32.x - vec31.x
            val d7: Double = vec32.y - vec31.y
            val d8: Double = vec32.z - vec31.z
            if (flag2) {
                d3 = (d0 - vec31.x) / d6
            }
            if (flag) {
                d4 = (d1 - vec31.y) / d7
            }
            if (flag1) {
                d5 = (d2 - vec31.z) / d8
            }
            if (d3 == -0.0D) {
                d3 = -1.0E-4f
            }
            if (d4 == -0.0D) {
                d4 = -1.0E-4f
            }
            if (d5 == -0.0D) {
                d5 = -1.0E-4f
            }
            var enumfacing: BlockDirection = null
            if (d3 < d4 && d3 < d5) {
                enumfacing = if (i > l) BlockDirection.WEST else BlockDirection.EAST
                vec31 = new Vector3f(d0 toFloat, vec31.y + d7 * d3 toFloat, vec31.z + d8 * d3 toFloat)
            } else if (d4 < d5) {
                enumfacing = if (j > i1) BlockDirection.DOWN else BlockDirection.UP
                vec31 = new Vector3f(vec31.x + d6 * d4 toFloat, d1 toFloat, vec31.z + d8 * d4 toFloat)
            } else {
                enumfacing = if (k > j1) BlockDirection.NORTH else BlockDirection.SOUTH
                vec31 = new Vector3f(vec31.x + d6 * d5 toFloat, vec31.y + d7 * d5 toFloat, d2 toFloat)
            }
            l = MathHelper.floor_double(vec31.x) - (if (enumfacing == BlockDirection.EAST) 1 else 0)
            i1 = MathHelper.floor_double(vec31.y) - (if (enumfacing == BlockDirection.UP) 1 else 0)
            j1 = MathHelper.floor_double(vec31.z) - (if (enumfacing == BlockDirection.SOUTH) 1 else 0)
            blockpos = new BlockWorldPos(l, i1, j1)
            val block1: AMultiBlock = getBlock(blockpos)
            if (block1 != null) {
                val raytraceresult1: RayTraceResult = block1.collisionRayTrace(this, blockpos, vec31, vec32)
                if (raytraceresult1 != null) {
                    return raytraceresult1
                }
            }

        }
        if (returnLastUncollidableBlock) raytraceresult2 else null

    }

    def addChunk(index: Long, chunk: Chunk): Unit = {
        chunks += index -> chunk
    }
}
