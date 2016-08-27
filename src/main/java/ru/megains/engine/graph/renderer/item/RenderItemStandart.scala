package ru.megains.engine.graph.renderer.item

import ru.megains.engine.graph.ShaderProgram
import ru.megains.engine.graph.renderer.api.ARenderItem
import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.engine.graph.renderer.texture.TextureManager
import ru.megains.game.item.Item

class RenderItemStandart(val item:Item) extends ARenderItem{

  override val inventoryMesh: Mesh = null

  override def renderInInventory(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {}

  override val worldMesh: Mesh = null

  override def renderInWorld(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit = {
  }
}
