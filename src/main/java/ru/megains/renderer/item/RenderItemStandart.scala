package ru.megains.renderer.item

import ru.megains.game.item.Item
import ru.megains.game.managers.TextureManager
import ru.megains.renderer.api.ARenderItem
import ru.megains.renderer.graph.ShaderProgram
import ru.megains.renderer.mesh.Mesh

class RenderItemStandart(val item: Item) extends ARenderItem {

    override val inventoryMesh: Mesh = null
    override val worldMesh: Mesh = null

    override def renderInInventory(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {}

    override def renderInWorld(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {
    }
}
