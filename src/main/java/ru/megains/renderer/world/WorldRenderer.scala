package ru.megains.renderer.world

import ru.megains.engine.graph.{ShaderProgram, Transformation}
import ru.megains.game.blockdata.{BlockWorldPos, MultiBlockPos}
import ru.megains.game.entity.item.EntityItem
import ru.megains.game.entity.player.EntityPlayer
import ru.megains.game.managers.TextureManager
import ru.megains.game.register.GameRegister
import ru.megains.game.util.BlockAndPos
import ru.megains.game.world.World
import ru.megains.renderer.Frustum
import ru.megains.renderer.mesh.{Mesh, MeshMaker}

import scala.collection.mutable


class WorldRenderer(val world: World, val textureManager: TextureManager) {
    world.worldRenderer = this

    val renderChunks: mutable.HashMap[Long, RenderChunk] = new mutable.HashMap[Long, RenderChunk]
    var mesh: Mesh = _
    val range = 2


    def getRenderChunks(entityPlayer: EntityPlayer, frustum: Frustum): IndexedSeq[RenderChunk] = {
        val posX: Int = entityPlayer.posX / 16 - (if (entityPlayer.posX < 0) 1 else 0) toInt
        val posY: Int = entityPlayer.posY / 16 - (if (entityPlayer.posX < 0) 1 else 0) toInt
        val posZ: Int = entityPlayer.posZ / 16 - (if (entityPlayer.posX < 0) 1 else 0) toInt

        for {x <- posX - range - 1 to posX + range
             y <- posY - range - 1 to posY + range
             z <- posZ - range - 1 to posZ + range
             renderChunk = getRenderChunk(x, y, z)
             if frustum.cubeInFrustum(renderChunk.cube)
        } yield renderChunk

    }


    def reRender(pos: BlockWorldPos) {
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

    def updateBlockBounds(bp: BlockAndPos): Unit = {
        if (mesh != null) {
            mesh.cleanUp()
            mesh = null
        }
        val mm = MeshMaker.getMeshMaker
        val aabb = bp.block.getSelectedBoundingBox(bp.pos, MultiBlockPos.default).expand(0.01f, 0.01f, 0.01f)

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


        mesh = mm.makeMesh()


    }

    def renderBlockBounds(shaderProgram: ShaderProgram): Unit = if (mesh != null) mesh.render(shaderProgram, textureManager)

    def renderEntitiesItem(frustum: Frustum, transformation: Transformation, sceneShaderProgram: ShaderProgram): Unit = {


        world.entities.par.filter(_.isInstanceOf[EntityItem]).foreach(
            (entity) => {
                if (frustum.cubeInFrustum(entity.body)) {
                    val modelViewMatrix = transformation.buildEntityModelViewMatrix(entity)
                    sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix)
                    GameRegister.getItemRender(entity.asInstanceOf[EntityItem].item).renderInWorld(sceneShaderProgram, textureManager)
                }
            }
        )
    }

}
