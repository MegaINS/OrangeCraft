package ru.megains.client.renderer

import java.awt.Color

import org.joml.Vector3f
import ru.megains.client.OrangeCraft
import ru.megains.client.renderer.gui.Gui
import ru.megains.client.renderer.mesh.Mesh
import ru.megains.common.item.{Item, ItemBlock, ItemStack}
import ru.megains.common.register.GameRegister

import scala.collection.mutable

class RenderItem(orangeCraft: OrangeCraft) extends Gui {
    val renderer: EntityRenderer = orangeCraft.renderer
    val fontRender: FontRender = orangeCraft.fontRender
    val cub: Mesh = createRect(15, 12, Color.white)
    val numberMeshMap = new mutable.HashMap[Int, Mesh]()


    def renderItemStackToGui(xPos: Int, yPos: Int, itemStack: ItemStack): Unit = {
        if (itemStack != null) {
            itemStack.item match {
                case block: ItemBlock =>
                    renderItemBlockToGui(xPos, yPos, block)
                case item: Item =>
                    renderItemToGui(xPos, yPos, item)

            }
            renderItemStackOverlay(xPos, yPos, itemStack)
        }
    }

    def renderItemStackOverlay(xPos: Int, yPos: Int, itemStack: ItemStack): Unit = {
        if (itemStack.stackSize > 1) {
            drawObject(xPos + 2, yPos + 2, cub, renderer)
            drawObject(xPos + 3, yPos + 3, getNumberMesh(itemStack.stackSize), renderer)
        }
    }

    def renderItemBlockToGui(xPos: Int, yPos: Int, itemBlock: ItemBlock): Unit = {
        val shaderProgram = renderer.hudShaderProgram
        shaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos + 20, yPos + 20, 0, -25, 45, 0, 25))
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        GameRegister.getItemRender(itemBlock).renderInInventory(renderer.textureManager)
    }

    def renderItemToGui(xPos: Int, yPos: Int, item: Item): Unit = {
        val shaderProgram = renderer.hudShaderProgram
        shaderProgram.setUniform("modelMatrix", renderer.transformation.buildOrtoProjModelMatrix(xPos + 4, yPos + 4, 1))
        shaderProgram.setUniform("colour", new Vector3f(1f, 1f, 1f))
        GameRegister.getItemRender(item).renderInInventory(renderer.textureManager)
    }

    def getNumberMesh(number: Int): Mesh = {
        numberMeshMap.getOrElse(number, default = {
            val mesh = fontRender.createStringGui(number.toString, Color.black)
            numberMeshMap += number -> mesh
            mesh
        })
    }
}
