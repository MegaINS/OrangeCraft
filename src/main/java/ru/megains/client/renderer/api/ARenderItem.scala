package ru.megains.client.renderer.api

import ru.megains.client.renderer.mesh.Mesh
import ru.megains.common.managers.TextureManager

abstract class ARenderItem {

    val inventoryMesh: Mesh

    val worldMesh: Mesh

    def renderInInventory(textureManager: TextureManager): Unit

    def renderInWorld(textureManager: TextureManager): Unit

}
