package ru.megains.renderer.item

import ru.megains.game.item.Item
import ru.megains.game.managers.TextureManager
import ru.megains.renderer.api.ARenderItem
import ru.megains.renderer.graph.ShaderProgram
import ru.megains.renderer.mesh.{Mesh, MeshMaker}
import ru.megains.renderer.texture.TextureAtlas

class RenderItemStandart(val item: Item) extends ARenderItem {

    override lazy val inventoryMesh: Mesh = createInventoryMesh()

    override lazy val worldMesh: Mesh = null

    override def renderInInventory(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {
        inventoryMesh.render(shaderProgram, textureManager)
    }

    override def renderInWorld(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {
    }

    def createWorldMesh(): Mesh = {

        createInventoryMesh()
    }

    def createInventoryMesh(): Mesh = {
        val mm = MeshMaker.getMeshMaker


        val maxX = 32
        val maxY = 32
        val minX = 0
        val minY = 0
        val zZero = 0.0f


        var minU: Float = 0
        var maxU: Float = 0
        var minV: Float = 0
        var maxV: Float = 0
        var texture: TextureAtlas = null


        mm.startMakeTriangles()
        mm.setTexture(TextureManager.locationBlockTexture)
        texture = item.aTexture
        minU = texture.minU
        maxU = texture.maxU
        minV = texture.minV
        maxV = texture.maxV

        mm.setCurrentIndex()
        mm.addVertexWithUV(minX, minY, zZero, minU, maxV)
        mm.addVertexWithUV(minX, maxY, zZero, minU, minV)
        mm.addVertexWithUV(maxX, maxY, zZero, maxU, minV)
        mm.addVertexWithUV(maxX, minY, zZero, maxU, maxV)
        mm.addIndex(0, 2, 1)
        mm.addIndex(0, 3, 2)

        mm.makeMesh()


    }

    //    def createInventoryMesh(): Mesh = {
    //
    //
    //
    //
    //        val maxX = 1f / 2f
    //        val maxY = 1f / 2f
    //        val maxZ = 1f / 2f
    //        val minX = -maxX
    //        val minY = -maxY
    //        val minZ = -maxZ
    //        val averageX: Float = 0f
    //        val averageY: Float = 0f
    //        val averageZ: Float = 0f
    //
    //
    //        var minU: Float = 0
    //        var maxU: Float = 0
    //        var minV: Float = 0
    //        var maxV: Float = 0
    //        var averageU: Float = 0
    //        var averageV: Float = 0
    //        var texture: TextureAtlas = null
    //
    //        val mm = MeshMaker.getMeshMaker
    //
    //
    //
    //        mm.startMakeTriangles()
    //        mm.setTexture(TextureManager.locationBlockTexture)
    //        texture = item.aTexture
    //        minU = texture.minU
    //        maxU = texture.maxU
    //        minV = texture.minV
    //        maxV = texture.maxV
    //        averageU = texture.averageU
    //        averageV = texture.averageV
    //
    //        mm.setCurrentIndex()
    //        mm.addVertexWithUV(minX, maxY, minZ, maxU, maxV)
    //        mm.addVertexWithUV(minX, maxY, maxZ, maxU, minV)
    //        mm.addVertexWithUV(maxX, maxY, minZ, minU, maxV)
    //        mm.addVertexWithUV(maxX, maxY, maxZ, minU, minV)
    //        mm.addVertexWithUV(averageX, maxY, averageZ, averageU, averageV)
    //        mm.addIndex(0, 1, 4)
    //        mm.addIndex(3, 2, 4)
    //        mm.addIndex(1, 3, 4)
    //        mm.addIndex(2, 0, 4)
    //
    //
    //
    //        //V2
    //
    //
    //
    //        minU = texture.minU
    //        maxU = texture.maxU
    //        minV = texture.minV
    //        maxV = texture.maxV
    //        averageU = texture.averageU
    //        averageV = texture.averageV
    //
    //
    //
    //
    //
    //        mm.setCurrentIndex()
    //        mm.addNormals(0, -1, 0)
    //        mm.addVertexWithUV(minX, minY, minZ, minU, maxV)
    //        mm.addVertexWithUV(minX, minY, maxZ, minU, minV)
    //        mm.addVertexWithUV(maxX, minY, minZ, maxU, maxV)
    //        mm.addVertexWithUV(maxX, minY, maxZ, maxU, minV)
    //        mm.addVertexWithUV(averageX, minY, averageZ, averageU, averageV)
    //        mm.addIndex(1, 0, 4)
    //        mm.addIndex(2, 3, 4)
    //        mm.addIndex(3, 1, 4)
    //        mm.addIndex(0, 2, 4)
    //
    //
    //
    //
    //
    //        minU = texture.minU
    //        maxU = texture.maxU
    //        minV = texture.minV
    //        maxV = texture.maxV
    //        averageU = texture.averageU
    //        averageV = texture.averageV
    //
    //
    //
    //
    //        mm.setCurrentIndex()
    //
    //        mm.addVertexWithUV(minX, minY, minZ, minU, maxV)
    //        mm.addVertexWithUV(minX, minY, maxZ, maxU, maxV)
    //        mm.addVertexWithUV(minX, maxY, minZ, minU, minV)
    //        mm.addVertexWithUV(minX, maxY, maxZ, maxU, minV)
    //        mm.addVertexWithUV(minX, averageY, averageZ, averageU, averageV)
    //        mm.addIndex(0, 1, 4)
    //        mm.addIndex(1, 3, 4)
    //        mm.addIndex(3, 2, 4)
    //        mm.addIndex(2, 0, 4)
    //
    //
    //
    //
    //        minU = texture.minU
    //        maxU = texture.maxU
    //        minV = texture.minV
    //        maxV = texture.maxV
    //        averageU = texture.averageU
    //        averageV = texture.averageV
    //
    //
    //
    //        mm.setCurrentIndex()
    //        mm.addColor(0.5f, 0.5f, 0.5f)
    //
    //        mm.addVertexWithUV(maxX, minY, minZ, maxU, maxV)
    //        mm.addVertexWithUV(maxX, minY, maxZ, minU, maxV)
    //        mm.addVertexWithUV(maxX, maxY, minZ, maxU, minV)
    //        mm.addVertexWithUV(maxX, maxY, maxZ, minU, minV)
    //        mm.addVertexWithUV(maxX, averageY, averageZ, averageU, averageV)
    //        mm.addIndex(1, 0, 4)
    //        mm.addIndex(3, 1, 4)
    //        mm.addIndex(2, 3, 4)
    //        mm.addIndex(0, 2, 4)
    //
    //
    //
    //
    //
    //
    //
    //        minU = texture.minU
    //        maxU = texture.maxU
    //        minV = texture.minV
    //        maxV = texture.maxV
    //        averageU = texture.averageU
    //        averageV = texture.averageV
    //
    //
    //
    //
    //
    //        mm.setCurrentIndex()
    //        mm.addColor(0.7f, 0.7f, 0.7f)
    //        mm.addVertexWithUV(minX, minY, maxZ, minU, maxV)
    //        mm.addVertexWithUV(minX, maxY, maxZ, minU, minV)
    //        mm.addVertexWithUV(maxX, minY, maxZ, maxU, maxV)
    //        mm.addVertexWithUV(maxX, maxY, maxZ, maxU, minV)
    //        mm.addVertexWithUV(averageX, averageY, maxZ, averageU, averageV)
    //        mm.addIndex(1, 0, 4)
    //        mm.addIndex(4, 0, 2)
    //        mm.addIndex(4, 2, 3)
    //        mm.addIndex(3, 1, 4)
    //
    //
    //
    //
    //
    //
    //        minU = texture.minU
    //        maxU = texture.maxU
    //        minV = texture.minV
    //        maxV = texture.maxV
    //        averageU = texture.averageU
    //        averageV = texture.averageV
    //
    //
    //
    //        mm.setCurrentIndex()
    //        mm.addColor(1f, 1f, 1f)
    //        mm.addVertexWithUV(minX, minY, minZ, maxU, maxV)
    //        mm.addVertexWithUV(minX, maxY, minZ, maxU, minV)
    //        mm.addVertexWithUV(maxX, minY, minZ, minU, maxV)
    //        mm.addVertexWithUV(maxX, maxY, minZ, minU, minV)
    //        mm.addVertexWithUV(averageX, averageY, minZ, averageU, averageV)
    //        mm.addIndex(0, 1, 4)
    //        mm.addIndex(0, 4, 2)
    //        mm.addIndex(2, 4, 3)
    //        mm.addIndex(1, 3, 4)
    //
    //
    //        mm.makeMesh()
    //    }
}
