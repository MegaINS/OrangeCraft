package ru.megains.game.position

import ru.megains.game.blockdata.BlockWorldPos
import ru.megains.game.world.chunk.Chunk


class ChunkPosition(val x: Int, val y: Int, val z: Int) {


    val minX: Int = x * Chunk.CHUNK_SIZE
    val minY: Int = y * Chunk.CHUNK_SIZE
    val minZ: Int = z * Chunk.CHUNK_SIZE
    val maxX = minX + Chunk.CHUNK_SIZE
    val maxY = minY + Chunk.CHUNK_SIZE
    val maxZ = minZ + Chunk.CHUNK_SIZE

    def localX(x: Int) = x - minX

    def localY(y: Int) = y - minY

    def localZ(z: Int) = z - minZ

    def blockPosLocalToWorld(blockPos: BlockWorldPos): BlockWorldPos = blockPos.sum(minX, minY, minZ)

    def blockPosWorldToLocal(blockPos: BlockWorldPos): BlockWorldPos = blockPos.sum(-minX, -minY, -minZ)

}
