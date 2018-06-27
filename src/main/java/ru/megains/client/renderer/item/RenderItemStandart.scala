package ru.megains.client.renderer.item

import ru.megains.client.renderer.api.ARenderItem
import ru.megains.client.renderer.mesh.{Mesh, MeshMaker}
import ru.megains.client.renderer.texture.TextureAtlas
import ru.megains.common.item.Item
import ru.megains.common.managers.TextureManager

class RenderItemStandart(val item: Item) extends ARenderItem {

    override lazy val inventoryMesh: Mesh = createInventoryMesh()

    override lazy val worldMesh: Mesh = null

    override def renderInInventory(textureManager: TextureManager): Unit = {
        inventoryMesh.render(textureManager)
    }

    override def renderInWorld(textureManager: TextureManager): Unit = {
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

}
