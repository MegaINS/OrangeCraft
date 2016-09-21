package ru.megains.renderer.api

import ru.megains.game.managers.TextureManager
import ru.megains.renderer.graph.ShaderProgram
import ru.megains.renderer.mesh.Mesh

abstract class ARenderItem {

    val inventoryMesh: Mesh

    val worldMesh: Mesh

    def renderInInventory(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit

    def renderInWorld(shaderProgram: ShaderProgram, textureManager: TextureManager): Unit

}
