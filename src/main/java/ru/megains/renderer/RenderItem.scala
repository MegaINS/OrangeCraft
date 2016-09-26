package ru.megains.renderer

import org.joml.Vector3f
import ru.megains.game.OrangeCraft
import ru.megains.game.item.{Item, ItemBlock, ItemStack}
import ru.megains.game.register.GameRegister

class RenderItem(orangeCraft: OrangeCraft) {
    val renderer: EntityRenderer = orangeCraft.renderer

    def renderItemStackToGui(xPos: Int, yPos: Int, itemStack: ItemStack): Unit = {
        if (itemStack != null) {
            itemStack.item match {
                case block: ItemBlock =>
                    renderItemBlockToGui(xPos, yPos, block)
                case item: Item =>
                    renderItemToGui(xPos, yPos, item)

            }
        }
    }

    def renderItemBlockToGui(xPos: Int, yPos: Int, itemBlock: ItemBlock): Unit = {
        val shaderProgram = renderer.hudShaderProgram
        shaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos + 20, yPos + 20, 0, -25, 45, 0, 25))
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        GameRegister.getItemRender(itemBlock).renderInInventory(shaderProgram, renderer.textureManager)
    }

    def renderItemToGui(xPos: Int, yPos: Int, item: Item): Unit = {
        val shaderProgram = renderer.hudShaderProgram
        shaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos + 4, yPos + 4, 1))
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        GameRegister.getItemRender(item).renderInInventory(shaderProgram, renderer.textureManager)
    }
}
