package ru.megains.common.position

import ru.megains.common.block.blockdata.BlockPos
import ru.megains.common.entity.Entity
import ru.megains.common.world.chunk.Chunk


class ChunkPosition(val chunkX: Int, val chunkY: Int, val chunkZ: Int) {


    val minX: Int = chunkX * Chunk.CHUNK_SIZE
    val minY: Int = chunkY * Chunk.CHUNK_SIZE
    val minZ: Int = chunkZ * Chunk.CHUNK_SIZE
    val maxX = minX + Chunk.CHUNK_SIZE
    val maxY = minY + Chunk.CHUNK_SIZE
    val maxZ = minZ + Chunk.CHUNK_SIZE

    def localX(x: Int) = x - minX

    def localY(y: Int) = y - minY

    def localZ(z: Int) = z - minZ

    def blockPosLocalToWorld(blockPos: BlockPos): BlockPos = blockPos.sum(minX, minY, minZ)

    def blockPosWorldToLocal(blockPos: BlockPos): BlockPos = blockPos.sum(-minX, -minY, -minZ)

    def getDistanceSq(entity: Entity): Double = {
        val x0 = (minX * 16 + 8).toDouble
        val y0 = (minY * 16 + 8).toDouble
        val z0 = (minZ * 16 + 8).toDouble

        val x1 = x0 - entity.posX
        val y1 = y0 - entity.posY
        val z1 = z0 - entity.posZ
        x1 * x1 + y1 * y1 + z1 * z1
    }

}
