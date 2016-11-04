package ru.megains.common.position

import ru.megains.common.block.blockdata.BlockPos
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

}
