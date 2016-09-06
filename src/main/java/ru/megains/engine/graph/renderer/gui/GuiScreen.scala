package ru.megains.engine.graph.renderer.gui

import java.awt.Color

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



    def keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == 1) {
          oc.guiManager.setGuiScreen(null)
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
