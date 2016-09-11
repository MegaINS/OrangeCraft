package ru.megains.renderer

import org.joml.Vector3f
import ru.megains.engine.graph.Renderer
import ru.megains.game.OrangeCraft
import ru.megains.game.item.ItemStack
import ru.megains.game.register.GameRegister

class RenderItem(orangeCraft: OrangeCraft) {
    val renderer: Renderer = orangeCraft.renderer

    def renderItemStackToGui(xPos: Int, yPos: Int, itemStack: ItemStack): Unit = {
        if (itemStack != null) {
            val shaderProgram = renderer.hudShaderProgram
            shaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos + 20, yPos + 20, 0, -25, 45, 0, 25))
            shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
            GameRegister.getItemRender(itemStack.item).renderInInventory(shaderProgram, renderer.textureManager)
        }
    }
}
