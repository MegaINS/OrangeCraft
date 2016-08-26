package ru.megains.engine.graph.renderer.api

import ru.megains.engine.graph.ShaderProgram
import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.engine.graph.renderer.texture.TextureManager

abstract class ARenderItem {

    val inventoryMesh:Mesh

    def renderInInventory(shaderProgram: ShaderProgram,textureManager:TextureManager):Unit
}
