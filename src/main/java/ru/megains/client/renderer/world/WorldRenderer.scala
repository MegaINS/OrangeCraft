package ru.megains.client.renderer.world

import ru.megains.client.renderer.graph.Transformation
import ru.megains.client.renderer.mesh.{Mesh, MeshMaker}
import ru.megains.client.renderer.{EntityRenderer, Frustum}
import ru.megains.client.world.WorldClient
import ru.megains.common.block.Block
import ru.megains.common.block.blockdata.{BlockPos, MultiBlockPos}
import ru.megains.common.entity.item.EntityItem
import ru.megains.common.entity.player.EntityPlayer
import ru.megains.common.managers.TextureManager
import ru.megains.common.position.ChunkPosition
import ru.megains.common.register.GameRegister

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class WorldRenderer(val world: WorldClient, val textureManager: TextureManager) {


    world.worldRenderer = this

    val renderChunks: mutable.HashMap[Long, RenderChunk] = new mutable.HashMap[Long, RenderChunk]
    var blockMouseOver: Mesh = _
    var blockSelect: Mesh = _
    val range = 5
    var lastX = 0
    var lastY = 0
    var lastZ = 0
    val playerRenderChunks: ArrayBuffer[RenderChunk] = mutable.ArrayBuffer[RenderChunk]()

    def getRenderChunks(entityPlayer: EntityPlayer): ArrayBuffer[RenderChunk] = {
        // TODO:  OPTIMIZE
        val posX: Int = entityPlayer.posX / 16 - (if (entityPlayer.posX < 0) 1 else 0) toInt
        val posY: Int = entityPlayer.posY / 16 - (if (entityPlayer.posY < 0) 1 else 0) toInt
        val posZ: Int = entityPlayer.posZ / 16 - (if (entityPlayer.posZ < 0) 1 else 0) toInt

        if (posX != lastX || posY != lastY || posZ != lastZ) {
            lastX = posX
            lastY = posY
            lastZ = posZ
            playerRenderChunks.clear()
            for (x <- posX - range to posX + range;
                 y <- posY - range to posY + range;
                 z <- posZ - range to posZ + range) {
                playerRenderChunks += getRenderChunk(x, y, z)
            }
        }
        playerRenderChunks
    }


    def reRender(pos: BlockPos) {
        val x: Int = pos.worldX >> 4
        val y: Int = pos.worldY >> 4
        val z: Int = pos.worldZ >> 4
        getRenderChunk(x, y, z).reRender()
        getRenderChunk(x + 1, y, z).reRender()
        getRenderChunk(x - 1, y, z).reRender()
        getRenderChunk(x, y + 1, z).reRender()
        getRenderChunk(x, y - 1, z).reRender()
        getRenderChunk(x, y, z + 1).reRender()
        getRenderChunk(x, y, z - 1).reRender()
    }

    def reRender(position: ChunkPosition): Unit = {
        val x: Int = position.chunkX
        val y: Int = position.chunkY
        val z: Int = position.chunkZ
        getRenderChunk(x, y, z).reRender()
        getRenderChunk(x + 1, y, z).reRender()
        getRenderChunk(x - 1, y, z).reRender()
        getRenderChunk(x, y + 1, z).reRender()
        getRenderChunk(x, y - 1, z).reRender()
        getRenderChunk(x, y, z + 1).reRender()
        getRenderChunk(x, y, z - 1).reRender()
    }

    def reRenderWorld(): Unit = {
        renderChunks.values.foreach(_.reRender())
    }

    def getRenderChunk(x: Int, y: Int, z: Int): RenderChunk = {
        val i: Long = (x & 16777215l) << 40 | (z & 16777215l) << 16 | (y & 65535L)

        if (renderChunks.contains(i) && !renderChunks(i).isVoid) renderChunks(i)
        else {
            val chunkRen = createChunkRen(x, y, z)
            renderChunks += i -> chunkRen
            chunkRen
        }
    }

    def createChunkRen(x: Int, y: Int, z: Int): RenderChunk = {
        new RenderChunk(world.getChunk(x, y, z), textureManager)
    }

    def cleanUp(): Unit = renderChunks.values.foreach(_.cleanUp())

    def updateBlockMouseOver(pos: BlockPos, block: Block): Unit = {
        if (blockMouseOver != null) {
            blockMouseOver.cleanUp()
            blockMouseOver = null
        }

        val mm = MeshMaker.getMeshMaker
        val aabb = block.getSelectedBoundingBox(pos, MultiBlockPos.default).expand(0.01f, 0.01f, 0.01f)

        val minX = aabb.getMinX
        val minY = aabb.getMinY
        val minZ = aabb.getMinZ
        val maxX = aabb.getMaxX
        val maxY = aabb.getMaxY
        val maxZ = aabb.getMaxZ



        mm.startMake(1)
        mm.addVertex(minX, minY, minZ)
        mm.addVertex(minX, minY, maxZ)
        mm.addVertex(minX, maxY, minZ)
        mm.addVertex(minX, maxY, maxZ)
        mm.addVertex(maxX, minY, minZ)
        mm.addVertex(maxX, minY, maxZ)
        mm.addVertex(maxX, maxY, minZ)
        mm.addVertex(maxX, maxY, maxZ)

        mm.addIndex(0, 1)
        mm.addIndex(0, 2)
        mm.addIndex(0, 4)

        mm.addIndex(6, 2)
        mm.addIndex(6, 4)
        mm.addIndex(6, 7)

        mm.addIndex(3, 1)
        mm.addIndex(3, 2)
        mm.addIndex(3, 7)

        mm.addIndex(5, 1)
        mm.addIndex(5, 4)
        mm.addIndex(5, 7)


        blockMouseOver = mm.makeMesh()
    }

    def updateBlockSelect(pos: BlockPos, block: Block): Unit = {
        if (blockSelect != null) {
            blockSelect.cleanUp()
            blockSelect = null
        }

        val mm = MeshMaker.getMeshMaker
        val aabb = block.getSelectedBoundingBox(pos, MultiBlockPos.default).expand(0.01f, 0.01f, 0.01f)

        val minX = aabb.getMinX
        val minY = aabb.getMinY
        val minZ = aabb.getMinZ
        val maxX = aabb.getMaxX
        val maxY = aabb.getMaxY
        val maxZ = aabb.getMaxZ



        mm.startMake(1)
        mm.addVertex(minX, minY, minZ)
        mm.addVertex(minX, minY, maxZ)
        mm.addVertex(minX, maxY, minZ)
        mm.addVertex(minX, maxY, maxZ)
        mm.addVertex(maxX, minY, minZ)
        mm.addVertex(maxX, minY, maxZ)
        mm.addVertex(maxX, maxY, minZ)
        mm.addVertex(maxX, maxY, maxZ)

        mm.addIndex(0, 1)
        mm.addIndex(0, 2)
        mm.addIndex(0, 4)

        mm.addIndex(6, 2)
        mm.addIndex(6, 4)
        mm.addIndex(6, 7)

        mm.addIndex(3, 1)
        mm.addIndex(3, 2)
        mm.addIndex(3, 7)

        mm.addIndex(5, 1)
        mm.addIndex(5, 4)
        mm.addIndex(5, 7)


        blockSelect = mm.makeMesh()
    }

    def renderBlockMouseOver(): Unit = if (blockMouseOver != null) blockMouseOver.render(textureManager)

    def renderBlockSelect(): Unit = if (blockSelect != null) blockSelect.render(textureManager)

    def renderEntitiesItem(frustum: Frustum, transformation: Transformation): Unit = {


        world.entities.par.filter(_.isInstanceOf[EntityItem]).foreach(
            (entity) => {
                if (frustum.cubeInFrustum(entity.body)) {
                    val modelViewMatrix = transformation.buildEntityModelViewMatrix(entity)
                    EntityRenderer.currentShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
                    GameRegister.getItemRender(entity.asInstanceOf[EntityItem].item).renderInWorld(textureManager)
                }
            }
        )
    }


}
