package ru.megains.game.world

import ru.megains.engine.graph.renderer.texture.TextureManager
import ru.megains.engine.graph.{ShaderProgram, RenderChunk}
import ru.megains.engine.graph.renderer.mesh.{Mesh, MeshMaker}
import ru.megains.game.blockdata.{BlockWorldPos, MultiBlockPos}
import ru.megains.game.util.BlockAndPos
import ru.megains.game.world.chunk.{Chunk, ChunkVoid}

import scala.collection.mutable


class WorldRenderer(val world: World,val textureManager: TextureManager) {
    world.worldRenderer = this

    val renderChunks: mutable.HashMap[Long, RenderChunk] = new mutable.HashMap[Long, RenderChunk]
    var voidRender: RenderChunk = new RenderChunk(new ChunkVoid,textureManager)
    def init() {
        world.chunks.foreach((chunk:(Long,Chunk))=>renderChunks.put(chunk._1, new RenderChunk(chunk._2,textureManager)))
    }





    def reRender(pos: BlockWorldPos) {
        val x: Int = pos.worldX >> 4
        val y: Int = pos.worldY >> 4
        val z: Int = pos.worldZ >> 4
        getRenderChunkRen(x, y, z).reRender()
        getRenderChunkRen(x + 1, y, z).reRender()
        getRenderChunkRen(x - 1, y, z).reRender()
        getRenderChunkRen(x, y + 1, z).reRender()
        getRenderChunkRen(x, y - 1, z).reRender()
        getRenderChunkRen(x, y, z + 1).reRender()
        getRenderChunkRen(x, y, z - 1).reRender()
    }

    def getRenderChunkRen(x: Int, y: Int, z: Int): RenderChunk = {
        val i: Long = (x & 16777215l) << 40 | (z & 16777215l) << 16 | (y & 65535L)

        if (renderChunks.contains(i)) renderChunks(i) else voidRender
    }
    def cleanUp(): Unit =  renderChunks.values.foreach(_.cleanUp)

    var mesh:Mesh = null
    
    def updateBlockBounds(bp:BlockAndPos): Unit ={
        if(mesh!=null) {
            mesh.cleanUp()
            mesh = null
        }
        val mm = MeshMaker.getMeshMaker
        val aabb = bp.block.getSelectedBoundingBox(bp.pos,MultiBlockPos.default).expand(0.01f,0.01f,0.01f)

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

        mm.addIndex(0,1)
        mm.addIndex(0,2)
        mm.addIndex(0,4)

        mm.addIndex(6,2)
        mm.addIndex(6,4)
        mm.addIndex(6,7)

        mm.addIndex(3,1)
        mm.addIndex(3,2)
        mm.addIndex(3,7)

        mm.addIndex(5,1)
        mm.addIndex(5,4)
        mm.addIndex(5,7)


        mesh =  mm.makeMesh()


    }
    def renderBlockBounds(shaderProgram: ShaderProgram): Unit = if(mesh!=null) mesh.render(shaderProgram,textureManager)


}
