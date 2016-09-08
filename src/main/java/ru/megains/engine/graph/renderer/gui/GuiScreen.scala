package ru.megains.engine.graph.renderer.gui

import java.awt.Color

import org.lwjgl.input.Keyboard._
import ru.megains.engine.graph.Renderer
import ru.megains.engine.graph.renderer.RenderItem
import ru.megains.engine.graph.renderer.mesh.Mesh
import ru.megains.game.OrangeCraft
import ru.megains.game.item.ItemStack

abstract class GuiScreen()  {



    var itemRender:RenderItem = _
    var renderer:Renderer = _
    var oc:OrangeCraft = _

    def createRect(width: Int, height: Int, color: Color): Mesh = Gui.createRect(width,height,color)

    def createTextureRect(width: Int, height: Int, texture: String): Mesh = Gui.createTextureRect(width, height, texture)

    def drawObject(mesh: Mesh,xPos:Int,yPos:Int): Unit = Gui.renderObject(xPos,yPos,mesh,renderer)

    def drawItemStack(itemStack: ItemStack,xPos:Int,yPos:Int): Unit = itemRender.renderItemStackToGui(xPos,yPos,itemStack)

    def mouseReleased(x: Int, y: Int, button: Int): Unit = {}

    def mouseClicked(x: Int, y: Int, button: Int): Unit = {}

    def mouseClickMove(x: Int, y: Int): Unit = {}


    def keyTyped(typedChar: Char, keyCode: Int) {
        keyCode match {
            case KEY_E | KEY_ESCAPE => oc.guiManager.setGuiScreen(null)
        }

    }

    def init(orangeCraft: OrangeCraft): Unit = {
        oc = orangeCraft
        itemRender = oc.itemRender
        renderer = oc.renderer
    }

    def drawScreen(mouseX:Int,mouseY:Int): Unit

    def cleanup(): Unit

}
