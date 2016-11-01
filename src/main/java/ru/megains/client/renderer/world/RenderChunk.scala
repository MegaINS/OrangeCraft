package ru.megains.client.renderer.world

import ru.megains.client.renderer.mesh.{Mesh, MeshMaker}
import ru.megains.game.blockdata.BlockPos
import ru.megains.game.managers.TextureManager
import ru.megains.game.multiblock.AMultiBlock
import ru.megains.game.physics.AxisAlignedBB
import ru.megains.game.world.World
import ru.megains.game.world.chunk.{Chunk, ChunkVoid}
import ru.megains.utils.Logger

class RenderChunk(val chunk: Chunk, textureManager: TextureManager) extends Logger[RenderChunk] {


    val isVoid: Boolean = chunk.isInstanceOf[ChunkVoid]
    val world: World = chunk.world
    val minX: Int = chunk.position.minX
    val minY: Int = chunk.position.minY
    val minZ: Int = chunk.position.minZ
    val cube: AxisAlignedBB = new AxisAlignedBB(minX, minY, minZ, chunk.position.maxX, chunk.position.maxY, chunk.position.maxZ)


    var isReRender: Boolean = true
    var blockRender: Int = 0
    val meshes: Array[Mesh] = new Array[Mesh](2)

    def render(layer: Int) {
        if (!isVoid) {
            if (isReRender) if (RenderChunk.rend < 2) {
                if (!chunk.isVoid) {
                    blockRender = 0
                    cleanUp()
                    makeChunk(0)
                }
                isReRender = false
                RenderChunk.rend += 1
                RenderChunk.chunkUpdate += 1
            }
            renderChunk(layer)
        }

    }

    private def makeChunk(layer: Int) {

        log.debug(s"Start make chunk $minX $minY $minZ")
        MeshMaker.startMakeTriangles()
        MeshMaker.setTexture(TextureManager.locationBlockTexture)
        var blockPos: BlockPos = null
        var renderPos: BlockPos = null
        var block: AMultiBlock = null

        for (x <- 0 until Chunk.CHUNK_SIZE; y <- 0 until Chunk.CHUNK_SIZE; z <- 0 until Chunk.CHUNK_SIZE) {
            blockPos = new BlockPos(x + minX, y + minY, z + minZ)
            renderPos = new BlockPos(x, y, z)
            if (!world.isAirBlock(blockPos)) {
                block = chunk.getBlockWorldCord(blockPos)
                blockRender += block.renderBlocks(world, blockPos, renderPos)
            }
        }
        meshes(layer) = MeshMaker.makeMesh()
        log.debug("Chunk completed")
    }

    def cleanUp() {
        for (mesh <- meshes) {
            if (mesh ne null) mesh.cleanUp()
        }
    }

    private def renderChunk(layer: Int) {
        if (blockRender != 0) if (meshes(layer) ne null) {
            meshes(layer).render(textureManager)
            RenderChunk.chunkRender += 1
        }
    }

    def reRender() {
        isReRender = true
    }

}

object RenderChunk {
    var chunkRender: Int = 0
    var chunkUpdate: Int = 0
    var rend: Int = 0

    def clearRend() {
        rend = 0
    }
}
